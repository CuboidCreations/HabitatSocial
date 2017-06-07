package com.gmail.Xeiotos.HabitatSocial;

import com.gmail.Xeiotos.HabitatSocial.Requests.PartyRequest;
import com.gmail.Xeiotos.HabitatSocial.Requests.FriendRequest;
import com.gmail.Xeiotos.HabitatAPI.HabitatAPI;
import com.gmail.Xeiotos.HabitatAPI.Managers.ConfigManager;
import com.gmail.Xeiotos.HabitatSocial.Enumerations.DropRule;
import com.gmail.Xeiotos.HabitatSocial.Listeners.PlayerJoinListener;
import com.gmail.Xeiotos.HabitatSocial.Listeners.PlayerJoinPartyListener;
import com.gmail.Xeiotos.HabitatSocial.Listeners.PlayerLeaveListener;
import com.gmail.Xeiotos.HabitatSocial.Listeners.PlayerLeavePartyListener;
import com.gmail.Xeiotos.HabitatSocial.Listeners.PlayerPickupItemListener;
import com.gmail.Xeiotos.HabitatSocial.Managers.MailManager;
import com.gmail.Xeiotos.HabitatSocial.Managers.SocialPlayerManager;
import com.gmail.Xeiotos.HabitatSocial.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Chris
 */
public class HabitatSocial extends JavaPlugin {

