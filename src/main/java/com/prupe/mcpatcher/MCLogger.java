package com.prupe.mcpatcher;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.logging.*;

@Environment(EnvType.CLIENT)
public class MCLogger {
    private static final HashMap<String, MCLogger> allLoggers = new HashMap<String, MCLogger>();

    public static final Level ERROR = new ErrorLevel();

    private static final long FLOOD_INTERVAL = 1000L;
    private static final long FLOOD_REPORT_INTERVAL = 5000L;
    private static final int FLOOD_LIMIT = Config.getInstance().floodMessageLimit;
    private static final int FLOOD_LEVEL = Level.INFO.intValue();

    private final String logPrefix;
    private final Logger logger;

    private boolean flooding;
    private long lastFloodReport;
    private int floodCount;
    private long lastMessage = System.currentTimeMillis();
    private long lastLogEvery = lastMessage;

    public static MCLogger getLogger(String category) {
        return getLogger(category, category);
    }

    public static synchronized MCLogger getLogger(String category, String logPrefix) {
        MCLogger logger = allLoggers.get(category);
        if (logger == null) {
            logger = new MCLogger(category, logPrefix);
            allLoggers.put(category, logger);
        }
        return logger;
    }

    private MCLogger(String category, String logPrefix) {
        this.logPrefix = logPrefix;
        logger = Logger.getLogger(category);
        logger.setLevel(Config.getLogLevel(category));
        logger.setUseParentHandlers(false);
        logger.addHandler(new Handler() {
            private final Formatter formatter = new Formatter() {
                @Override
                public String format(LogRecord record) {
                    Level level = record.getLevel();
                    if (level == Level.CONFIG) {
                        return record.getMessage();
                    } else {
                        String message = record.getMessage();
                        StringBuilder prefix = new StringBuilder();
                        while (message.startsWith("\n")) {
                            prefix.append("\n");
                            message = message.substring(1);
                        }
                        return prefix.toString() + "[" + MCLogger.this.logPrefix + "] " + level.toString() + ": " + message;
                    }
                }
            };

            @Override
            public void publish(LogRecord record) {
                System.out.println(formatter.format(record));
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }

    private boolean checkFlood() {
        long now = System.currentTimeMillis();
        boolean showFloodMessage = false;
        if (now - lastMessage > FLOOD_INTERVAL) {
            if (flooding) {
                reportFlooding(now);
                flooding = false;
            } else {
                floodCount = 0;
            }
        } else if (flooding && now - lastFloodReport > FLOOD_REPORT_INTERVAL) {
            reportFlooding(now);
            showFloodMessage = true;
        }
        lastMessage = now;
        floodCount++;
        if (flooding) {
            return showFloodMessage;
        } else if (floodCount > FLOOD_LIMIT) {
            flooding = true;
            lastFloodReport = now;
            reportFlooding(now);
            return false;
        } else {
            return true;
        }
    }

    private void reportFlooding(long now) {
        if (floodCount > 0) {
            logger.log(Level.WARNING, String.format(
                "%d flood messages dropped in the last %ds", floodCount, (now - lastFloodReport) / 1000L
            ));
        }
        floodCount = 0;
        lastFloodReport = now;
    }

    public boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    public void setLevel(Level level) {
        logger.setLevel(level);
    }

    public void log(Level level, String format, Object... params) {
        if (isLoggable(level)) {
            if (level.intValue() >= FLOOD_LEVEL && !checkFlood()) {
                return;
            }
            logger.log(level, String.format(format, params));
        }
    }

    public void severe(String format, Object... params) {
        log(Level.SEVERE, format, params);
    }

    public void error(String format, Object... params) {
        log(ERROR, format, params);
    }

    public void warning(String format, Object... params) {
        log(Level.WARNING, format, params);
    }

    public void info(String format, Object... params) {
        log(Level.INFO, format, params);
    }

    public void config(String format, Object... params) {
        log(Level.CONFIG, format, params);
    }

    public void fine(String format, Object... params) {
        log(Level.FINE, format, params);
    }

    public void finer(String format, Object... params) {
        log(Level.FINER, format, params);
    }

    public void finest(String format, Object... params) {
        log(Level.FINEST, format, params);
    }

    public boolean logEvery(long milliseconds) {
        long now = System.currentTimeMillis();
        if (now - lastLogEvery > milliseconds) {
            lastLogEvery = now;
            return true;
        } else {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    private static class ErrorLevel extends Level {
        protected ErrorLevel() {
            super("ERROR", (Level.WARNING.intValue() + Level.SEVERE.intValue()) / 2);
        }
    }
}
// ---END EDIT---
