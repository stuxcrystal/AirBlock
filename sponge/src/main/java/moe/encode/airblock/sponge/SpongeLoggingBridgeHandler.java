package moe.encode.airblock.sponge;

import org.slf4j.Logger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Implementation of a handler bridge.
 */
public class SpongeLoggingBridgeHandler extends Handler {

    Logger logger;

    public SpongeLoggingBridgeHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void publish(LogRecord record) {
        int level = record.getLevel().intValue();
        Throwable throwable = record.getThrown();
        String message = record.getMessage();

        if (level > Level.FINEST.intValue()) {
            this.logger.debug(message, throwable);
        } else if (level > Level.INFO.intValue()) {
            this.logger.info(message, throwable);
        } else if (level > Level.WARNING.intValue()) {
            this.logger.warn(message, throwable);
        } else if (level > Level.SEVERE.intValue()) {
            this.logger.error(message, throwable);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {

    }
}
