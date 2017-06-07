package com.gmail.Xeiotos.HabitatSocial;

import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import com.gmail.Xeiotos.HabitatSocial.Requests.PartyRequest;
import com.gmail.Xeiotos.HabitatSocial.Requests.Request;
import com.gmail.Xeiotos.HabitatSocial.Requests.FriendRequest;
import com.gmail.Xeiotos.HabitatAPI.HabitatPlayer;
import com.gmail.Xeiotos.HabitatAPI.Managers.HabitatPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.Enumerations.Gender;
import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Xeiotos
 */
public class SocialPlayer {

    private String playerName = null;
    private Player player;
    private List<String> friends = new LinkedList<>();
    private Party party;
    private ArrayList<FriendRequest> friendRequests = new ArrayList<>();
    private PartyRequest partyRequest;
    private String birthDay;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String email;
    private boolean registered;

    /**
     * Create a HabitatPlayer
     *
     * @param playerName Player's name
     */
    public SocialPlayer(String playerName, String firstName, String lastName, String birthDay, Gender gender, List<String> friends, String email, boolean registered) {
        this.playerName = playerName;
        this.player = Bukkit.getPlayer(playerName);
        this.registered = registered;
        this.friends = friends;
        
        if (registered) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.birthDay = birthDay;
            this.email = email;
        } else {
            player.sendMessage(ChatColor.AQUA + "Please consider using /register to register your name, birthday and gender :)");
        }
    }

    /**
     * Get the player's name
     *
     * @return SocialPlayer's Player name
     */
    public String getName() {
        return playerName;
    }

    /**
     * Get the socialPlayer's player object
     *
     * @return SocialPlayer's player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get if the SocialPlayer is in a party
     *
     * @return true if yes, false if not
     */
    public boolean isInParty() {
        if (party != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the player's friends
     *
     * @return SocialPlayer's friends, null if none
     */
    public List<String> getFriends() {
        return friends;
    }

    /**
     * Get a specific friend request by requesting player
     *
     * @param requestingPlayer requesting player
     * @return Friendrequest of requestingPlayer, null if not found
     */
    public FriendRequest getFriendRequest(String requestingPlayer) {
        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getRequestingPlayer().equalsIgnoreCase(requestingPlayer)) {
                return friendRequest;
            }
        }
        return null;
    }
    
    /**
     * Get the current party request
     *
     * @return PartyRequest of this player, null if none
     */
    public PartyRequest getPartyRequest() {
        return partyRequest;
    }

    /**
     * Remove a specific friend request
     *
     * @param friendRequest friendRequest to remove
     */
    public void removeFriendRequest(FriendRequest friendRequest) {
        if (friendRequests.contains(friendRequest)) {
            friendRequests.remove(friendRequest);
        }
    }

    /**
     * Send the player a party request
     *
     */
    public void sendRequest(Request request) {
        if (request instanceof FriendRequest) {
            this.friendRequests.add((FriendRequest) request);
        } else if (request instanceof PartyRequest) {
            this.partyRequest = (PartyRequest) request;
        }
    }

    /**
     * Send the player a party request
     *
     */
    public void removeRequest(Request request) {
        if (request instanceof FriendRequest) {
            removeFriendRequest((FriendRequest) request);
        } else if (request instanceof PartyRequest) {
            this.partyRequest = null;
        }
    }

    /**
     * Get the player's party
     *
     * @return SocialPlayer's party, null if none
     */
    public Party getParty() {
        return party;
    }
    
    /**
     * Set the player's party
     *
     * @param party Party to add player to
     */
    public void setParty(Party party) {
        this.party = party;
    }

    /**
     * Get the player's friend requests
     *
     * @return SocialPlayer's friendRequests, null if none
     */
    public List<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    /**
     * Get the player's online friends
     *
     * @return SocialPlayer's online friends, empty list if none
     */
    public List<SocialPlayer> getOnlineFriends() {
        List<SocialPlayer> onlineFriends = new LinkedList<>();
        for (String friend : friends) {
            SocialPlayer socialFriend = SocialPlayerManager.getManager().getSocialPlayer(friend);
            if (socialFriend != null) {
                onlineFriends.add(socialFriend);
            }
        }
        return onlineFriends;
    }

    /**
     * Add a player to the player's friend list
     *
     * @param playerName Player to add
     */
    public void addFriend(SocialPlayer socialPlayer) {
        addFriend(socialPlayer.getName());
    }

    /**
     * Add a player to the player's friend list
     *
     * @param playerName Player to add
     */
    public void addFriend(String playerName) {
        if (!isFriendsWith(playerName)) {
            friends.add(playerName);
        }
    }

    /**
     * Remove a player from the player's friend list
     *
     * @param playerName Player to remove
     */
    public void removeFriend(String playerName) {
        if (isFriendsWith(playerName)) {
            friends.remove(playerName);
        }
    }

    /**
     * Check if a player is friends with another player
     *
     * @return True if friends, false if not
     * @param playerName Player to check
     */
    public boolean isFriendsWith(String playerName) {
        if (friends == null) {
            return false;
        }
        
        if (friends.contains(playerName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if a player is registered
     *
     * @return True if registered, false if not
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * Get the player's birthday
     *
     * @return SocialPlayer's birthday, may be null
     */
    public String getBirthDay() {
        return birthDay;
    }

    /**
     * Get the player's first name
     *
     * @return SocialPlayer's first name, may be null
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the player's last name
     *
     * @return SocialPlayer's last name, may be null
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the player's full name
     *
     * @return SocialPlayer's full name, may be null
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Get the player's gender
     *
     * @return SocialPlayer's gender, may be null
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Set if a player is registered
     *
     * @param bool If player is registered
     */
    public void setRegistered(boolean bool) {
        this.registered = bool;
    }

    /**
     * Get the player's HabitatPlayer
     *
     * @return SocialPlayer's HabitatPlayer
     */
    public HabitatPlayer getHabitatPlayer() {
        return HabitatPlayerManager.getManager().getHabitatPlayer(playerName);
    }
}
