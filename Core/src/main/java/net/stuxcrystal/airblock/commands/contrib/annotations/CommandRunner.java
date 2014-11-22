package net.stuxcrystal.airblock.commands.contrib.annotations;

import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.core.settings.CommandSettings;

/**
 * Used for executing commands asynchronously.
 */
public class CommandRunner implements Runnable {

    private final AnnotationCommand command;

    private final Executor executor;

    private final String rawArguments;

    private final CommandSettings locale;

    public CommandRunner(AnnotationCommand command, Executor executor, String rawArguments) {
        this.command = command;
        this.executor = executor;
        this.rawArguments = rawArguments;
        this.locale = executor.getContext();
    }

    @Override
    public void run() {
        this.executor.pushContext(this.locale);
        this.command.executeNow(this.executor, this.rawArguments);
        this.executor.popContext();
    }
}
