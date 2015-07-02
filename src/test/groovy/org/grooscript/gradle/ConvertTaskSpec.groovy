package org.grooscript.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.grooscript.GrooScript
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: jorgefrancoleza
 * Date: 14/12/13
 */
class ConvertTaskSpec extends Specification {

    def 'create the task'() {
        expect:
        task instanceof ConvertTask
    }

    def 'by default properties come from project.grooscript'() {
        given:
        GroovySpy(GrooScript, global: true)
        project.extensions.grooscript = [source: [new File('1')], destination: new File('2'), customization: { -> },
                classPath: ['3'], initialText: 'initial', finalText: 'final',
                recursive: true, mainContextScope: ['7'], addGsLib: 'grooscript', requireJs: false]

        when:
        task.convert()

        then:
        1 * GrooScript.getDefaultOptions()
        1 * GrooScript.clearAllOptions()
        1 * GrooScript.setConversionProperty('customization', project.grooscript.customization)
        1 * GrooScript.setConversionProperty('classPath', { it.size() == 1 && it[0].endsWith('3') })
        1 * GrooScript.setConversionProperty('initialText', project.grooscript.initialText)
        1 * GrooScript.setConversionProperty('finalText', project.grooscript.finalText)
        1 * GrooScript.setConversionProperty('recursive', project.grooscript.recursive)
        1 * GrooScript.setConversionProperty('mainContextScope', project.grooscript.mainContextScope)
        1 * GrooScript.setConversionProperty('addGsLib', project.grooscript.addGsLib)
        1 * GrooScript.convert([new File('1')], new File('2')) >> null
        1 * GrooScript.setConversionProperty('requireJs', project.grooscript.requireJs)
        0 * _
    }

    def 'doesn\'t override task properties'() {
        given:
        GroovySpy(GrooScript, global: true)
        task.source = SOURCE
        task.destination = DESTINATION
        project.extensions.grooscript = [source: [new File('1')], destination: new File('2'), customization: { -> },
                classPath: ['3'], recursive: false]

        when:
        task.convert()

        then:
        1 * GrooScript.convert(SOURCE, DESTINATION)
    }

    @Unroll
    def 'run the task without source or destination throws error'() {
        when:
        task.source = source
        task.destination = destination
        task.convert()

        then:
        thrown(GradleException)

        where:
        source  |destination
        SOURCE |null
        null    |null
        null    |DESTINATION
    }

    def 'run the task with correct data'() {
        given:
        GroovySpy(GrooScript, global: true)
        project.extensions.grooscript = [:]

        when:
        task.source = SOURCE
        task.destination = DESTINATION
        task.convert()

        then:
        1 * GrooScript.convert(SOURCE, DESTINATION)
        task.inputs.files.files == [new File(projectDir + System.getProperty('file.separator') + SOURCE[0])] as Set
        task.outputs.files.files == [new File(projectDir + System.getProperty('file.separator') + DESTINATION)] as Set
    }

    def 'convert tasks with options'() {
        given:
        GroovySpy(GrooScript, global: true)
        task.source = SOURCE
        task.destination = DESTINATION
        task.classPath = ['d']
        task.customization = { true }
        task.initialText = 'initial'
        task.finalText = 'final'
        task.recursive = true
        task.mainContextScope = [',']
        task.addGsLib = 'include'

        when:
        task.convert()

        then:
        1 * GrooScript.getDefaultOptions()
        1 * GrooScript.clearAllOptions()
        1 * GrooScript.setConversionProperty('customization', task.customization)
        1 * GrooScript.setConversionProperty('classPath', { it.size() == 1 && it[0].endsWith(task.classPath) })
        1 * GrooScript.setConversionProperty('initialText', task.initialText)
        1 * GrooScript.setConversionProperty('finalText', task.finalText)
        1 * GrooScript.setConversionProperty('recursive', task.recursive)
        1 * GrooScript.setConversionProperty('mainContextScope', task.mainContextScope)
        1 * GrooScript.setConversionProperty('addGsLib', task.addGsLib)
        1 * GrooScript.setConversionProperty('requireJs', task.requireJs)
        1 * GrooScript.convert(SOURCE, DESTINATION) >> null
        0 * _
    }

    static final SOURCE = [new File('source')]
    static final DESTINATION = new File('destination')
    Project project
    ConvertTask task
    String projectDir

    def setup() {
        project = ProjectBuilder.builder().build()

        task = project.task('convert', type: ConvertTask)
        task.project = project
        projectDir = task.project.projectDir.absolutePath
    }
}
