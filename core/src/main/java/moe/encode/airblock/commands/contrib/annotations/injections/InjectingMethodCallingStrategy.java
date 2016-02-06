package moe.encode.airblock.commands.contrib.annotations.injections;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import moe.encode.airblock.commands.Executor;
import moe.encode.airblock.commands.arguments.list.ArgumentList;
import moe.encode.airblock.commands.contrib.annotations.AnnotationCommand;
import moe.encode.airblock.commands.contrib.annotations.CommandCallingStrategy;
import moe.encode.airblock.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Parses the command.
 */
@RequiredArgsConstructor
public class InjectingMethodCallingStrategy implements CommandCallingStrategy {

    private static final Step[] STEPS = {
            new FlagStep(),
            new ArgumentStep(),
            new ScopeObjectStep(),
            new ComponentStep(),
    };

    @Getter
    @NonNull
    public final Method method;

    @Getter
    public final Object object;

    @Override
    public void call(AnnotationCommand command, Executor executor, String raw) {
        ArgumentList list = new ArgumentList(raw, executor);
        if (!command.checkArgumentLength(executor, list))
            return;

        Scope scope = new Scope(this.method, this.object, command, list, executor);
        Object[] parameters = new Object[this.method.getParameterTypes().length];
        for (Step step : InjectingMethodCallingStrategy.STEPS)
            parameters = step.inject(scope, parameters);

        System.out.println(Arrays.toString(parameters));

        try {
            ReflectionUtils.invoke(this.method, this.object, parameters);
        } catch (Throwable throwable) {
            AnnotationCommand.throwError(
                    "Error occured while '" + executor.getName() + "' executed '/" + command.getName() + " " + raw,
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
