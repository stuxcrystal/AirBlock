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

package net.stuxcrystal.airblock.commands.core.settings;

import lombok.Getter;
import lombok.Setter;
import net.stuxcrystal.airblock.commands.arguments.ArgumentConverter;
import net.stuxcrystal.airblock.commands.arguments.split.ArgumentSplitter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.logging.Logger;


/**
 * Stores settings for the commands.
 */
@NotThreadSafe
public class CommandLocale implements CommandSettings {

    /**
     * The parent settings.
     */
    @Getter
    private final CommandSettings parent;

    /**
     * Contains the argument converter.
     */
    @Setter
    public ArgumentConverter argumentConverter;

    /**
     * Contains the argument splitter.
     */
    @Setter
    public ArgumentSplitter argumentSplitter;

    /**
     * Creates a new command setting module.
     * @param parent The parent locale
     */
    public CommandLocale(CommandSettings parent) {
        this.parent = parent;
    }

    @Override
    public ArgumentConverter getArgumentConverter() {
        if (this.argumentConverter != null)
            return this.argumentConverter;
        return this.parent.getArgumentConverter();
    }

    @Override
    public ArgumentSplitter getArgumentSplitter() {
        if (this.argumentSplitter != null)
            return this.argumentSplitter;
        return this.parent.getArgumentSplitter();
    }

    @Nonnull
    @Override
    public Environment getEnvironment() {
        return this.getParent().getEnvironment();
    }

    @Nonnull
    @Override
    public Logger getLogger() {
        return this.getEnvironment().getLogger();
    }
}
