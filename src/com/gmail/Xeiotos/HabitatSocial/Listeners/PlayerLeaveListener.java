package com.gmail.Xeiotos.HabitatSocial.Listeners;

import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Xeiotos
 */
public class PlayerLeaveListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        SocialPlayer socialPlayer = SocialPlayerManager.getManager().getSocialPlayer(event.getPlayer());
        if (socialPlayer.isInParty()) {
            Party party = socialPlayer.getParty();
            if (party.getMaster().equals(socialPlayer)) {
                party.broadcast(ChatColor.RED + "Party disbanding because party master is leaving");
                party.disband();
            } else {
                party.broadcast(ChatColor.RED + socialPlayer.getName() + " has left the party");
                party.kick(socialPlayer);
            }
        }
        SocialPlayerManager.getManager().savePlayerData(event.getPlayer());
        SocialPlayerManager.getManager().removePlayer(event.getPlayer().getName());
    }
}
