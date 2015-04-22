package net.stuxcrystal.airblock.commands.core.exceptions;

import net.stuxcrystal.airblock.commands.Commands;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;
import net.stuxcrystal.airblock.commands.localization.TranslationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Contains the exeption handlers.
 */
public class ExceptionHandlerBag {

    /**
     * The fallback handler.
     */
    private static ExceptionHandler<Throwable> FALLBACK_HANDLER = new ExceptionHandler<Throwable>() {
        @Override
        public void handle(Throwable exception, Commands commands, Executor executor, String label, String raw) {
            commands.getSettings().getLogger().log(
                    Level.SEVERE,
                    commands.getSettings().getEnvironment().getTranslationManager().translate(
                            executor,
                            TranslationManager.COMMAND_FAILURE_LOG,
                            executor.getName(),
                            executor.getUniqueExecutorIdentifier(),
                            label + " " + raw
                    ),
                    exception
            );

            executor.sendMessage(TranslationManager.COMMAND_FAILURE);
        }
    };

    /**
     * Contains the handlers.
     */
    private Map<Class<? extends Throwable>, ExceptionHandler> handlers = new HashMap<Class<? extends Throwable>, ExceptionHandler>();

    /**
     * The child handler bag.
     */
    private ExceptionHandlerBag parent;

    /**
     * Creates a new container for exception handlers.
     * @param parent The parent.
     */
    public ExceptionHandlerBag(ExceptionHandlerBag parent) {
        this.parent = parent;
    }

    /**
     * Creates a new container for exception handlers.
     */
    public ExceptionHandlerBag() {
        this(null);
    }

    /**
     * Registers a new handler in the handler bag.
     * @param exception The exception.
     * @param handler   The handler.
     * @param <T>       The exception type.
     */
    public <T extends Throwable> void registerHandler(Class<T> exception, ExceptionHandler<T> handler) {
        this.handlers.put(exception, handler);
    }

    /**
     * Handles the exception.
     * @param exception   The exception that occured.
     * @param commands    The commands-container that was executed.
     * @param executor    The executor who executed the command.
     * @param label       The label of the command that has been executed.
     * @param raw         The raw arguments that has been passed.
     */
    @SuppressWarnings("unchecked")
    public void onException(Throwable exception, Commands commands, Executor executor, String label, String raw) {
        // Try to match the exception.
        Class<?> exceptionType = exception.getClass();
        while (Throwable.class.isAssignableFrom(exceptionType)) {
            if (this.handlers.containsKey(exceptionType)) {
                this.handlers.get(exceptionType).handle(exception, commands, executor, label, raw);
                return;
            }
            exceptionType = exceptionType.getSuperclass();
        }

        if (this.parent == null)
            // Fall back to the default handler if there is no parent.
            ExceptionHandlerBag.FALLBACK_HANDLER.handle(exception, commands, executor, label, raw);
        else
            // If there is a parent, try to access the parent.
            this.parent.onException(exception, commands, executor, label, raw);
    }
}
