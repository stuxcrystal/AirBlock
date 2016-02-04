package moe.encode.airblock.configuration.storage.storage.multi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import moe.encode.airblock.configuration.storage.storage.ConfigurationStorage;
import moe.encode.airblock.configuration.parser.files.FileType;

/**
 * <p>Storage that uses multiple storage systems. Nice for shadowing.</p>
 * <p>Please note that the multi-storage will use the default-filetype of the write-storage.</p>
 */
@AllArgsConstructor
public class MultiStorage extends ConfigurationStorage {

    /**
     * The storage that will be used to write data into.
     */
    @NonNull
    @Getter
    @Setter
    private ConfigurationStorage writeStorage;

    /**
     * The storage that will be used to wr
     */
    @NonNull
    @Getter
    @Setter
    private ConfigurationStorage[] childStorage;

    /**
     * Stores the location of the configuration.
     *
     * @param modulePath    The path to the module.
     * @param configuration The configuration of the module.
     * @return The given configuration.
     */
    @Override
    public MultiStorageLocation getConfiguration(@NonNull String[] modulePath, @NonNull String configuration) {
        return new MultiStorageLocation(this, modulePath, configuration);
    }

    /**
     * Returns the File-Type of the given configuration file.
     *
     * @param modulePath    The module path.
     * @param configuration The configuration.
     * @return The file type.
     */
    @Override
    public FileType getType(@NonNull String[] modulePath, @NonNull String configuration) {
        return this.writeStorage.getType(modulePath, configuration);
    }
}
