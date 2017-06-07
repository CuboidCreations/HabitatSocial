package com.gmail.Xeiotos.HabitatSocial;

import com.gmail.Xeiotos.HabitatSocial.Util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Xeiotos
 */
public class Registration {

    private SocialPlayer socialPlayer;
    private Player player;
    private String birthDay;
    private String gender;
    private String firstName;
    private String lastName;
    private String email;

    public Registration(SocialPlayer socialPlayer, String firstName, String lastName, String birthDay, String gender, String email) {
        this.socialPlayer = socialPlayer;
        this.player = socialPlayer.getPlayer();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.gender = gender;
        this.email = email;
    }

    public boolean validateRegistration() {
        boolean hasErrors = false;
        
        if (socialPlayer.isRegistered()) {
            player.sendMessage(ChatColor.RED + "You have already registered!");
            player.sendMessage(ChatColor.RED + "Please contact a moderator to change your personal info");
            return false;
        }

        if (firstName.matches(".*\\d.*") || lastName.matches(".*\\d.*")) {
            player.sendMessage(ChatColor.RED + "That's not your name, is it?");
            hasErrors = true;
        }
        String[] split = birthDay.split("/");
        if (split.length != 3 || split[0].length() != 2 || split[1].length() != 2 || split[2].length() != 4) {
            player.sendMessage(ChatColor.RED + "You birthday must be formatted like this: DD/MM/YYYY (e.g. 06/05/1996 for the 6th of May 1996)");
            hasErrors = true;
        }

        if (!Util.isInteger(split[0]) || !Util.isInteger(split[1]) || !Util.isInteger(split[2])) {
            player.sendMessage(ChatColor.RED + "Your birthday may not contain letters.");
            hasErrors = true;
        }
        if (!"Male".equalsIgnoreCase(gender) && !"Female".equalsIgnoreCase(gender) && !"Private".equalsIgnoreCase(gender)) {
            player.sendMessage(ChatColor.RED + "Your gender may only be male, female or private.");
            hasErrors = true;
        }
        if (!email.contains("@")) { //LOL! Worst e-mail validation ever. Yes. I could do this with regexes, but is it worth it?
            player.sendMessage(ChatColor.RED + "Invalid e-mail!");
            hasErrors = true;
        }
        
        if (hasErrors) {
            return false;
        }
        
        return true;
    }
}
