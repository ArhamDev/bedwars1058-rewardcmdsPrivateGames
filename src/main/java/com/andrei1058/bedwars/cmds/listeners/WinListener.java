package com.andrei1058.bedwars.cmds.listeners;

import club.mher.privategames.api.PrivateGames;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.cmds.ConfigPath;
import com.andrei1058.bedwars.cmds.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.andrei1058.bedwars.cmds.Main.getPlugin;

public class WinListener implements Listener {

    @EventHandler
    public void onWin(GameStateChangeEvent e) {
        PrivateGames privateGamesAPI = Bukkit.getServicesManager().getRegistration(PrivateGames.class).getProvider();
        if (e.getNewState() == GameState.restarting  && !privateGamesAPI.getPrivateGameUtil().isPrivateGame(e.getArena().getArenaName())) {
            Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                for (Player p : e.getArena().getPlayers()) {
                    for (String s : Main.getCfg().getYml().getStringList(ConfigPath.GAME_WIN_WINNER_CMDS_AS_PLAYER)) {
                        Bukkit.getServer().dispatchCommand(p, "/" + s.replace("{player}", p.getName()
                                .replace("{arena}", e.getArena().getWorldName()))
                                .replace("{arenaDisplay}", e.getArena().getDisplayName())
                        .replace("{group}", e.getArena().getGroup()));
                    }
                    for (String s : Main.getCfg().getYml().getStringList(ConfigPath.GAME_WIN_WINNER_CMDS_AS_CONSOLE)) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s.replace("{player}", p.getName()
                                .replace("{arena}", e.getArena().getWorldName()))
                                .replace("{arenaDisplay}", e.getArena().getDisplayName())
                                .replace("{group}", e.getArena().getGroup()));
                    }
                }
            }, 10L);
        }
    }
}
