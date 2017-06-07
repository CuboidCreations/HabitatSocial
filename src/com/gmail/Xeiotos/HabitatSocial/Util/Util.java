package com.gmail.Xeiotos.HabitatSocial.Util;

import com.gmail.Xeiotos.HabitatAPI.HabitatPlayer;
import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.Requests.PartyRequest;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Xeiotos
 */
public class Util {

    /**
     * Get if a string is an integer
     *
     * @return True if string is an integer, false if not.
     * @param s The string to check
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static StringBuilder formatNameByHabitat(SocialPlayer socialPlayer) {
        StringBuilder stringBuilder = new StringBuilder();
        Player player = socialPlayer.getPlayer();

        if (player.getWorld() != Bukkit.getWorld("world")) {
            stringBuilder.append(ChatColor.RESET).append(player.getName()).append(" ").append(ChatColor.GOLD).append(" @ ").append(ChatColor.AQUA).append("Otherworld ").append("\n");
            return stringBuilder;
        }

        HabitatPlayer habitatPlayer = socialPlayer.getHabitatPlayer();

        if (socialPlayer.getHabitatPlayer().getHabitat() == null) {
            stringBuilder.append(ChatColor.RESET).append(player.getName()).append(" ").append(ChatColor.GOLD).append(" @ ").append(ChatColor.AQUA).append("Overworld ").append("\n");
            return stringBuilder;
        }

        stringBuilder.append(ChatColor.RESET).append(player.getName()).append(ChatColor.GOLD).append(" @ ").append(ChatColor.AQUA).append(habitatPlayer.getHabitat().getTypeName()).append(" ").append(habitatPlayer.getRelativeLocation().x).append(",").append(habitatPlayer.getRelativeLocation().y).append("\n");
        return stringBuilder;
    }

    public static boolean validateCommandLength(String commandName, String[] args, int validLength, Player player) {
        if (args.length < validLength) {
            player.sendMessage(ChatColor.RED + "Not enough arguments! Use /" + commandName + " for help!");
            return false;
        } else if (args.length == validLength) {
            return true;

        } else {
            player.sendMessage(ChatColor.RED + "Too many arguments! Use /" + commandName + " for help!");
            return false;
        }
    }
}
