package com.connorlinfoot.protocolmanagement;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin implements Listener {

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();

        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
        console.sendMessage(ChatColor.AQUA + "ProtocolManagement");
        console.sendMessage(ChatColor.AQUA + "Version " + getDescription().getVersion());
        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");

        Bukkit.getPluginManager().registerEvents(this,this);
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        List<?> blockedProtocols = getConfig().getList("Protocols");
        Integer playerProtocol = craftPlayer.getHandle().playerConnection.networkManager.getVersion();
        if( !getConfig().getBoolean("Whitelist") ) {
            for (Object o : blockedProtocols) {
                if (o == playerProtocol) {
                    player.kickPlayer(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Message")));
                    break;
                }
            }
        } else {
            boolean allow = false;
            for (Object o : blockedProtocols) {
                if (o == playerProtocol) {
                    allow = true;
                    break;
                }
            }
            if( !allow ) player.kickPlayer(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Message")));
        }
    }
}