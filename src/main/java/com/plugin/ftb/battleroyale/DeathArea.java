package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

class ThreadClass extends BukkitRunnable{

    public static BattleRoyale plugin = BattleRoyale.plugin;
    public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
    public static ArrayList<Integer> locL = new ArrayList<Integer>();
    public static ArrayList<Integer> locR = new ArrayList<Integer>();

    @SuppressWarnings("deprecation")
    public void run(){
        Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

        if(!(board.getTeam(TEAM_ALIVE_NAME).getSize()>1)){
        	this.cancel();
        }
        locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsL");
        locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsR");

        if (((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1))){
            for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
                if(p.isOnline()){
                    if(locL.get(0)<(int)p.getPlayer().getLocation().getX()||locR.get(0)>(int)p.getPlayer().getLocation().getX()
                            ||locL.get(1)<(int)p.getPlayer().getLocation().getZ()||locR.get(1)>(int)p.getPlayer().getLocation().getZ()){
                        p.getPlayer().setHealth(0.0D);
                    }
                }
            }
        }else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
            for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
                if(p.isOnline()){
                    if((int)locL.get(0)>(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)<(int)p.getPlayer().getLocation().getX()
                            ||(int)locL.get(1)>(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)<(int)p.getPlayer().getLocation().getZ()){
                        p.getPlayer().setHealth(0.0D);
                    }
                }
            }

        }else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
            for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
                if(p.isOnline()){
                    if((int)locL.get(0)<(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)>(int)p.getPlayer().getLocation().getX()
                            ||(int)locL.get(1)>(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)<(int)p.getPlayer().getLocation().getZ()){
                        p.getPlayer().setHealth(0.0D);
                    }
                }
            }

        }else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
            for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
            	if(p.isOnline()){
                    if((int)locL.get(0)>(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)<(int)p.getPlayer().getLocation().getX()
                            ||(int)locL.get(1)<(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)>(int)p.getPlayer().getLocation().getZ()){
                        p.getPlayer().setHealth(0.0D);
                    }
                }
            }
        }else{
        }
    }
}

public class DeathArea{

    public static BattleRoyale plugin = BattleRoyale.plugin;

    public void deathArea(){
        ThreadClass th = new ThreadClass();
        th.runTaskTimer(plugin, 0, 100);
    }
}
