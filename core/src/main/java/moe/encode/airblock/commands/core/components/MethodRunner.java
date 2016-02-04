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

package moe.encode.airblock.commands.core.components;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Executes the method.
 * @param <V> The return type.
 */
@RequiredArgsConstructor
public class MethodRunner<V> implements Callable<V> {

    /**
     * The method that should be executed.
     */
    private final Method method;

    /**
     * The instance on which the method will be called.
     */
    private final Object instance;

    /**
     * The parameters that have been passed.
     */
    private final Object[] parameters;

    /**
     * Call the method.
     * @return The result of the method.
     * @throws Exception If the method threw an exception.
     */
    @Override
    public V call() throws Exception {
        try {
            return ComponentManager.invoke(method, instance, parameters);
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
