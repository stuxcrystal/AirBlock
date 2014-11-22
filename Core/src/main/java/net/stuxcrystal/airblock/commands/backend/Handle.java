package net.stuxcrystal.airblock.commands.backend;

import lombok.*;

/**
 * <p>Defines a handle that wraps an object of the underlying backend.</p
 * <p>
 *     Please note that all non-component commands are required to be thread-safe.
 * </p>
 * @param <T> The type of the backend.
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class Handle<T> {

    /**
     * Contains the handle of the object.
     */
    @NonNull
    @Getter
    private final T handle;

}
