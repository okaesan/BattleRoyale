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

		 p.getInventory().addItem(new ItemStack(Material.getMaterial(conf.getProperty("items"+ran))));
		 p.sendMessage("item"+ran);
	}
}