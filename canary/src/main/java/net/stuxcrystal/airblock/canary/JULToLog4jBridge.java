/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.stuxcrystal.airblock.canary;


import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Bridge for JUL to Log4j
 */
public class JULToLog4jBridge {

    /**
     * The handler for Log4j
     */
    private static class Log4jHandler extends Handler {

        final org.apache.logging.log4j.Logger logger;

        Log4jHandler(org.apache.logging.log4j.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void publish(LogRecord record) {
            int level =  record.getLevel().intValue();
            org.apache.logging.log4j.Level newLevel;

            if (level <= Level.FINER.intValue()) {
                newLevel = org.apache.logging.log4j.Level.TRACE;
            } else if (level <= Level.FINE.intValue()) {
                newLevel = org.apache.logging.log4j.Level.DEBUG;
            } else if (level <= Level.INFO.intValue()) {
                newLevel = org.apache.logging.log4j.Level.INFO;
            } else if (level <= Level.WARNING.intValue()) {
                newLevel = org.apache.logging.log4j.Level.WARN;
            } else if (level <= Level.SEVERE.intValue()) {
                newLevel = org.apache.logging.log4j.Level.ERROR;
            } else {
                newLevel = org.apache.logging.log4j.Level.FATAL;
            }

            this.logger.log(newLevel, record.getMessage(), record.getThrown());
        }

        @Override
        public void flush() { }
        @Override
        public void close() throws SecurityException { }
    }

    /**
     * Create a wrapper for a log4j bridge.
     * @param logger The logger to bridge.
     * @return The wrapped logger.
     */
    public static Logger bridge(org.apache.logging.log4j.Logger logger) {
        Logger result = Logger.getLogger(UUID.randomUUID().toString() + "_log4j_" + logger.getName());
        result.setUseParentHandlers(false);
        result.addHandler(new Log4jHandler(logger));
        result.setLevel(Level.ALL);
        return result;
    }

}
