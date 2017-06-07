package com.gmail.Xeiotos.HabitatSocial.Events;

import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Xeiotos
 */
public class PlayerLeavePartyEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final Player player;
    private final Party party;
    private final Party newParty;

    public PlayerLeavePartyEvent(final Player player, final Party party, final Party newParty) {
        this.player = player;
        this.party = party;
        this.newParty = newParty;
    }

    /**
     * @return the player entering the party
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * @return the party being left
     */
    public Party getParty() {
        return party;
    }
    
    /**
     * @return the party being entered, null if none
     */
    public Party getNewParty() {
        return newParty;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
