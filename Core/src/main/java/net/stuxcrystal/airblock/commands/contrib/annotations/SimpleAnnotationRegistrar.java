/*
 * AirBlock - Framework for Multi-Platform Minecraft-Plugins.
 * Copyright (C) 2014 stux!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
