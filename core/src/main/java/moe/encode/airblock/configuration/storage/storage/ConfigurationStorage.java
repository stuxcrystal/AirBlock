package moe.encode.airblock.configuration.storage.storage;

import lombok.NonNull;
import moe.encode.airblock.configuration.parser.files.FileType;
import moe.encode.airblock.configuration.parser.node.Node;
import moe.encode.airblock.configuration.storage.ConfigurationModule;
import moe.encode.airblock.configuration.storage.location.ConfigurationLocation;
import moe.encode.airblock.configuration.storage.storage.multi.MultiStorage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains the location of the current configuration.
 */
public abstract class ConfigurationStorage {

    /**
     * Stores the location of the configuration.
     * @param modulePath      The path to the module.
     * @param configuration   The configuration of the module.
     * @return The given configuration.
     */
    public abstract @NonNull ConfigurationLocation getConfiguration(@NonNull String[] modulePath, @NonNull String configuration);

    /**
     * Checks if we can write to this configuration file.
     * @param modulePath      The module path.
     * @param configuration   The name of the configuration file.
     * @return {@code true} if we can write to the configuration file.
     */
    public boolean canWrite(@NonNull String[] modulePath, @NonNull String configuration) {
        return this.getConfiguration(modulePath, configuration).canWrite();
    }

    /**
     * Returns the File-Type of the given configuration file.
     * @param modulePath      The module path.
     * @param configuration   The configuration.
     * @return The file type.
     */
    public abstract @NonNull FileType getType(@NonNull String[] modulePath, @NonNull String configuration);

    /**
     * Returns the input-stream behind this location.
     * @param modulePath     The module path.
     * @param configuration  The configuration.
     * @return The InputStream.
     * @throws IOException If an I/O-Operation fails.
     */
    public @NonNull InputStream getInputStream(@NonNull String[] modulePath, @NonNull String configuration) throws IOException {
        return this.getConfiguration(modulePath, configuration).getInputStream();
    }

    /**
     * Parses the contents of the configuration file.
     * @param modulePath     The module path.
     * @param configuration  The configuration.
     * @param ft The FileType.
     * @return The node.
     * @throws IOException If an I/O-Operation fails.
     */
    public @NonNull Node read(@NonNull String[] modulePath, @NonNull String configuration, @NonNull FileType ft) throws IOException {
        return this.getConfiguration(modulePath, configuration).read(ft);
    }

    /**
     * Parses the contents of the configuration file.
     * @param modulePath     The module path.
     * @param configuration  The configuration.
     * @return The node.
     * @throws IOException If an I/O-Operation fails.
     */
    public @NonNull Node read(@NonNull String[] modulePath, @NonNull String configuration) throws IOException {
        return this.read(modulePath, configuration, this.getType(modulePath, configuration));
    }

    /**
     * Returns the input-stream behind this location.
     * @param modulePath     The module path.
     * @param configuration  The configuration.
     * @return The InputStream.
     * @throws IOException If an I/O-Operation fails.
     */
    public @NonNull OutputStream getOutputStream(@NonNull String[] modulePath, @NonNull String configuration) throws IOException {
        return this.getConfiguration(modulePath, configuration).getOutputStream();
    }

    /**
     * Parses the contents of the configuration file.
     * @param modulePath     The module path.
     * @param configuration  The configuration.
     * @param node           The node to write.
     * @param ft             The FileType.
     * @throws IOException If an I/O-Operation fails.
     */
    public void write(@NonNull String[] modulePath, @NonNull String configuration, @NonNull Node node, @NonNull FileType ft) throws IOException {
        this.getConfiguration(modulePath, configuration).write(node, ft);
    }

    /**
     * Parses the contents of the configuration file.
     * @param modulePath     The module path.
     * @param configuration  The configuration.
     * @param node           The node to write.
     * @return The node.
     * @throws IOException If an I/O-Operation fails.
     */
    public void write(@NonNull String[] modulePath, @NonNull String configuration, @NonNull Node node) throws IOException {
        this.write(modulePath, configuration, node, this.getType(modulePath, configuration));
    }

    /**
     * Adds the given storages to the module.
     * @param module    The module whose storages should be added.
     * @param storages  The storages that should be added for reading.
     */
    public static void addToModule(@NonNull ConfigurationModule module, @NonNull ConfigurationStorage... storages) {
        ConfigurationStorage moduleStorage = module.getStorage();
        // Add the storages to the multistorage of the module.
        if (moduleStorage instanceof MultiStorage) {
            ((MultiStorage) moduleStorage).setChildStorage(ArrayUtils.addAll(
                    ((MultiStorage) moduleStorage).getChildStorage(),
                    storages
            ));
        } else {
            // Just wrap the module-storage otherwise.
            module.setLocation(new MultiStorage(module.toStorage(), storages));
        }
    }

}
