/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.Xeiotos.HabitatSocial.Managers;

import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class PartyManager {
    
    private static PartyManager partyManager;
    private static ArrayList<Party> parties = new ArrayList<>();

    /**
     * Create a new instance of a PartyManager
     */
    public PartyManager() {
    }
    
    /**
     * Get the PartyManager instance
     *
     * @return PartyManager instance
     */
    public static PartyManager getManager() {
        if (partyManager == null) {
            partyManager = new PartyManager();
        }

        return partyManager; // NOT THREAD SAFE!
    }
    
    /**
     * Create a new party
     *
     * @param partyMaster The master of the party
     * @param members The members of the party
     */
    public void createParty(SocialPlayer partyMaster, SocialPlayer... members) {
        parties.add(new Party(partyMaster, members));
    }
    
    /**
     * Delete a party
     *
     * @param party The party to delete
     */
    public void removeParty(Party party) {
        if (parties.contains(party)) {
            parties.remove(party);
        }
    }
    
    /**
     * Get a party by it's master
     *
     * @param name Player name to lookup
     * @return Party from the list, null if not found
     */
    public Party getParty(String name) {
        if (name != null) {
            for (Party p : parties) {
                if (p.getMaster().getName().equalsIgnoreCase(name)) {
                    return p;
                }
            }
        }
        return null;
    }
}
