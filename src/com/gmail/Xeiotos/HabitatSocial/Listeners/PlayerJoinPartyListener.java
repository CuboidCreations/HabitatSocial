/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.Xeiotos.HabitatSocial.Listeners;

import com.gmail.Xeiotos.HabitatSocial.Events.PlayerJoinPartyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Chris
 */
public class PlayerJoinPartyListener implements Listener {
    
    @EventHandler
    public void onPlayerJoinParty(PlayerJoinPartyEvent event) {
        event.getParty().getPartyGUI().addPlayer(event.getPlayer());
    }
}
