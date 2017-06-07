/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.Xeiotos.HabitatSocial.Listeners;

import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Chris
 */
public class PlayerPickupItemListener implements Listener{
    
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        SocialPlayer socialPlayer = SocialPlayerManager.getManager().getSocialPlayer(player);
        
        if (socialPlayer.isInParty()) {
            Party party = socialPlayer.getParty();
            Item item = event.getItem();
            ItemStack itemStack = item.getItemStack();
            
            switch(party.getDropRule()) {
                case EQUAL:
                    int numMembers = party.getMembers().size() + 1;
                    int remainder = itemStack.getAmount() % numMembers;
                    
                    itemStack.setAmount((itemStack.getAmount() / numMembers));
                    
                    for (SocialPlayer partyMember : party.getMembers()) {
                        partyMember.getPlayer().getInventory().addItem(itemStack);
                    }
                    party.getMaster().getPlayer().getInventory().addItem(itemStack);
                    player.getInventory().remove(itemStack);
                    break;
                case OWN:
                    break;
                case RANDOM:
                    break;
                default:
                    break;
            }
        }
    }
}
