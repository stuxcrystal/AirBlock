package net.stuxcrystal.airblock.commands.contrib.annotations;

import lombok.NonNull;
import net.stuxcrystal.airblock.commands.core.list.CommandList;
import net.stuxcrystal.airblock.commands.core.list.CommandRegistrar;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Simple registrar for annotations.
 */
public class SimpleAnnotationRegistrar implements CommandRegistrar {

    @Override
    public void register(@NonNull CommandList list, @NonNull Object object) {
        if (object instanceof Class) {
            this.register(list, (Class<?>)object, null);
        } else {
            this.register(list, object.getClass(), object);
        }
    }

    private void register(@NonNull CommandList list, @NonNull Class<?> current, @Nullable Object instance) {
        for (Method method : current.getDeclaredMethods()) {
            // Get the command annotation.
            SimpleCommand cmd = method.getAnnotation(SimpleCommand.class);
            if (cmd == null)
                continue;

            // And if there is a command annotation, register it.
            list.register(new LeafAnnotationCommand(cmd, method, instance));
        }

        Class<?> parent = current.getSuperclass();
        if (parent != null)
            this.register(list, parent, instance);
    }
}
