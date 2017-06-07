package com.gmail.Xeiotos.HabitatSocial.Managers;

import com.gmail.Xeiotos.HabitatAPI.Managers.ConfigManager;
import com.gmail.Xeiotos.HabitatSocial.Enumerations.Gender;
import com.gmail.Xeiotos.HabitatSocial.Requests.FriendRequest;
import com.gmail.Xeiotos.HabitatSocial.HabitatSocial;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Xeiotos
 */
public class SocialPlayerManager {

    private static SocialPlayerManager socialPlayerManager;
    private static ArrayList<SocialPlayer> socialPlayers = new ArrayList<>();

    /**
     * Create a new instance of a SocialPlayerManager
     */
    public SocialPlayerManager() {
    }

    /**
     * Get the SocialPlayerManager instance
     *
     * @return SocialPlayerManager instance
     */
    public static SocialPlayerManager getManager() {
        if (socialPlayerManager == null) {
            socialPlayerManager = new SocialPlayerManager();
        }

        return socialPlayerManager; // NOT THREAD SAFE!
    }

    /**
     * Remove a player from the player list by SocialPlayer
     *
     * @param socialPlayer Habitatplayer to remove
     */
    public void removePlayer(SocialPlayer socialPlayer) {
        socialPlayers.remove(socialPlayer);
    }

    /**
     * Add a player to the list by player object
     *
     * @param player Player to add
     */
    public void addPlayer(Player player) {
        socialPlayers.add(loadPlayerData(player.getName()));
    }

    /**
     * Remove a player from the player list by playername
     *
     * @param playerName Habitatplayer to remove
     */
    public void removePlayer(String playerName) {
        socialPlayers.remove(getSocialPlayer(playerName));
    }

    /**
     * Remove a player from the player list by player
     *
     * @param player Habitatplayer to remove
     */
    public void removePlayer(Player player) {
        socialPlayers.remove(getSocialPlayer(player));
    }

    /**
     * Get a SocialPlayer by it's player object
     *
     * @param p Player to lookup
     * @return SocialPlayer from the list, null if not found
     */
    public SocialPlayer getSocialPlayer(Player p) {
        return getSocialPlayer(p.getName());
    }

    /**
     * Get a SocialPlayer by it's name
     *
     * @param name Player name to lookup
     * @return SocialPlayer from the list, null if not found
     */
    public SocialPlayer getSocialPlayer(String name) {
        if (name != null) {
            for (SocialPlayer p : socialPlayers) {
                if (p.getName().equalsIgnoreCase(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Save all player data
     */
    public void saveSocialPlayers() {
        for (SocialPlayer socialPlayer : socialPlayers) {
            if (socialPlayer == null) {
                continue;
            }

            savePlayerData(socialPlayer, null);
        }
    }

    /**
     * Get all SocialPlayers
     *
     * @return List of all SocialPlayers, null if none.
     */
    public ArrayList<SocialPlayer> getSocialPlayers() {
        return socialPlayers;
    }

    /**
     * Load SocialPlayer into memory
     *
     * @param playerName Socialplayer to load into memory
     */
    public SocialPlayer loadPlayerData(String playerName) {

        FileConfiguration configFile = safeLoadPlayerToMemory(playerName);

        SocialPlayer socialPlayer;
        
        if (!configFile.getBoolean("registered")) {
            socialPlayer = new SocialPlayer(playerName, null, null, null, null, configFile.getStringList("friends"), null, false);
            return socialPlayer;
        }

        socialPlayer = new SocialPlayer(playerName,
                configFile.getString("firstname"),
                configFile.getString("lastname"),
                configFile.getString("birthday"),
                Gender.valueOf(configFile.getString("gender").toUpperCase()),
                configFile.getStringList("friends"),
                configFile.getString("email"),
                configFile.getBoolean("registered"));

        HabitatSocial.getInstance().getConfigManager().unloadConfig(playerName);
        
        for (String requestingPlayer : configFile.getStringList("friendRequests")) {
            socialPlayer.sendRequest(new FriendRequest(playerName, requestingPlayer));
        }

        return socialPlayer;
    }
    
    /**
     * Load player fileconfig to memory
     *
     * @param playerName Player file to load into memory
     * @return FileConfiguration of player, null if not existing
     */
    public FileConfiguration safeLoadPlayerToMemory(String playerName) {
        ConfigManager configManager = HabitatSocial.getInstance().getConfigManager();
        configManager.loadConfigFiles(
                new ConfigManager.ConfigPath(playerName, "Player.yml", "players/" + playerName + ".yml"));

        FileConfiguration configFile = configManager.getFileConfig(playerName);
        
        return configFile;
    }

    /**
     * Save SocialPlayer to disk
     *
     * @param playerName Socialplayer to save to disk
     */
    public void savePlayerData(Player player) {
        savePlayerData(getSocialPlayer(player), null);
    }

    /**
     * Save SocialPlayer to disk
     *
     * @param playerName Socialplayer to save to disk
     */
    public void savePlayerData(SocialPlayer socialPlayer, String[] args) {

        if (socialPlayer == null) {
            return;
        }

        String playerName = socialPlayer.getName();

        FileConfiguration configFile = safeLoadPlayerToMemory(playerName);

        configFile.set("friends", socialPlayer.getFriends());
        
        for (FriendRequest friendRequest : socialPlayer.getFriendRequests()) {
            List<String> friendRequests = configFile.getStringList("friendRequests");
            if (!friendRequests.contains(friendRequest.getRequestingPlayer())) {
                friendRequests.add(friendRequest.getRequestingPlayer());
            }
        }

        if (args != null) {
            if (!configFile.getBoolean("registered")) {
                configFile.set("firstname", args[0]);
                configFile.set("lastname", args[1]);
                configFile.set("birthday", args[2]);
                configFile.set("gender", args[3]);
                configFile.set("email", args[4]);
                configFile.set("registered", true);
                socialPlayer.setRegistered(true);
            }
        }

        ConfigManager configManager = HabitatSocial.getInstance().getConfigManager();
        configManager.saveConfig(playerName);
        configManager.unloadConfig(playerName);

        refreshPlayer(socialPlayer.getPlayer());
    }

    public void refreshPlayer(Player player) {
        removePlayer(player);
        addPlayer(player);
    }
}
