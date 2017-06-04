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
		ArrayList<Player> sortArray = new ArrayList<>();	//昇順完成後のリスト
		for(Player player : hashMap.keySet()){				//プレイヤーをhashMapから取り出し
			if(sortArray.isEmpty()){						//用意したリストが空なら先頭に配置
				sortArray.add(player);
				continue;
			}
			for(int i=0; i<sortArray.size(); i++){
				if(hashMap.get(player) > hashMap.get(sortArray.get(i))){	//用意したリストに入っているプレイヤーの値と比較
					sortArray.add(i, player);
					break;
				}
			}
			if(!sortArray.contains(player)){
				sortArray.add(player);						//最後まで低ければ最後に配置
			}
		}
		
		return sortArray;
	}
}
