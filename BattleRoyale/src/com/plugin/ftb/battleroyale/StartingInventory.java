package com.plugin.ftb.battleroyale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StartingInventory extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	@SuppressWarnings("deprecation")
	public static void StartingInventoryFunc(Player p) {////関数名がクラス名と被っていたので変更しました。

		File items = new File("plugins/BattleRoyale/items.yml");
		try {
			items.createNewFile();
		} catch (IOException e) {}

		 String filename = "plugins/BattleRoyale/items.yml";
		 Properties conf = new Properties();
		 try {
			 conf.load(new FileInputStream(filename));
		 } catch (IOException e) {}
		 //[items.yml]のアイテムがいくつあるかの値
		 int a = Integer.parseInt(conf.getProperty("setItemCounter"));
		 //while文を何回繰り返しているか
		 int b = 1;
		 //プレイヤーにアイテムをいくつ与えたか
		 int d = 0;
		 int f = Integer.parseInt(conf.getProperty("Items"));

		 while(b <= a){

			 int item = Integer.parseInt(conf.getProperty("item" + b));
			 int chance = Integer.parseInt(conf.getProperty("chance" + b));

			 Random r = new Random();

			 int ran = r.nextInt(10) + 1;
			 if(ran <= chance){
				 p.getInventory().addItem(new ItemStack(Material.getMaterial(item)));
				 d = d + 1;
			 }
			 if(d == f){
				 break;
			 }
			 b = b + 1;
			 if(b != a){
				 b = 1;
			 }
		 }
	}
}