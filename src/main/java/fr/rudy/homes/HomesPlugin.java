package fr.rudy.homes;

import fr.rudy.databaseapi.DatabaseAPI;
import fr.rudy.homes.commands.HomeCommand;
import fr.rudy.homes.manager.HomesManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HomesPlugin extends JavaPlugin {
    private HomesManager homesManager;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("xDatabaseAPI") == null) {
            getLogger().severe("xDatabaseAPI requis !");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        homesManager = new HomesManager(DatabaseAPI.get().getDatabaseManager().getConnection());
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new HomeCommand(this));

        getLogger().info("✅ Plugin HomesPlugin activé !");
    }

    public HomesManager getHomesManager() {
        return homesManager;
    }
}
