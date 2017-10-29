package com.plugin.ftb.battleroyale.clubs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ClubManagerTabCompleter implements TabCompleter{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player && args.length == 1) {
			if(label.equalsIgnoreCase("clubmanager")){
				tab.add("list");
				tab.add("get");
				tab.add("remove");
				tab.add("set");
				/*tab.add("debugmode");
				tab.add("debugger");*/
				
			}
		}
		if (sender instanceof Player && args.length == 2) {
			if(label.equalsIgnoreCase("clubmanager")){
				
				if(args[0].equals("set") || args[0].equals("remove") || args[0].equals("get") || args[0].equals("debugger")){
					
					for(Player p : Bukkit.getOnlinePlayers()){
						tab.add(p.getName());
					}
				}
				if(args[0].equals("get")){
					tab.add("all");
				}
				/*if(args[0].equals("debugmode")){
					tab.add("true");
					tab.add("false");
				}*/
			}
		}
		if (sender instanceof Player && args.length == 3) {
			if(label.equalsIgnoreCase("clubmanager")){
				
				if(args[0].equals("set")){
					for(Club c : ClubManager.getClubList()){
						tab.add(c.getName());
					}
				}
			}
			
		}
		return tab;
	}
}