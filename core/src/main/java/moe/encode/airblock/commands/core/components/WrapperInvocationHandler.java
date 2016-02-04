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

import moe.encode.airblock.commands.core.backend.HandleWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation handler for the wrapper.
 */
public class WrapperInvocationHandler implements InvocationHandler {

    /**
     * The handle wrapper that will be used to call methods.
     */
    final HandleWrapper wrapper;

    /**
     * The interface that will be implemented.
     */
    final Class<?> interfaceCls;

    /**
     * The invocation handler.
     * @param wrapper        The wrapper.
     * @param interfaceCls   The interface class.
     */
    public WrapperInvocationHandler(HandleWrapper wrapper, Class<?> interfaceCls) {
        this.wrapper = wrapper;
        this.interfaceCls = interfaceCls;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.wrapper.getEnvironment().getComponentManager().call(
                this.wrapper,
                this.interfaceCls,
                method,
                args
        );
    }
}
