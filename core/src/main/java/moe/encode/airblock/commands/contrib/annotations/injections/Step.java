package moe.encode.airblock.commands.contrib.annotations.injections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Abstract class that represents a dependency injection step.
 */
public abstract class Step {

    /**
     * Executes the current injection step.
     * @param scope      The scope of the method call.
     * @param previous   The previous result of the injection.
     * @return The injection.
     */
    public Object[] inject(Scope scope, Object[] previous) {
        Object[] result = new Object[previous.length];
        for (int i = 0; i<result.length; i++)
            result[i] = this.currentStep(
                    scope,
                    scope.getMethod().getParameterAnnotations()[i],
                    scope.getMethod().getGenericParameterTypes()[i],
                    previous[i]
            );
        return result;
    }

    /**
     * Injects a single object.
     * @param scope          The scope.
     * @param annotations    The annotation.
     * @param previous       The previous object.
     * @return The new injection object.
     */
    public abstract Object currentStep(Scope scope, Annotation[] annotations, Type type, Object previous);

    /**
     * Get the annotation in the list.
     * @param cls            The class.
     * @param annotations    The annotations.
     * @param <T>            The types.
     * @return The annotation.
     */
    @SuppressWarnings("unchecked")
    protected static <T> T getAnnotation(Class<T> cls, Annotation[] annotations) {
        for (Annotation annotation : annotations)
            if (cls.isInstance(annotation))
                return (T)annotation;
        return null;
    }

}
