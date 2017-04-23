package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MainTabCompleter implements TabCompleter{
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player && args.length == 1) {
			if(label.equalsIgnoreCase("battleroyale")){
				tab.add("stageL");
				tab.add("stageR");
				tab.add("setLobbypoint");
				tab.add("setStartpoint");
				tab.add("setDeathpoint");
				tab.add("setChest");
				tab.add("comChest");
				tab.add("setMap");
			}if(label.equalsIgnoreCase("startpoint")){
				tab.add("add");
				tab.add("remove");
			}
			return tab;//タブ補完
		}
		
		return tab;
	}
}
