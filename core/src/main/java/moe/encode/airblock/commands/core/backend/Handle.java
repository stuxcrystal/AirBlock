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

package moe.encode.airblock.commands.core.backend;

import lombok.*;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * <p>Defines a handle that wraps an object of the underlying backend.</p
 * <p>
 *     Please note that all non-component commands are required to be thread-safe.
 * </p>
 * @param <T> The type of the backend.
 */
@ToString
@RequiredArgsConstructor
public abstract class Handle<T> {

    /**
     * Contains the handle of the object.
     */
    @NonNull
    @Getter
    private final T handle;

    /**
     * <p>Returns a unique identifier that is used on all handles to check if they are
     * essentially the same.</p>
     *
     * <p>
     *     The UUID is not necessarily the same for same wrapped object in each run of the
     *     application. It should be the same for the same wrapped object in each run of the
     *     application.
     * </p>
     *
     * <p>
     *     If this function returns {@code null} it won't make sense to use the function.
     * </p>
     * @return The unique identifier.
     */
    @Nullable
    public abstract UUID getUniqueIdentifier();

    @Override
    public final boolean equals(Object other) {
        // Forcibly ensure that other is never null.
        if (other == null)
            return false;

        // Make sure that we have the same type.
        if (!this.getClass().equals(other.getClass()))
            return false;

        UUID thisUUID = this.getUniqueIdentifier();
        // If this identifier is null, check that
        if (thisUUID == null) {
            // Make sure the other handle is not identifiable.
            if (((Handle) other).getUniqueIdentifier() != null)
                return false;

            // Just check if the handles are equal.
            return this.getHandle().equals(((Handle) other).getHandle());
        }
        // If the UUID is the same, you can probably assume that the object are equal.
        return thisUUID.equals(((Handle) other).getUniqueIdentifier());
    }

}
