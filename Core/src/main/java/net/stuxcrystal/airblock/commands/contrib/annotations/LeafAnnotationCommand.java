package net.stuxcrystal.airblock.commands.contrib.annotations;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.Executor;
import net.stuxcrystal.airblock.commands.arguments.list.ArgumentList;
import net.stuxcrystal.airblock.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Just executes the command.
 */
public class LeafAnnotationCommand extends AnnotationCommand {

    /**
     * Creates a new annotation command.
     *
     * @param command The new command.
     * @param method  The method.
     */
    public LeafAnnotationCommand(SimpleCommand command, Method method, Object instance) {
        super(command, method, instance);
    }

    @Override
    public void executeNow(@NonNull Executor executor, @NonNull String rawArguments) {
        // Parse the arguments.
        ArgumentList al = new ArgumentList(rawArguments, this.getCommand().useFlags(), executor);

        try {
            ReflectionUtils.invoke(this.getMethod(), this.getInstance(), executor, al);
        } catch (Throwable throwable) {
            executor.getEnvironment().getBackend().getLogger().log(
                    Level.SEVERE,
                    "Error occured while '" +  executor.getName() +"' executed '/" + this.getName() + " " + rawArguments,
                    throwable);
        }
    }

    @Override
    public void execute(@NonNull Executor executor, @NonNull String rawArguments) {
        if (this.getCommand().async()) {
            executor.getEnvironment().getBackend().runAsynchronously(new CommandRunner(this, executor, rawArguments));
        } else {
            this.executeNow(executor, rawArguments);
        }
    }
}