    private static HabitatSocial instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);

        getHabitatAPI();

        for (Player p : Bukkit.getOnlinePlayers()) {
            SocialPlayerManager.getManager().addPlayer(p);
        }

        registerEvents();
    }

    @Override
    public void onDisable() {
        SocialPlayerManager.getManager().saveSocialPlayers();
    }

    public static HabitatSocial getInstance() {
        return instance;
    }

    public HabitatAPI getHabitatAPI() {
        return HabitatAPI.getHook(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            SocialPlayer socialPlayer = SocialPlayerManager.getManager().getSocialPlayer(player);
            switch (cmd.getName().toLowerCase()) {
                case "register":
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.GOLD + "Type /register <FirstName> <LastName> <Birthday:DD/MM/YYYY> <Male/Female/Private> <e-mail>");
                        player.sendMessage(ChatColor.AQUA + "Your personal info will never be shared with anyone.");
                    } else if (args.length == 5) {
                        Registration registration = new Registration(socialPlayer, args[0], args[1], args[2], args[3], args[4]);
                        if (registration.validateRegistration()) {
                            SocialPlayerManager.getManager().savePlayerData(socialPlayer, args);
                            MailManager.getManager().logMail(args[0] + " " + args[1], player.getName(), args[4]);
                            player.sendMessage(ChatColor.GREEN + "You have succesfully registered!");
                        }

                    }
                    break;
                case "friend":
                    if (args.length == 0) {
                        sender.sendMessage(ChatColor.GOLD + "HabitatFriends - " + ChatColor.ITALIC + ChatColor.AQUA + "Available commands:");
                        sender.sendMessage(ChatColor.GOLD + "List - " + ChatColor.ITALIC + ChatColor.AQUA + "View a list of your online friends");
                        sender.sendMessage(ChatColor.GOLD + "Add <Playername> - " + ChatColor.ITALIC + ChatColor.AQUA + "Add a friend");
                        sender.sendMessage(ChatColor.GOLD + "Remove <Playername> - " + ChatColor.ITALIC + ChatColor.AQUA + "Remove a friend");
                        sender.sendMessage(ChatColor.GOLD + "Requests - " + ChatColor.ITALIC + ChatColor.AQUA + "View a list of your friendrequests");
                        sender.sendMessage(ChatColor.GOLD + "Accept <Playername> - " + ChatColor.ITALIC + ChatColor.AQUA + "Accept a friendrequest");
                        sender.sendMessage(ChatColor.GOLD + "Decline <Playername> - " + ChatColor.ITALIC + ChatColor.AQUA + "Decline a friendrequest");
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("list")) {
                            StringBuilder stringBuilder = new StringBuilder();

                            if (socialPlayer.getOnlineFriends() != null) {
                                for (SocialPlayer friend : socialPlayer.getOnlineFriends()) {
                                    stringBuilder.append(Util.formatNameByHabitat(friend));
                                }
                            } else {
                                player.sendMessage(ChatColor.GRAY + "You do not have any online friends :(");
                            }

                            if (stringBuilder.toString().length() != 0) {
                                player.sendMessage(stringBuilder.toString());
                            } else {
                                player.sendMessage(ChatColor.GRAY + "You do not have any online friends :(");
                            }

                        } else if (args[0].equalsIgnoreCase("requests")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            int i = 0;
                            for (FriendRequest friendRequest : socialPlayer.getFriendRequests()) {
                                i++;
                                if (i > 1) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(friendRequest.getRequestingPlayer());
                            }
                            if (stringBuilder.toString().length() != 0) {
                                player.sendMessage(stringBuilder.toString());
                            } else {
                                player.sendMessage(ChatColor.GRAY + "You do not have any friend requests");
                            }
                        }
                    } else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("add")) {
                            SocialPlayer playerToAdd = SocialPlayerManager.getManager().getSocialPlayer(args[1]);
                            if (playerToAdd != null) {
                                if (args[1].equals(socialPlayer.getName())) {
                                    player.sendMessage(ChatColor.RED + "Easy there, Narcissus, you can't friend yourself!");
                                    return true;
                                }

                                if (socialPlayer.isFriendsWith(args[1])) {
                                    player.sendMessage(ChatColor.RED + "You're already friends with that player!");
                                } else {
                                    playerToAdd.sendRequest(new FriendRequest(playerToAdd.getName(), socialPlayer.getName()));
                                    playerToAdd.getPlayer().sendMessage(ChatColor.GOLD + socialPlayer.getName() + " wants to be friends with you!");
                                    player.sendMessage(ChatColor.GOLD + "You have requested " + playerToAdd.getName() + " to be friends!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "That player doesn't exist or isn't online!");
                            }
                        } else if (args[0].equalsIgnoreCase("remove")) {
                            SocialPlayer playerToRemove = SocialPlayerManager.getManager().getSocialPlayer(args[1]);
                            if (playerToRemove != null) {
                                if (socialPlayer.isFriendsWith(args[1])) {
                                    socialPlayer.removeFriend(args[1]);
                                    playerToRemove.removeFriend(socialPlayer.getName());
                                    player.sendMessage(ChatColor.GOLD + "You have removed " + args[1] + " from your friend list!");
                                    playerToRemove.getPlayer().sendMessage(ChatColor.GOLD + "You are no longer friends with " + socialPlayer.getName() + "!");
                                } else {
                                    player.sendMessage(ChatColor.RED + "You aren't friends with that player!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "That player doesn't exist or isn't online!");
                            }
                        } else if (args[0].equalsIgnoreCase("accept")) {
                            FriendRequest friendRequest = socialPlayer.getFriendRequest(args[1]);

                            if (friendRequest != null) {
                                friendRequest.accept();
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have a friend request from that player!");
                            }

                        } else if (args[0].equalsIgnoreCase("decline")) {
                            FriendRequest friendRequest = socialPlayer.getFriendRequest(args[1]);

                            if (friendRequest != null) {
                                friendRequest.decline();
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have a friend request from that player!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid command! Run /friend for help.");
                        }
                    }
                    break;
                case "party":
                    if (args.length == 0) {
                        sender.sendMessage(ChatColor.GOLD + "HabitatParty - " + ChatColor.ITALIC + ChatColor.AQUA + "Available commands:");
                        sender.sendMessage(ChatColor.GOLD + "Invite <Playername> - " + ChatColor.ITALIC + ChatColor.AQUA + "Invite someone to your party");
                        sender.sendMessage(ChatColor.GOLD + "List - " + ChatColor.ITALIC + ChatColor.AQUA + "Lists the players in your party");
                        sender.sendMessage(ChatColor.GOLD + "Accept - " + ChatColor.ITALIC + ChatColor.AQUA + "Accept a party request");
                        sender.sendMessage(ChatColor.GOLD + "Decline - " + ChatColor.ITALIC + ChatColor.AQUA + "Decline a party request");
                        if (socialPlayer.isInParty()) {
                            sender.sendMessage(ChatColor.GOLD + "Leave - " + ChatColor.ITALIC + ChatColor.AQUA + "Leave your current party");
                            if (socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                sender.sendMessage(ChatColor.GOLD + "Kick <Playername> - " + ChatColor.ITALIC + ChatColor.AQUA + "Kick a player from your party");
                                sender.sendMessage(ChatColor.GOLD + "Droprule - " + ChatColor.ITALIC + ChatColor.AQUA + "Set drop rules");
                                sender.sendMessage(ChatColor.GOLD + "TPAll - " + ChatColor.ITALIC + ChatColor.AQUA + "Teleport all party members to your location");
                                sender.sendMessage(ChatColor.GOLD + "Disband - " + ChatColor.ITALIC + ChatColor.AQUA + "Disband the party");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("invite")) {
                        if (Util.validateCommandLength(cmd.getName(), args, 2, player)) {
                            SocialPlayer playerToAdd = SocialPlayerManager.getManager().getSocialPlayer(args[1]);
                            if (playerToAdd == null) {
                                player.sendMessage(ChatColor.RED + "That player doesn't exist or isn't online!");
                                return true;
                            }

                            if (socialPlayer.isInParty()) {
                                if (!socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                    sender.sendMessage(ChatColor.RED + "Only the party master may use this command");
                                    return true;
                                }
                            }

                            player.sendMessage(ChatColor.GOLD + "You have requested " + playerToAdd.getName() + " to join your party!");
                            playerToAdd.getPlayer().sendMessage(ChatColor.GOLD + socialPlayer.getName() + " wants to party with you!");
                            playerToAdd.sendRequest(new PartyRequest(args[1], player.getName()));
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (socialPlayer.isInParty()) {
                            if (socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                socialPlayer.getParty().broadcast(ChatColor.RED + "Party disbanding because party master is leaving");
                                socialPlayer.getParty().disband();
                            } else {
                                socialPlayer.getParty().broadcast(ChatColor.RED + socialPlayer.getName() + " has left the party");
                                socialPlayer.getParty().kick(socialPlayer);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("kick")) {
                        if (Util.validateCommandLength(cmd.getName(), args, 2, player)) {
                            if (socialPlayer.isInParty()) {
                                if (socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                    SocialPlayer playerToKick = SocialPlayerManager.getManager().getSocialPlayer(args[1]);
                                    if (playerToKick != null) {
                                        socialPlayer.getParty().broadcast(ChatColor.RED + playerToKick.getName() + " has been kicked from the party");
                                        socialPlayer.getParty().kick(playerToKick);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "That player doesn't exist or isn't online!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Only the party master may use this command");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You're not in a party.");
                            }                            
                        }


                    } else if (args[0].equalsIgnoreCase("droprule")) {
                        if (socialPlayer.isInParty()) {
                            if (socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                if (Util.validateCommandLength(cmd.getName(), args, 2, player)) {
                                    try {
                                        socialPlayer.getParty().setDropRule(DropRule.valueOf(args[1].toUpperCase()));
                                        socialPlayer.getParty().broadcast(ChatColor.GOLD + "Droprule changed to " + args[1].toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        player.sendMessage(ChatColor.RED + "Permitted values: Equal, Random, Own");
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Only the party master may use this command");
                            }                            
                        } else {
                            player.sendMessage(ChatColor.RED + "You're not in a party.");
                        }
                    } else if (args[0].equalsIgnoreCase("tpall")) {
                        if (socialPlayer.isInParty()) {
                            if (socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                socialPlayer.getParty().tpAll();
                            } else {
                                sender.sendMessage(ChatColor.RED + "Only the party master may use this command");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You're not in a party.");
                        }
                    } else if (args[0].equalsIgnoreCase("disband")) {
                        if (socialPlayer.isInParty()) {
                            if (socialPlayer.getParty().getMaster().equals(socialPlayer)) {
                                socialPlayer.getParty().broadcast(ChatColor.RED + "Party disbanded");
                                socialPlayer.getParty().disband();
                            } else {
                                sender.sendMessage(ChatColor.RED + "Only the party master may use this command");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You're not in a party.");
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (socialPlayer.isInParty()) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(ChatColor.GOLD).append("Master: ").append(Util.formatNameByHabitat(socialPlayer.getParty().getMaster()));
                            for (SocialPlayer partyMember : socialPlayer.getParty().getMembers()) {
                                stringBuilder.append(Util.formatNameByHabitat(partyMember));
                            }
                            player.sendMessage(stringBuilder.toString());
                        } else {
                            player.sendMessage(ChatColor.RED + "You're not in a party.");
                        }
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        PartyRequest partyRequest = socialPlayer.getPartyRequest();

                        if (partyRequest != null) {
                            partyRequest.accept();
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have a party request!");
                        }

                    } else if (args[0].equalsIgnoreCase("decline")) {
                        PartyRequest partyRequest = socialPlayer.getPartyRequest();

                        if (partyRequest != null) {
                            partyRequest.decline();
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have a party request!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Invalid command! Run /party for help.");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.GOLD + "HabitatSocial - " + ChatColor.ITALIC + ChatColor.AQUA + "Available commands:");
                    sender.sendMessage(ChatColor.GOLD + "Register - " + ChatColor.ITALIC + ChatColor.AQUA + "Register your personal info");
                    sender.sendMessage(ChatColor.GOLD + "Friend - " + ChatColor.ITALIC + ChatColor.AQUA + "Friend-related commands");
                    sender.sendMessage(ChatColor.GOLD + "Party - " + ChatColor.ITALIC + ChatColor.AQUA + "Party-related commands");
            }
        }

        return true;
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerLeaveListener(), this);
        pluginManager.registerEvents(new PlayerJoinPartyListener(), this);
        pluginManager.registerEvents(new PlayerLeavePartyListener(), this);
        pluginManager.registerEvents(new PlayerPickupItemListener(), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
