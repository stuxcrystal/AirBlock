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

package net.stuxcrystal.airblock.commands.backend;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.stuxcrystal.airblock.commands.core.settings.Environment;

/**
 * The wrapper for the handle.
 */
@RequiredArgsConstructor
public abstract class HandleWrapper<T extends Handle> {

    @NonNull
    @Getter(AccessLevel.PUBLIC)
    private final T handle;

    @NonNull
    @Getter(AccessLevel.PUBLIC)
    private final Environment environment;

    /**
     * Returns the interface that is implemented by the given component.
     * @param component The component which implementation should be returned.
     * @param <C> The type of the component.
     * @return The given component.
     */
    public <C> C getComponent(Class<C> component) {
        return this.getEnvironment().getComponentManager().getInterface(component, this);
    }

    /**
     * Checks if the given component has been registered.
     * @param component  The component to check.
     * @return If the component has been registered.
     */
    public boolean hasComponent(Class<?> component) {
        return this.getEnvironment().getComponentManager().isImplemented(component, this);
    }
}
