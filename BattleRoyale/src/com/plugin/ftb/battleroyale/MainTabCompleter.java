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
		if (sender instanceof Player) {
			if(label.equalsIgnoreCase("battleroyale")){
				if(args.length == 1){
					tab.add("setStageL");
					tab.add("setStageR");
					tab.add("setLobbypoint");
					tab.add("setStartpoint");
					tab.add("setDeathpoint");
					tab.add("setTimer");
					tab.add("setChest");
					tab.add("comChest");
					tab.add("setMap");
					return tab;
				}
			}
		}
		return tab;
	}
}
