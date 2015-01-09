package net.stuxcrystal.airblock.commands.contrib.annotations.injections;

import net.stuxcrystal.airblock.commands.arguments.list.ArgumentContainer;
import net.stuxcrystal.airblock.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * The argument step.
 */
public class ArgumentStep extends Step {

    /**
     * Injects a single object.
     *
     * @param scope       The scope.
     * @param annotations The annotation.
     * @param type        The type of the parameter.
     * @param previous    The previous object.  @return The new injection object.
     */
    @Override
    public Object currentStep(Scope scope, Annotation[] annotations, Type type, Object previous) {
        Argument argument = Step.getAnnotation(Argument.class, annotations);
        if (argument == null)
            return previous;

        boolean array = false;

        Type cls = type;
        if (List.class.equals(ReflectionUtils.toClass(cls))) {
            cls = ReflectionUtils.getGenericArguments(type)[0];
        }else if (ReflectionUtils.toClass(cls).isArray()) {
            cls = ReflectionUtils.getGenericComponentType(cls);
            array = true;
        }

        if (!argument.type().equals(Void.class))
            cls = argument.type();

        if (argument.to() != Integer.MIN_VALUE) {
            ArgumentContainer container = scope.getArgumentList().from(argument.value()).to(argument.to()).step(argument.step());
            if (array)
                return container.as(cls).toArray();
            return container.as(cls).copy();
        } else {
            return scope.getArgumentList().getRaw(argument.value(), type, argument.defaultValue());
        }
    }
}
