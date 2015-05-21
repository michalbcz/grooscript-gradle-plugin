package org.grooscript.gradle.require

import groovyx.gpars.actor.DefaultActor
import org.grooscript.convert.util.ConvertedFile
import org.grooscript.util.GsConsole

/**
 * Created by jorgefrancoleza on 19/5/15.
 */
class RequireJsActor extends DefaultActor {

    private static final WAIT_TIME = 400
    private List<ConvertedFile> listFiles = []
    private Map<String, Long> dateSourceFiles = [:]
    Closure convertAction

    public static RequireJsActor getInstance() {
        new RequireJsActor()
    }

    void act() {
        loop {
            react { source ->
                if (anyFileChanged()) {
                    listFiles = convertAction()
                    updateFilesDateTimes()
                    GsConsole.message("Require.js modules generated from $source")
                }
                sleep(WAIT_TIME)
                this << source
            }
        }
    }

    private updateFilesDateTimes() {
        listFiles.each {
            dateSourceFiles[it.sourceFilePath] = new File(it.sourceFilePath).lastModified()
        }
    }

    private boolean anyFileChanged() {
        !listFiles || listFiles.any {
            !dateSourceFiles[it.sourceFilePath] ||
                    (dateSourceFiles[it.sourceFilePath] != new File(it.sourceFilePath).lastModified())
        }
    }
}