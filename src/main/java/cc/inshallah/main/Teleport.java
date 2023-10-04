package cc.inshallah.main;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class Teleport implements CommandExecutor, Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    private NamespacedKey key = new NamespacedKey(plugin, "player-name");

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String invName = event.getView().getTitle();
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (!invName.equals(plugin.getTitle())) return;
        event.setCancelled(true);
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String playerName = null;
        if (container.has(key, PersistentDataType.STRING)) {
            playerName = container.get(key, PersistentDataType.STRING);
        } else {
            return;
        }

        try {
            Player target = Bukkit.getServer().getPlayer(playerName);
            Location targetLocation = target.getLocation();
            targetLocation.setPitch(0);
            player.teleport(targetLocation);
            if (plugin.shouldNotify()) {
                target.sendMessage(plugin.getNotifyMessage().replace("%player%", player.getName()));
            }
            if (plugin.shouldPlaySound()) {
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
                target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 0.8f);
            }
        } catch (Exception e) {
            if (player.hasPermission("teleportgui.admin")) {
                player.sendMessage(ChatColor.RED + "Something went wrong, check console for error.");
            } else {
                player.sendMessage(ChatColor.RED + "Something went wrong.");
            }
        }
    }

    private void CreateTeleportGUI(Player player) {
        Inventory inv = player.getServer().createInventory(null, Math.max(9, player.getServer().getOnlinePlayers().size() / 9), plugin.getTitle());
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        int slot = 0;
        for (Player p : player.getServer().getOnlinePlayers()) {
            if (p == player) return;
            meta.setDisplayName(plugin.getDisplayColor() + player.getName());
            meta.setOwner(player.getName());
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, player.getName());
            head.setItemMeta(meta);
            inv.setItem(slot++, head);
        }
        player.openInventory(inv);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("t")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.getServer().getOnlinePlayers().size() > 1) {
                    if (args.length == 0) {
                        CreateTeleportGUI(player);
                    } else {
                        Player targetPlayer = Bukkit.getPlayer(args[0]);
                        if (targetPlayer == null) {
                            player.sendMessage(ChatColor.RED + "Target player is offline.");
                            return true;
                        } else {
                            int delay = Math.abs(plugin.getTeleportDelay());
                            if (delay != 0) {
                                player.sendMessage(ChatColor.GREEN + String.format("Teleporting to %s in %d seconds.", targetPlayer.getName(), delay));
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.teleport(targetPlayer.getLocation().add(0, 0.1, 0));
                                }
                            }.runTaskLater(plugin, 20L * delay);
                        }
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Insufficient player to use this command.");
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Only player can perform this command.");
            }
        } else if (cmd.getName().equalsIgnoreCase("treload")) {
            if (sender instanceof Player) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Reload Successfully!");
            } else {
                sender.sendMessage(ChatColor.RED + "Only player can perform this command.");
            }
        }
        return false;
    }

}
