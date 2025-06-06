package dev.mynny.deathChest.listener;

import dev.mynny.deathChest.Main;
import dev.mynny.deathChest.manager.ChestChecker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final ChestChecker chestChecker;

    public PlayerListener(ChestChecker chestChecker) {
        this.chestChecker = chestChecker;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation().getBlock().getLocation();
        Boolean keepInventory = player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);

        if(keepInventory != null && keepInventory) {
            return;
        }

        event.getDrops().clear();

        Block block = deathLocation.getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        chest.setCustomName(ChatColor.of("#ff4400") + "Collect all items from the chest");
        chest.update();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) chest.getInventory().addItem(item);
        }

        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand != null && offHand.getType() != Material.AIR) {
            chest.getInventory().addItem(offHand);
        }
        player.getInventory().clear();
        player.sendMessage(ChatColor.of("#ff4400") + "Your items have been stored in a DeathChest.");
        if(player.hasPermission(Main.deathchest_perm_location)) {
            player.sendMessage(ChatColor.of("#ff4400") + "Your DeathChest is located at " + ChatColor.of("#ff0400") + "X: " + deathLocation.getBlockX() + " Y: " + deathLocation.getBlockY() + " Z: " + deathLocation.getBlockZ());
        }

        Location holoLoc = deathLocation.clone().add(0.5, 1.3, 0.5);
        ArmorStand stand = (ArmorStand) deathLocation.getWorld().spawnEntity(holoLoc, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setCustomName(ChatColor.of("#ff4400") +  "☠ " + ChatColor.of("#c7cedd") + "ʀ.ɪ.ᴘ " + ChatColor.of("#ff0400") + player.getName());
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setSmall(true);

        chestChecker.getActiveChests().put(deathLocation, stand);
    }
}
