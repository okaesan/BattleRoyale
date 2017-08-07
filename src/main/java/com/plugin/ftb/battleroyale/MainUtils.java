package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

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

		//戻り値用 deathTimeの要素番号(Player判別)/ランキング
		Map<Integer, Integer> rankSort = new HashMap<Integer, Integer>();

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
		for(int rankNumber = 1; rankNumber<=2; ){
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

	/*
	 * 最後の生き残りが2位のプレイヤーと同じタイミングで死んだかどうかをチェックするとき、禁止区域にいたかどうかで判断する
	 * @param 最後の生き残りのプレイヤーの情報
	 * @return 禁止区域内にいたら0, 禁止区域外だったら1
	 */

	@SuppressWarnings("deprecation")
	public static boolean lastPlayer(Player lastPlayer){
		BattleRoyale plugin = BattleRoyale.plugin;
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
		ArrayList<Integer> locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsL");
		ArrayList<Integer> locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsR");

		for(int i:PlusThreadClass.deathRandomCountPast){
			//Bukkit.broadcastMessage(PlusThreadClass.deathRanCount.size() + ", " + PlusThreadClass.deathRan.size());
			if(PlusThreadClass.deathRandomCount.isEmpty() || PlusThreadClass.deathRandom.isEmpty()){
				return true;
			}

			int r = PlusThreadClass.deathRandom.get(i);
			int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
			int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);

			if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(p.isOnline()){
						if(pdaX-64<=(int)p.getPlayer().getLocation().getX()&&pdaX>=(int)p.getPlayer().getLocation().getX()
								&&pdaZ-64<=(int)p.getPlayer().getLocation().getZ()&&pdaZ>=(int)p.getPlayer().getLocation().getZ()){

							return false;

						}
					}
				}
			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(p.isOnline()){
						if(pdaX+64>=(int)p.getPlayer().getLocation().getX()&&pdaX<=(int)p.getPlayer().getLocation().getX()
								&&pdaZ+64>=(int)p.getPlayer().getLocation().getZ()&&pdaZ<=(int)p.getPlayer().getLocation().getZ()){

							return false;

						}
					}
				}

			}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(p.isOnline()){
						if(pdaX-64<=(int)p.getPlayer().getLocation().getX()&&pdaX>=(int)p.getPlayer().getLocation().getX()
								&&pdaZ+64>=(int)p.getPlayer().getLocation().getZ()&&pdaZ<=(int)p.getPlayer().getLocation().getZ()){

							return false;

						}
					}
				}

			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					if(p.isOnline()){
						if(pdaX+64>=(int)p.getPlayer().getLocation().getX()&&pdaX<=(int)p.getPlayer().getLocation().getX()
								&&pdaZ-64<=(int)p.getPlayer().getLocation().getZ()&&pdaZ>=(int)p.getPlayer().getLocation().getZ()){

							return false;

						}
					}
				}
			}
		}

		return true;
	}
}