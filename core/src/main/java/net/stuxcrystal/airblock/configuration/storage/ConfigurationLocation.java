package net.stuxcrystal.airblock.configuration.storage;

import lombok.NonNull;
import net.stuxcrystal.airblock.configuration.parser.node.Node;

import java.io.IOException;

/**
 * Contains the location of the current configuration.
 */
public interface ConfigurationLocation {

    /**
     * Can we write to the given location.
     * @param modulePath        The path to the module.
     * @param configuration     The name of the configuration file.
     * @return {@code true} if we can write to the given location.
     */
    public boolean canWrite(@NonNull String[] modulePath, @NonNull String configuration);

    /**
     * Reads a file from the configuration.
     * @param modulePath        The path to the module.
     * @param configuration     The name of the configuration file.
     * @return The node that has been read. If the file does not exists, it returns an empty MapNode.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    public @NonNull Node read(@NonNull String[] modulePath, @NonNull String configuration) throws IOException;

    /**
     * Writes to the given configuration file
     * @param modulePath        The path to the module.
     * @param configuration     The name of the configuration file.
     * @param content           The content of the configuration.
     */
    public void write(@NonNull String[] modulePath, @NonNull String configuration, @NonNull Node content) throws IOException;

}
