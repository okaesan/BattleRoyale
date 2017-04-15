package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

/*
 * ユーティリティクラス。自由にどうぞ
 */
public class MainUtils {
	
	/*
	 * HashMapを降準にソート
	 * @param ソートしたいリスト
	 * @return ソート後のKayリスト
	 */
	public static ArrayList<Player> scoreSort(HashMap<Player, Integer> hashMap){
		ArrayList<Player> sortArray = new ArrayList<>();
		for(Player player : hashMap.keySet()){
			if(sortArray.isEmpty()){
				sortArray.add(player);
				continue;
			}
			for(int i=0; i<sortArray.size(); i++){
				if(hashMap.get(sortArray.get(i)) < hashMap.get(player)){
					sortArray.add(0, player);
					break;
				}
				if(i == sortArray.size()-1){
					sortArray.add(player);
					break;
				}
			}
		}
		
		return sortArray;
	}
}
