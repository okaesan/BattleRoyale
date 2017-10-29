package com.plugin.ftb.battleroyale;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StartingInventory extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	public static void StartingInventoryFunc(Player p) {
		FileConfiguration firstItemsConfig = MainConfig.getFirstItemsConfig();

		//[items.yml]のアイテムがいくつあるかの値
		int a = firstItemsConfig.getInt("firstItem.setItemCounter");

		Random r = new Random();

		int ran = r.nextInt(a) + 1;

		p.getInventory().clear();

		Material material = Material.getMaterial(firstItemsConfig.getString("firstItem.item" + ran));
		ItemStack itemStack = new ItemStack(material);
		ItemMeta meta = itemStack.getItemMeta();

		if(firstItemsConfig.getString("firstItem.item" + ran + "_name") != null) {
			String name = firstItemsConfig.getString("firstItem.item" + ran + "_name");

			meta.setDisplayName(name);
			itemStack.setItemMeta(meta);
		}else {
			if(material==Material.BOW){
				p.getInventory().addItem(new ItemStack(Material.ARROW, firstItemsConfig.getInt("firstItem.item" + ran + "_amount")));
			}else {
				if(firstItemsConfig.getString("firstItem.item" + ran + "_amount") != null){
					itemStack.setAmount(firstItemsConfig.getInt("firstItem.item" + ran + "_amount"));
				}
			}
		}

		itemStack.setDurability((short) firstItemsConfig.getInt("firstItem.item" + ran + "_damage"));

		if(firstItemsConfig.getString("firstItem.item" + ran + "_unbreakable") != null){
			meta.setUnbreakable(firstItemsConfig.getBoolean("firstItem.item" + ran + "_unbreakable"));
			itemStack.setItemMeta(meta);
		}

		p.getInventory().addItem(itemStack);
	}
}