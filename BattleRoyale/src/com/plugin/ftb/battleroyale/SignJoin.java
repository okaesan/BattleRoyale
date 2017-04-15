package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignJoin implements Listener {

	public static ArrayList<Player> join = new ArrayList<>();

	@EventHandler
	public void onSign(SignChangeEvent e){
        if(e.getLine(0).equals("[BattleRoyale]")&&e.getLine(1).equals("joincounter")&&e.getLine(2).equals("Join to Click")){
        	e.setLine(0, ChatColor.DARK_AQUA + "[BattleRoyale]");
        	e.setLine(1, ChatColor.GRAY + String.valueOf(join.size() + "/" + 50));
        	e.setLine(2, ChatColor.GREEN + "Join to Click");
        	e.setLine(3, "-------------");
        }

	}

	@EventHandler
	public void joinSign(PlayerInteractEvent e){
		if (e.getAction()==Action.RIGHT_CLICK_BLOCK){
            if (e.getClickedBlock().getType()==Material.WALL_SIGN || e.getClickedBlock().getType()==Material.SIGN_POST){
            	Sign s = ((Sign) e.getClickedBlock().getState());

            	Player _player = (Player)e.getPlayer();

            	if(s.getLine(0).equals(ChatColor.DARK_AQUA + "[BattleRoyale]")&&s.getLine(1).equals(ChatColor.GRAY + String.valueOf(join.size() + "/" + 50))
            			&&s.getLine(2).equals(ChatColor.GREEN + "Join to Click")&&s.getLine(3).equals("-------------")){
            		for(Player p : join){
            			if(_player == p){
            				_player.sendMessage(ChatColor.GREEN + "既に参加しています！");
            				return;
            			}
            		}

            		join.add(_player);
            		s.setLine(1, ChatColor.GRAY + String.valueOf(join.size() + "/" + 50));
            		s.update();
            		return;
            	}
            }
        }

	}
}
