package moe.encode.airblock.commands.contrib.annotations.injections;

import moe.encode.airblock.commands.core.backend.HandleWrapper;
import moe.encode.airblock.utils.ReflectionUtils;

import java.lang.annotation.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Resolves the component of the last object.
 */
public class ComponentStep extends Step {

    /**
     * Injects a single object.
     *
     * @param scope       The scope.
     * @param annotations The annotation.
     * @param type        The type of the component.
     * @param previous    The previous object.
     * @return The new injection object.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object currentStep(Scope scope, Annotation[] annotations, Type type, Object previous) {
        Component component = getAnnotation(Component.class, annotations);
        if (component == null)
            return previous;

        // Handle lists and arrays.
        Boolean array = null;

        Type cls = type;
        if (List.class.equals(ReflectionUtils.toClass(cls))) {
            cls = ReflectionUtils.getGenericArguments(type)[0];
            array = false;
        } else if (ReflectionUtils.toClass(cls).isArray()) {
            cls = ReflectionUtils.getGenericComponentType(cls);
            array = true;
        }

        // Make sure that we use the backend if we need to.
        if (previous == null) {
            previous = scope.getExecutor().getContext().getEnvironment().getBackend();
            if (array != null) {
                if (array)
                    previous = new Object[] {previous};
                else
                    previous = Arrays.asList(previous);
            }
        }

        // Start parsing.
        if (array == null)
            return this.forSingleItem(ReflectionUtils.toClass(type), previous);
        else {
            List<Object> objects = new ArrayList<Object>();
            for (Object item : (Iterable) previous) {
                objects.add(this.forSingleItem(ReflectionUtils.toClass(type), item));
            }

            if (array)
                return objects.toArray();
            else
                return objects;
        }

    }

    public Object forSingleItem(Class<?> component, Object item) {
        if (!(item instanceof HandleWrapper))
            return item;
        return ((HandleWrapper) item).getComponent(component);
    }
}
