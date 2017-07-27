package com.plugin.ftb.battleroyale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StartingInventory extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;

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

		 Random r = new Random();

		 int ran = r.nextInt(a) + 1;

		 //p.getEquipment().clear();
		 p.getInventory().clear();
		 
		 ItemStack itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(conf.getProperty("item" + ran))));
		 
		 if(conf.getProperty("item" + ran + "_name") != null && conf.getProperty("item" + ran + "_amount") != null) {
			ItemMeta im = itemStack.getItemMeta();
			
			String name = "§e" + conf.getProperty("item" + ran + "_name") + "§e ▪ «" + conf.getProperty("item" + ran + "_amount") + "»";
			
			im.setDisplayName(name);
			itemStack.setItemMeta(im);
			
		 }
		p.getInventory().addItem(itemStack);
		p.sendMessage("item"+ran);
	}
}