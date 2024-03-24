package example.randomdrop;

import example.randomdrop.config.RandomdropConfig;
import example.randomdrop.listeners.ChunkLoadListen;
import org.bukkit.plugin.java.JavaPlugin;

public final class Randomdrop extends JavaPlugin {
    private static RandomdropConfig RDConfigInstance;

    @Override
    public void onEnable() {
        // -------------------------CONFIG INITIALIZATION/CHECKS-----------------------------
        RDConfigInstance = new RandomdropConfig(this);

        // Shutdown if the plugin is disabled (meaning enabled in config.yml is false)
        randomdropIsEnabled();
        // ----------------------------------------------------------------------------------


        // ----------------------------------LISTENERS---------------------------------------

        getServer().getPluginManager().registerEvents(new ChunkLoadListen(getLogger(), RDConfigInstance), this); // Listen for chunk loads

        // ----------------------------------------------------------------------------------
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Shuts down the plugin if the plugin is disabled via config.yml
    private void randomdropIsEnabled() {
        if (!RDConfigInstance.getDefaultConfig().getBoolean("enabled")) {
            getLogger().info("RandomDrop is disabled. Update config.yml and restart server to re-enable the plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
