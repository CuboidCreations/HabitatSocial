package com.gmail.Xeiotos.HabitatSocial.Requests;

import com.gmail.Xeiotos.HabitatSocial.Managers.PartyManager;
import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Xeiotos
 */
public class PartyRequest extends Request {
    
    public PartyRequest(String requestedPlayer, String requestingPlayer) {
        super(requestedPlayer, requestingPlayer);
    }
    
    /**
     * Accept the request
     *
     */
    @Override
    public void accept() {
        this.accepted = true;
        
        SocialPlayerManager socialPlayerManager = SocialPlayerManager.getManager();
        
        SocialPlayer requestingSocialPlayer = socialPlayerManager.getSocialPlayer(requestingPlayer);
        SocialPlayer requestedSocialPlayer = socialPlayerManager.getSocialPlayer(requestedPlayer);
        
        if (requestingSocialPlayer != null) {
            if (requestedSocialPlayer.isInParty()) {
                requestedSocialPlayer.getParty().broadcast(ChatColor.RED + requestingPlayer + " has left the party");
                requestedSocialPlayer.getParty().kick(requestedSocialPlayer);
            }

            if (requestingSocialPlayer.isInParty()) {
                requestingSocialPlayer.getParty().add(requestedSocialPlayer);
            } else {
                PartyManager.getManager().createParty(requestingSocialPlayer, requestedSocialPlayer);
            }
            
            requestedSocialPlayer.getParty().broadcast(ChatColor.GOLD + requestedPlayer + " has joined the party");
        } else {
            requestedSocialPlayer.getPlayer().sendMessage(ChatColor.RED + requestingPlayer + " is offline! Party request cancelled.");
        }
        
        requestedSocialPlayer.removeRequest(this);
    }
    
    /**
     * Decline the request
     *
     */
    @Override
    public void decline() {
        this.accepted = false;
        
        SocialPlayerManager socialPlayerManager = SocialPlayerManager.getManager();
        
        SocialPlayer requestingSocialPlayer = socialPlayerManager.getSocialPlayer(requestingPlayer);
        SocialPlayer requestedSocialPlayer = socialPlayerManager.getSocialPlayer(requestedPlayer);
        
        if (requestedSocialPlayer != null) {
            requestedSocialPlayer.removeRequest(this);
            requestedSocialPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have declined " + requestingPlayer + "'s party request!");
            if (requestingSocialPlayer != null) {
                requestingSocialPlayer.getPlayer().sendMessage(ChatColor.GOLD + requestedPlayer + " has declined your party request!");
            }
        }
    }
}
