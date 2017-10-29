package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AreaJoin {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static ArrayList<Integer> locL = new ArrayList<Integer>();
	public static ArrayList<Integer> locR = new ArrayList<Integer>();

	public static int betaLx, betaLz, betaRx, betaRz;

	public static void joinArea(){
		locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("joinGameL");
		locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("joinGameR");

		betaLx = (int)locL.get(0);
		betaLz = (int)locL.get(1);
		betaRx = (int)locR.get(0);
		betaRz = (int)locR.get(1);

		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.hasPermission("battleroyale.op")) {
				
			}
			int playerLocationX = (int) player.getLocation().getX();
			int playerLocationZ = (int) player.getLocation().getZ();
			if (betaLx>=playerLocationX&&playerLocationX>=betaRx&&betaLz>=playerLocationZ&&playerLocationZ>=betaRz){
				//Lがx座標、z座標共にRより大きい場合
				JoinSystem.onJoin(player);
			}else if (betaLx<playerLocationX&&playerLocationX<betaRx&&betaLz<playerLocationZ&&playerLocationZ<betaRz){
				//Lがx座標、z座標共にRより小さい場合
				JoinSystem.onJoin(player);
			}else if (betaLx>=playerLocationX&&playerLocationX>=betaRx&&betaLz<playerLocationZ&&playerLocationZ<betaRz){
				//x座標はLが大きく、z座標はRが大きい場合
				JoinSystem.onJoin(player);
			}else if (betaLx<playerLocationX&&playerLocationX<betaRx&&betaLz>=playerLocationZ&&playerLocationZ>=betaRz){
				//x座標はRが大きく、z座標はLが大きい場合
				JoinSystem.onJoin(player);
			}
		}
	}
}
