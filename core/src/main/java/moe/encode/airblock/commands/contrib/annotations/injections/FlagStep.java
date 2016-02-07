package moe.encode.airblock.commands.contrib.annotations.injections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Looks if there is a flag annotation and assigns it.
 */
public class FlagStep extends Step {

    @Override
    public Object currentStep(Scope scope, Annotation[] annotations, Type type, Object previous) {
        Flag flag = getAnnotation(Flag.class, annotations);
        if (flag == null)
            return null;
        return scope.getArgumentList().hasFlag(flag.value());
    }
}
