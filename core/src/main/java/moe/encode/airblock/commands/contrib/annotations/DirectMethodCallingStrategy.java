package moe.encode.airblock.commands.contrib.annotations;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import moe.encode.airblock.commands.Executor;
import moe.encode.airblock.commands.arguments.list.ArgumentList;
import moe.encode.airblock.commands.localization.TranslationManager;
import moe.encode.airblock.utils.ChatColor;
import moe.encode.airblock.utils.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Strategy that directly calls methods.
 */
@RequiredArgsConstructor
public class DirectMethodCallingStrategy implements CommandCallingStrategy {

    @Getter
    @NonNull
    public final Method method;

    @Getter
    public final Object object;

    /**
     * Executes the command.
     * @param command   The command that has been executed.
     * @param executor  The executor that is executed.
     * @param raw       The raw command.
     */
    @Override
    public void call(AnnotationCommand command, Executor executor, String raw) {
        ArgumentList list = new ArgumentList(raw, executor);

        if (!command.checkArgumentLength(executor, list))
            return;

        try {
            ReflectionUtils.invoke(this.method, this.object, list);
        } catch (Throwable throwable) {
            AnnotationCommand.throwError(
                    "Error occured while '" +  executor.getName() +"' executed '/" + command.getName() + " " + raw,
                    throwable, executor
            );
        }
    }

    /**
     * The raw name of the command.
     *
     * @return The raw name.
     */
    @Override
    public String getName() {
        return this.getMethod().getName();
    }
}
