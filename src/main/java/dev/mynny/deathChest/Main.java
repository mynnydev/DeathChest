package dev.mynny.deathChest;

import dev.mynny.deathChest.listener.PlayerListener;
import dev.mynny.deathChest.manager.ChestChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    public static String deathchest_perm_location = "deathchest.location";

    private ChestChecker chestChecker;

    @Override
    public void onEnable() {
        getLogger().info("DeathChest Plugin enabled!");
        getLogger().info("Author: Mynny");
        getLogger().info("Discord: http://discord.seasonworld.net");

        chestChecker = new ChestChecker(this);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(chestChecker), this);

        chestChecker.startDeathChestChecker();
    }

    @Override
    public void onDisable() {
        getLogger().info("DeathChest Plugin disabled!");

        if (chestChecker != null) {
            chestChecker.removeAllHolograms();
        }
    }
}
