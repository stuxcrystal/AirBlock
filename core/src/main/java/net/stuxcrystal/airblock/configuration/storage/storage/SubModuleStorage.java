package net.stuxcrystal.airblock.configuration.storage.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.stuxcrystal.airblock.configuration.parser.files.FileType;
import net.stuxcrystal.airblock.configuration.storage.ConfigurationModule;
import net.stuxcrystal.airblock.configuration.storage.location.ConfigurationLocation;
import org.apache.commons.lang3.ArrayUtils;

/**
 * A storage that will use the storage of the given submodule using
 * the path of the submodule as root..
 */
public class SubModuleStorage extends ConfigurationStorage {

    /**
     * The configuration module.
     */
    @NonNull
    @Getter
    private final String[] module;

    @NonNull
    @Getter
    private final ConfigurationStorage storage;

    /**
     * The storage for the submodule.
     * @param module   The module
     * @param storage  The storage
     */
    public SubModuleStorage(@NonNull String[] module, @NonNull ConfigurationStorage storage) {
        this.module = module;
        this.storage = storage;
    }

    /**
     * Pass a configuration module into it.
     * @param module The configuration module
     */
    public SubModuleStorage(@NonNull ConfigurationModule module) {
        this.module = module.getEffectiveModulePath();
        this.storage = module.getStorage();
    }

    /**
     * Joins the effective module path of the submodule with the given module path.
     * @param modulePath  The effective module path.
     * @return the joined path.
     */
    String[] joinPathes(String[] modulePath) {
        return ArrayUtils.addAll(module, modulePath);
    }

    /**
     * Stores the location of the configuration.
     *
     * @param modulePath    The path to the module.
     * @param configuration The configuration of the module.
     * @return The given configuration.
     */
    @Override
    public ConfigurationLocation getConfiguration(@NonNull String[] modulePath, @NonNull String configuration) {
        return this.storage.getConfiguration(this.joinPathes(modulePath), configuration);
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
        return this.storage.getType(this.joinPathes(modulePath), configuration);
    }
}
