package dev.mynny.deathChest.manager;

import dev.mynny.deathChest.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class ChestChecker {

    private final Main plugin;

    public ChestChecker(Main plugin) {
        this.plugin = plugin;
    }

    private final Map<Location, ArmorStand> activeChests = new HashMap<>();

    public Map<Location, ArmorStand> getActiveChests() {
        return activeChests;
    }

    public void startDeathChestChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Set<Location> toRemove = new HashSet<>();
                for (Map.Entry<Location, ArmorStand> entry : activeChests.entrySet()) {
                    Location loc = entry.getKey();
                    Block block = loc.getBlock();

                    if (block.getType() == Material.CHEST) {
                        Chest chest = (Chest) block.getState();
                        boolean empty = Arrays.stream(chest.getInventory().getContents())
                                .allMatch(item -> item == null || item.getType() == Material.AIR);

                        if (empty) {
                            block.setType(Material.AIR);
                            entry.getValue().remove();
                            toRemove.add(loc);
                        }
                    } else {
                        entry.getValue().remove();
                        toRemove.add(loc);
                    }
                }
                toRemove.forEach(activeChests::remove);
            }
        }.runTaskTimer(plugin, 20 * 10L, 20 * 10L);
    }

    public void removeAllHolograms() {
        for (ArmorStand stand : activeChests.values()) {
            if (!stand.isDead()) {
                stand.remove();
            }
        }
        activeChests.clear();
    }
}