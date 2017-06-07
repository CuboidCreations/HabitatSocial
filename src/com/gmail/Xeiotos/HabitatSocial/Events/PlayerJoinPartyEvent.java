package com.gmail.Xeiotos.HabitatSocial.Events;

import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Xeiotos
 */
public class PlayerJoinPartyEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final Player player;
    private final Party party;
    private final Party oldParty;

    public PlayerJoinPartyEvent(final Player player, final Party party, final Party oldParty) {
        this.player = player;
        this.party = party;
        this.oldParty = oldParty;
    }

    /**
     * @return the player entering the party
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * @return the party being entered
     */
    public Party getParty() {
        return party;
    }
    
    /**
     * @return the party being left, null if none
     */
    public Party getOldParty() {
        return oldParty;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
