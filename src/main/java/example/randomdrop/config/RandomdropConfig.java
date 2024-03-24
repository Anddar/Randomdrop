package example.randomdrop.config;

import example.randomdrop.Randomdrop;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * This class will acquire and initialize all needed instances of configuration files.
 * Ensuring multiple instances are not popping up everywhere around the plugin.
 */
public class RandomdropConfig {
    private static FileConfiguration defaultConfig;

    public RandomdropConfig(Randomdrop instanceMain) {
        instanceMain.saveDefaultConfig();

        defaultConfig = instanceMain.getConfig();

        setDefaults(); // Set default values for the config
    }

    public FileConfiguration getDefaultConfig() {
        return defaultConfig;
    }

    private void setDefaults() {
        defaultConfig.addDefault("enabled", true);
        defaultConfig.addDefault("randomdrop_chance", 30);
    }
}
