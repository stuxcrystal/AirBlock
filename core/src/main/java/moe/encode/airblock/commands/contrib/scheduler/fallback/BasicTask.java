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

package moe.encode.airblock.commands.contrib.scheduler.fallback;

import moe.encode.airblock.commands.contrib.scheduler.Task;

import java.util.concurrent.Future;

/**
 * The task.
 */
public class BasicTask implements Task {

    private final Future task;

    public BasicTask(Future task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        this.task.cancel(true);
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public boolean isCompleted() {
        return this.task.isDone();
    }
}
