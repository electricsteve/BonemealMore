package electricsteve.bonemealmore;

import electricsteve.bonemealmore.Events.InteractListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class BonemealMore extends JavaPlugin {
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.addDefault("blocks.sugar_cane", true);
        config.addDefault("blocks.cactus", true);
        config.options().copyDefaults(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
