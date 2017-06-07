/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.Xeiotos.HabitatSocial.Listeners;

import com.gmail.Xeiotos.HabitatSocial.Events.PlayerLeavePartyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Chris
 */
public class PlayerLeavePartyListener implements Listener {
    
    @EventHandler
    public void onPlayerLeaveParty(PlayerLeavePartyEvent event) {
        if (event.getParty().getPartyGUI() != null) {
            event.getParty().getPartyGUI().removePlayer(event.getPlayer());
        }
    }
}
