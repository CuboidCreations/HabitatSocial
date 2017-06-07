package com.gmail.Xeiotos.HabitatSocial.GUI;

import com.gmail.Xeiotos.HabitatSocial.Party.Party;
import com.gmail.Xeiotos.HabitatSocial.SocialPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author Xeiotos
 */
public class PartyGUI implements Listener {

    private Party party;
    private Team team;
    private Scoreboard scoreboard;
    private Objective playerList;

    public PartyGUI(Party party) {
        this.party = party; //TODO health bar

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        team = scoreboard.registerNewTeam(party.getMaster().getName());

        team.setAllowFriendlyFire(party.getFriendlyFire());

        this.playerList = registerPlayerList();
        initPlayers();
    }

    /**
     * Get the scoreboard of this GUI
     *
     * @return Scoreboard of this GUI
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private Objective registerPlayerList() {
        Objective objective = scoreboard.registerNewObjective("PartyGUI", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "- [Party] -");

        return objective;
    }

    private void initPlayers() {
        for (SocialPlayer socialPlayer : party.getMembers()) {
            addPlayer(socialPlayer.getPlayer());
        }

        addPlayer(party.getMaster().getPlayer());
    }

    /**
     * Add a player to this gui
     *
     * @param player Player to add
     */
    public void addPlayer(Player player) {
        team.addPlayer(player);

        player.setScoreboard(scoreboard);
        Score score = playerList.getScore(Bukkit.getOfflinePlayer(player.getName()));
        score.setScore(-1);
    }

    /**
     * Remove a player from this gui
     *
     * @param player Player to remove
     */
    public void removePlayer(Player player) {
        team.removePlayer(player);
        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
        scoreboard.resetScores(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
