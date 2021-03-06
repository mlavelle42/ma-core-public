package com.serotonin.m2m2.rt.script;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.serotonin.m2m2.util.ExportCodes;

public class ScriptLog {
	
	public static String CONTEXT_KEY = "LOG";
	
    public interface LogLevel {
        int TRACE = 1;
        int DEBUG = 2;
        int INFO = 3;
        int WARN = 4;
        int ERROR = 5;
        int FATAL = 6;
        int NONE = 10;
    }

    public static final ExportCodes LOG_LEVEL_CODES = new ExportCodes();
    static {
        LOG_LEVEL_CODES.addElement(LogLevel.NONE, "NONE", "common.none");
        LOG_LEVEL_CODES.addElement(LogLevel.TRACE, "TRACE", "common.logging.trace");
        LOG_LEVEL_CODES.addElement(LogLevel.DEBUG, "DEBUG", "common.logging.debug");
        LOG_LEVEL_CODES.addElement(LogLevel.INFO, "INFO", "common.logging.info");
        LOG_LEVEL_CODES.addElement(LogLevel.WARN, "WARN", "common.logging.warn");
        LOG_LEVEL_CODES.addElement(LogLevel.ERROR, "ERROR", "common.logging.error");
        LOG_LEVEL_CODES.addElement(LogLevel.FATAL, "FATAL", "common.logging.fatal");
    }
	
    private static final String[] LEVEL_STRINGS = { //
    "", //
            "TRACE", //
            "DEBUG", //
            "INFO ", //
            "WARN ", //
            "ERROR", //
            "FATAL", //
    };

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";

    private final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private final PrintWriter out;
    private final int logLevel;

    public ScriptLog(PrintWriter out, int logLevel) {
        this.out = out;
        this.logLevel = logLevel;
    }

    public void close() {
        out.close();
    }

    public boolean trouble() {
        return out.checkError();
    }

    public void trace(Object o) {
        log(o, LogLevel.TRACE);
    }

    public void debug(Object o) {
        log(o, LogLevel.DEBUG);
    }

    public void info(Object o) {
        log(o, LogLevel.INFO);
    }

    public void warn(Object o) {
        log(o, LogLevel.WARN);
    }

    public void error(Object o) {
        log(o, LogLevel.ERROR);
    }

    public void fatal(Object o) {
        log(o, LogLevel.FATAL);
    }

    private void log(Object o, int level) {
        if (level < logLevel)
            return;

        synchronized (out) {
            out.append(LEVEL_STRINGS[level]).append(' ');
            out.append(sdf.format(new Date())).append(" - ");
            out.println(o == null ? "null" : o.toString());
            out.flush();
        }
    }
}
