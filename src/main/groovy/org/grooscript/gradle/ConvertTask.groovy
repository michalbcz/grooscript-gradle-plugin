package org.grooscript.gradle

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.grooscript.GrooScript

/**
 * User: jorgefrancoleza
 * Date: 14/12/13
 */
class ConvertTask extends GrooscriptTask {

    @TaskAction
    void convert() {
        println 'Starting convert...'
        println ' source: ' + source
        println ' destination: ' + destination
        checkProperties()
        source.add(project.file('src/main/groovy/gs/Initial.groovy'))
        if (!source || !destination) {
            throw new GradleException("Need define source and destination.")
        } else {
            doConversion()
        }
    }

    private void doConversion() {
        println 'Do conversion' + inputs.files.files
        GrooScript.clearAllOptions()
        conversionProperties.each { String key, value ->
            GrooScript.setConversionProperty(key, value)
        }
        GrooScript.convert(source, destination)
    }
}
