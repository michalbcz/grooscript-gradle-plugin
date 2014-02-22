package org.grooscript.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.grooscript.gradle.util.InitTools
import spock.lang.Specification

/**
 * Created by jorge on 15/02/14.
 */
class InitStaticWebTaskSpec extends Specification {

    Project project
    InitStaticWebTask task
    InitTools initTools

    def setup() {
        initTools = Mock(InitTools)
        project = ProjectBuilder.builder().build()
        task = project.task('initStaticWeb', type: InitStaticWebTask)
        task.initTools = initTools
        task.project = project
    }

    def 'create the task'() {
        expect:
        task instanceof InitStaticWebTask
    }

    def 'can\'t init if index.html file already exists'() {
        when:
        task.initStaticWeb()

        then:
        1 * initTools.existsFile('src/main/webapp/index.html') >> true
        thrown(GradleException)
    }

    def 'init static web'() {
        when:
        task.initStaticWeb()

        then:
        1 * initTools.existsFile('src/main/webapp/index.html') >> false
        1 * initTools.createDirs('src/main/webapp/js/lib') >> true
        1 * initTools.createDirs('src/main/webapp/js/app') >> true
        1 * initTools.createDirs('src/main/groovy') >> true
        1 * initTools.saveFile('src/main/webapp/index.html', task.HTML_TEXT) >> true
        1 * initTools.saveFile('src/main/groovy/Presenter.groovy', task.PRESENTER_TEXT) >> true
        1 * initTools.saveFile('src/main/webapp/js/main.js', task.MAIN_REQUIRE_TEXT) >> true
        1 * initTools.extractGrooscriptJarFile('grooscript.js', 'src/main/webapp/js/lib/grooscript.js') >> true
        1 * initTools.extractGrooscriptJarFile('grooscript-builder.js', 'src/main/webapp/js/lib/grooscript-builder.js') >> true
        1 * initTools.extractJarFile('grooscript-binder.js', 'src/main/webapp/js/lib/grooscript-binder.js') >> true
        1 * initTools.extractJarFile('JQueryUtils.js', 'src/main/webapp/js/lib/JQueryUtils.js') >> true
        1 * initTools.saveRemoteFile('src/main/webapp/js/lib/require.min.js', task.REQUIRE_JS_REMOTE) >> true
        1 * initTools.saveRemoteFile('src/main/webapp/js/lib/jquery.min.js', task.JQUERY_JS_REMOTE) >> true
        0 * initTools._
        noExceptionThrown()
    }
}
