package cc.inshallah.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    FileConfiguration cfg = this.getConfig();

    @Override
    public void onEnable() {
        setupConfig();
        Bukkit.getPluginManager().registerEvents(new Teleport(), this);
        this.getCommand("t").setExecutor(new Teleport());
        this.getCommand("treload").setExecutor(new Teleport());
    }

    @Override
    public void onDisable() {
        //
    }

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', cfg.getString("inventory-title"));
    }

    public String getDisplayColor() {
        return ChatColor.translateAlternateColorCodes('&', cfg.getString("display-name-color"));
    }

    public String getNotifyMessage() {
        return ChatColor.translateAlternateColorCodes('&', cfg.getString("notify-message"));
    }

    public boolean shouldNotify() {
        return cfg.getBoolean("notify-target");
    }

    public boolean shouldPlaySound() {
        return cfg.getBoolean("teleport-sound");
    }

    private void setupConfig() {
        cfg.addDefault("inventory-title", "&cChoose a player!");
        cfg.addDefault("display-name-color", "&b");
        cfg.addDefault("notify-message", "&a%player% &eteleported to you!");
        cfg.addDefault("notify-target", true);
        cfg.addDefault("teleport-sound", true);
        cfg.options().copyDefaults(true);
        saveConfig();
    }

    public void reloadConfig() {
        super.reloadConfig();
        saveDefaultConfig();
        cfg = this.getConfig();
        cfg.options().copyDefaults();
        saveConfig();
    }

}
