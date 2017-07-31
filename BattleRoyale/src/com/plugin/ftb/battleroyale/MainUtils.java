package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
 * ユーティリティクラス。自由にどうぞ
 */
public class MainUtils {

	/*
	 * プレイヤーを死亡した時間順にhashmapに入れる
	 * @param 死亡時間
	 * @return HashMap<順位に対応する人が入ってるlistの要素番号, 順位(3位まで)>
	 */
	public static Map<Integer, Integer> rankSort(ArrayList<Integer> deathTime) {

		//戻り値用
		Map<Integer, Integer> rankSort = new TreeMap<Integer, Integer>();

		//死亡した時間が遅い人の要素番号を保存(listなのは時間が重複している時のため)
		ArrayList<Integer> deathTimeRepeat = new ArrayList<>();

		//ゲームの参加人数(生存者+死亡者)
		int listSize = deathTime.size();
		//ループ内での最大値を保存
		int strongPlayer = -1;

		/*
		 * @rankNumber 順位
		 * @playerNumber リストから取得する要素番号
		 * @registerRank ランクの登録時に使用
		 */
		for(int rankNumber = 1; rankNumber<=3; ){
			for(int playerNumber = 0; playerNumber<listSize; playerNumber++){
				//死亡した時間が遅い人(ランキング上位の人)を探す
				if(strongPlayer<deathTime.get(playerNumber)){
					strongPlayer = deathTime.get(playerNumber);
					deathTimeRepeat.clear();
					deathTimeRepeat.add(playerNumber);
				}
				else if(strongPlayer==deathTime.get(playerNumber)){
					deathTimeRepeat.add(playerNumber);
				}
			}
			for(int registerRank : deathTimeRepeat){
				rankSort.put(registerRank, rankNumber);
				deathTime.set(registerRank, -2);
			}
			rankNumber+=deathTimeRepeat.size();
			strongPlayer = -1;
			deathTimeRepeat.clear();
		}

		return rankSort;
	}
}