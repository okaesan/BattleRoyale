package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle.EnumTitleAction;

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

			int r = PlusThreadClass.deathNotRandom.get(PlusThreadClass.rootRandom.get(StartCommand.playCount)).get(i);
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
	
	/*
	 * プレイヤーにタイトルを表示する
	 */
	public static void sendTitle(Player player, String title, String subtitle, int fadein, int say, int fadeout) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
		IChatBaseComponent chatsubtitle = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");

		PacketPlayOutTitle t = new PacketPlayOutTitle (EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle s = new PacketPlayOutTitle (EnumTitleAction.SUBTITLE, chatsubtitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle (fadein * 20, say * 20, fadeout * 20);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket (t);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket (s);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket (length);
	}
	
	/*
	 * 全員にタイトルを表示する
	 */
	public static void sendTitleToEveryone(String title, String subtitle, int fadein, int say, int fadeout) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
		IChatBaseComponent chatsubtitle = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");

		PacketPlayOutTitle t = new PacketPlayOutTitle (EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle s = new PacketPlayOutTitle (EnumTitleAction.SUBTITLE, chatsubtitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle (fadein * 20, say * 20, fadeout * 20);

		for(Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket (t);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket (s);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket (length);
		}
	}
	/*
	 * カスタムポーションを返す
	 */
	public static ItemStack getPotion(PotionType effectType, int seconds, int level, int amount){
		ItemStack potion = new ItemStack(Material.SPLASH_POTION, amount);
		PotionMeta potionMeta = (PotionMeta)potion.getItemMeta();
		PotionEffect pe = new PotionEffect(effectType.getEffectType(),seconds*20,level-1);
		potionMeta.addCustomEffect(pe,true);
		potionMeta.setLocalizedName(getPotionName(effectType));
		potionMeta.setColor(effectType.getEffectType().getColor());
		potion.setItemMeta(potionMeta);
		return potion;
	}

	public static String getPotionName(PotionType effectType){
		String s = effectType.getEffectType().getName();
		String name = "";
		switch (s){
			case "AWKWARD":
				name = "奇妙なスプラッシュポーション";
				break;
			case "FIRE_RESISTANCE" :
				name = "耐火のスプラッシュポーション";
				break;
			case "INSTANT_DAMAGE" :
				name = "負傷のスプラッシュポーション";
				break;
			case "INSTANT_HEAL" :
				name = "治癒のスプラッシュポーション";
				break;
			case "INVISIBILITY" :
				name = "透明化のスプラッシュポーション";
				break;
			case "JUMP":
				name = "跳躍のスプラッシュポーション";
				break;
			case "LUCK":
				name = "幸運のスプラッシュポーション";
				break;
			case "MUNDANE" :
				name = "ありふれたスプラッシュポーション";
				break;
			case "NIGHT_VISION":
				name = "暗視のスプラッシュポーション";
				break;
			case "POISON" :
				name = "毒のスプラッシュポーション";
				break;
			case "REGEN" :
				name = "再生のスプラッシュポーション";
				break;
			case "SLOWNESS" :
				name = "鈍化のスプラッシュポーション";
				break;
			case "SPEED" :
				name = "俊敏のスプラッシュポーション";
				break;
			case "STRENGTH" :
				name = "力のスプラッシュポーション";
				break;
			case "THICK" :
				name = "濃厚なスプラッシュポーション";
				break;
			case "WATER" :
				name = "水入りスプラッシュ瓶";
				break;
			case "WATER_BREATHING":
				name = "水中呼吸のスプラッシュポーション";
				break;
			case "WEAKNESS" :
				name = "弱化のスプラッシュポーション";
				break;
			default:
				name = "クラフト不可能なスプラッシュポーション";
				break;
		}
		return name;
	}


	public static void sendDeathArea() {
		//禁止区域をチャットに送信
		String area = "";
		boolean[] areas = new boolean[16];
		boolean[] sortedAreas = new boolean[16];
		for(int count:PlusThreadClass.deathRandomCountPast){
			int s = PlusThreadClass.deathNotRandom.get(PlusThreadClass.rootRandom.get(StartCommand.playCount)).get(count);
			//禁止区域のエリアはtrueにしておく
			areas[s] = true;
		}

		ArrayList<Integer> locL = new ArrayList<Integer>();
		ArrayList<Integer> locR = new ArrayList<Integer>();
		if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
			//上下左右逆
			for(int i=15; i>=0; i--) {
				sortedAreas[i] = areas[15-i];
			}
		}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
			//同じ
			sortedAreas = areas;
		}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
			//左右逆
			for(int x=0; x<4; x++) {
				for(int z=0; z<4; z++) {
					sortedAreas[x+(4*z)] = areas[(3-x)+(4*z)];
				}
			}
		}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
			//逆
			for(int x=0; x<4; x++) {
				for(int z=0; z<4; z++) {
					sortedAreas[x+(4*z)] = areas[x+(4*(3-z))];
				}
			}
		}

		for(int i=0; i<16; i++) {
			if(sortedAreas[i]) {
				area += "■ ";
			}else {
				area += "□ ";
			}
		}

		Bukkit.broadcastMessage(area);
	}

}