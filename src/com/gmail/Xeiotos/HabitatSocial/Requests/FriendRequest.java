package com.gmail.Xeiotos.HabitatSocial.Requests;

import com.gmail.Xeiotos.HabitatSocial.HabitatSocial;
import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Xeiotos
 */
public class FriendRequest extends Request {

    public FriendRequest(String requestedPlayer, String requestingPlayer) {
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
            requestingSocialPlayer.addFriend(requestedPlayer);
            requestingSocialPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You are now friends with " + requestedPlayer + "!");
        } else {
            FileConfiguration fileConfiguration = SocialPlayerManager.getManager().safeLoadPlayerToMemory(requestingPlayer);
            fileConfiguration.getStringList("friends").add(requestedPlayer);
            HabitatSocial.getInstance().getConfigManager().unloadConfig(requestingPlayer);
        }
        
        if (requestedSocialPlayer != null) {
            requestedSocialPlayer.addFriend(requestingPlayer);
            requestedSocialPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You are now friends with " + requestingPlayer + "!");
            requestedSocialPlayer.removeFriendRequest(this);
        } else {
            FileConfiguration fileConfiguration = SocialPlayerManager.getManager().safeLoadPlayerToMemory(requestedPlayer);
            fileConfiguration.getStringList("friends").add(requestingPlayer);
            HabitatSocial.getInstance().getConfigManager().unloadConfig(requestedPlayer);
        }
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
            requestedSocialPlayer.removeFriendRequest(this);
            requestedSocialPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have declined " + requestingPlayer + "'s friend request!");
            if (requestingSocialPlayer != null) {
                requestingSocialPlayer.getPlayer().sendMessage(ChatColor.GOLD + requestedPlayer + " has declined your friend request!");
            }
        }
    }
}
