package com.gmail.Xeiotos.HabitatSocial.Listeners;

import com.gmail.Xeiotos.HabitatAPI.HabitatAPI;
import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author Xeiotos
 */
public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(final PlayerLoginEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(HabitatAPI.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                SocialPlayerManager.getManager().addPlayer(player);
                if (!player.hasPlayedBefore()) {
                    player.sendMessage(ChatColor.GOLD + "Welcome to AtmoSphere!");
                } else {
                    player.sendMessage(ChatColor.GOLD + "Welcome back to AtmoSphere!");
                }
            }
        });
    }
}
