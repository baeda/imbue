package dk.skrypalle.imbue;

import org.slf4j.Logger;

enum LoggerFactory {

    ;

    static Logger getLogger() {
        return getLogger(3);
    }

    static Logger getLogger(int callDepth) {
        var loggerName = Thread.currentThread().getStackTrace()[callDepth].getClassName();
        return org.slf4j.LoggerFactory.getLogger(loggerName);
    }

}
