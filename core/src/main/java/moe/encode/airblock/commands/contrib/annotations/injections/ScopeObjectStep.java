package moe.encode.airblock.commands.contrib.annotations.injections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * The executor step.
 */
public class ScopeObjectStep extends Step {

    /**
     * Injects a single object.
     *
     * @param scope       The scope.
     * @param annotations The annotation.
     * @param type
     * @param previous    The previous object.  @return The new injection object.
     */
    @Override
    public Object currentStep(Scope scope, Annotation[] annotations, Type type, Object previous) {
        ScopeObject scopedExecutor = getAnnotation(ScopeObject.class, annotations);
        if (scopedExecutor == null)
            return previous;
        return scopedExecutor.value().getObject(scope.getExecutor());
    }
}
