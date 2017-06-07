package com.gmail.Xeiotos.HabitatSocial.Party;

import com.gmail.Xeiotos.HabitatSocial.Enumerations.DropRule;
import com.gmail.Xeiotos.HabitatSocial.Events.PlayerJoinPartyEvent;
import com.gmail.Xeiotos.HabitatSocial.Events.PlayerLeavePartyEvent;
import com.gmail.Xeiotos.HabitatSocial.GUI.PartyGUI;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;

/**
 *
 * @author Xeiotos
 */
public class Party {

    private ArrayList<SocialPlayer> members;
    private SocialPlayer master;
    private boolean friendlyFire = false;
    private PartyGUI partyGUI;
    private DropRule dropRule;

    public Party(SocialPlayer master, SocialPlayer... members) {
        this.master = master;
        this.members = new ArrayList<>(Arrays.asList(members));
        master.setParty(this);
        for (SocialPlayer socialPlayer : members) {
            socialPlayer.setParty(this);
        }
        this.partyGUI = new PartyGUI(this);
    }

    /**
     * Get the party's members
     *
     * @return The party's members
     */
    public List<SocialPlayer> getMembers() {
        return members;
    }

    /**
     * Get the party's master
     *
     * @return The party's master
     */
    public SocialPlayer getMaster() {
        return master;
    }

    /**
     * Get the party's GUI
     *
     * @return The party's GUI
     */
    public PartyGUI getPartyGUI() {
        return partyGUI;
    }

    /**
     * Set the party's GUI
     *
     * @param The party's GUI to set
     */
    public void setPartyGUI(PartyGUI partyGUI) {
        this.partyGUI = partyGUI;
    }

    /**
     * Get the party's drop rule
     *
     * @return This party's droprule
     */
    public DropRule getDropRule() {
        return dropRule;
    }
    
    /**
     * Set the party's drop rule
     *
     * @param dropRule droprule to use
     */
    public void setDropRule(DropRule dropRule) {
        this.dropRule = dropRule;
    }

    /**
     * Disband the party
     *
     */
    public void disband() {
        kickAll();

        partyGUI = null;
    }

    /**
     * Teleport all members to the party master
     *
     */
    public void tpAll() {
        for (SocialPlayer socialPlayer : members) {
            socialPlayer.getPlayer().teleport(master.getPlayer());
        }
    }

    /**
     * Send a message to all party members
     *
     * @param message Message to send to party members
     */
    public void broadcast(String message) {
        for (SocialPlayer socialPlayer : members) {
            socialPlayer.getPlayer().sendMessage(message);
        }
        master.getPlayer().sendMessage(message);
    }

    /**
     * Kick a party member
     *
     * @param socialPlayer socialPlayer to kick
     */
    public void kick(SocialPlayer socialPlayer) {
        members.remove(socialPlayer);
        socialPlayer.setParty(null);
        Bukkit.getServer().getPluginManager().callEvent(new PlayerLeavePartyEvent(socialPlayer.getPlayer(), this, null));
    }

    public void kickAll() {
        for (SocialPlayer member : members) {
            member.setParty(null);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerLeavePartyEvent(member.getPlayer(), this, null));
        }
        members.removeAll(members);
        kick(master);
    }

    /**
     * Add a party member
     *
     * @param socialPlayer socialPlayer to add
     */
    public void add(SocialPlayer socialPlayer) {
        if (!members.contains(socialPlayer)) {
            members.add(socialPlayer);
            socialPlayer.setParty(this);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinPartyEvent(socialPlayer.getPlayer(), this, null));
        }
    }

    /**
     * Set whether or not friendly fire should be allowed
     *
     * @param bool whether or not friendly fire should be allowed
     */
    public void setFriendlyFire(boolean bool) {
        friendlyFire = bool;
    }

    /**
     * Get wheter or not friendly fire is allowed
     *
     * @return True if yes, false if not
     */
    public boolean getFriendlyFire() {
        return friendlyFire;
    }
}
