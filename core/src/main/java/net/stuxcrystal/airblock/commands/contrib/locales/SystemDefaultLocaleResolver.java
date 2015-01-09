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

package net.stuxcrystal.airblock.commands.contrib.locales;

import net.stuxcrystal.airblock.commands.core.backend.ExecutorHandle;
import net.stuxcrystal.airblock.commands.core.components.Component;
import net.stuxcrystal.airblock.commands.core.components.Components;

import java.util.Locale;

/**
 * Implementation for containers.
 */
@Components(LocaleResolver.class)
public class SystemDefaultLocaleResolver {

    /**
     * Returns the default locale of the system.
     * @param executor The executor of the resolver.
     * @return The locale of the system.
     */
    @Component(thread = Component.ExecutionThread.SAME_THREAD, strategy = Component.ExecutionStrategy.NONE)
    public Locale getLocale(ExecutorHandle executor) {
        return Locale.getDefault();
    }

}
