package com.plugin.ftb.battleroyale;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StartCommand implements CommandExecutor {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static String prefix = BattleRoyale.prefix;

	 	// キル数カウント
	 	public static HashMap<Player, Integer> killCount = BattleRoyale.killCount;
	 	// ポイントカウント
	 	public static HashMap<Player, Integer> pointCount = BattleRoyale.pointCount;

	DeathArea deathA = new DeathArea();
	PlusDeathArea PdeathA = new PlusDeathArea();

	public static int locX,locY,locZ;
	static int r, c = 0, item;

	/*
	 * ゲーム開始コマンド
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		MainListener.c = 1;
		Player _player = (Player)sender;
		//SLOWエフェクトを削除
		/*for (Player p : SignJoin.join) {
			p.sendMessage("wa-i");
			p.removePotionEffect(PotionEffectType.SLOW);
		}*/

		setChest((Player)sender);


		Bukkit.broadcastMessage(prefix + ChatColor.GOLD + "ゲームスタート");

		deathA.deathArea();
		PdeathA.setPlusDeath();
		PdeathA.plus(_player);
		MainConfig.giveMap();

		return true;
	}

	public void setChest(Player p){

		for(int i = 1; i <= plugin.getConfig().getInt("chestCounter"); i++){
			locX = plugin.getConfig().getInt("chestlocations"+i+".x");
			locY = plugin.getConfig().getInt("chestlocations"+i+".y");
			locZ = plugin.getConfig().getInt("chestlocations"+i+".z");

			Block block = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);
			block.setType(Material.CHEST);
			Chest chest = (Chest)block.getState();
			Inventory inv = chest.getInventory();

			c = 0;

			do{
				item = plugin.getConfig().getInt("chestItem.setItemCounter");
				Material material = Material.getMaterial(plugin.getConfig().getString("chestItem.item"+(int)((Math.random()*1000)%item+1)));

				inv.setItem((int)(Math.random()*729)/27, new ItemStack(material,1));

				c++;
				r = (int)(Math.random() * 1000 + 22) % 22 - c;

			}while(r>0);

		}
		return;
	}
}
