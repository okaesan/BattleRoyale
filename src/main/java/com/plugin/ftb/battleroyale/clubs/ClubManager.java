package com.plugin.ftb.battleroyale.clubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.plugin.ftb.battleroyale.BattleRoyale;


public class ClubManager extends BattleRoyale{
	
	public static BattleRoyale plugin = BattleRoyale.plugin;
	private static List<Club> clublist = new ArrayList<>();
	
	/**
	 * システムに部活を登録します。<p>
	 * 登録されていない場合、エラーになるのため最初に登録してください。
	 */
	public static void addClub(Club club){
		clublist.add(club);
	}
	
	/**
	 * システムに部活を登録します。<p>
	 * 登録されていない場合、エラーになるのため最初に登録してください。<p>
	 * また、表示されるべき名前も同時に設定します。
	 */
	public static void addClub(Club club, String displayname){
		clublist.add(club);
		club.setDisplayName(displayname);
	}
	
	/**
	 * プレイヤーに部活を設定します。
	 */
	public static void setClub(Player player ,Club club){
		if(ClubManager.getClubList().contains(club)){
			BattleRoyale.playersClub.put(player.getUniqueId(), club);
		}
		else{
			Bukkit.broadcastMessage(ChatColor.RED + "部活 <" + club.getName() + "> は登録されていません。");
		}
	}
	
	/**
	 * プレイヤーの部活を取得します。<p>
	 * @return プレイヤーの所属する部活(Club)
	 */
	public static Club getClub(Player player){
		if(BattleRoyale.playersClub.get(player.getUniqueId()) != null) {
			return BattleRoyale.playersClub.get(player.getUniqueId());
		}
		return ClubManager.getClub("Dummy");
	}
	
	/**
	 * プレイヤーが部活に所属しているか判定します。
	 * @return プレイヤーが部活に所属しているか(true or false)
	 */
	public static boolean hasClub(Player player){
		
		if(ClubManager.getClub(player) != ClubManager.getClub("Dummy")){
			return true;
		}
		return false;
	}
	
	/**
	 * すべてのプレイヤーから部活を消去します。
	 */
	public static void removeClubs(){
		for(Player p: Bukkit.getOnlinePlayers()){
			BattleRoyale.playersClub.clear();
		}
	}
	
	/**
	 * 対象から部活を消去します。
	 */
	public static void removeClub(Player player){
		BattleRoyale.playersClub.remove(player.getUniqueId());
	}
	
	/**
	 * 対象をシステムから抹消します。
	 * @param club
	 */
	public static void removeClub(Club club){
		if(ClubManager.getClubList().contains(club)){
			ClubManager.getClubList().remove(club);
		}
	}
	
	/**
	 * 登録されている部活一覧を取得します。
	 * @return 部活リスト(List Club)
	 */
	public static List<Club> getClubList(){
		return clublist;
	}
	
	/**
	 * システム名から部活を取得します。<p>
	 * 登録されていない場合、Nullが変えるので注意してください。
	 * @return Club
	 */
	public static Club getClub(String clubname){
		
		for(Club club : ClubManager.getClubList()){
			if(clubname.equals(club.getName())){
				return club;
			}
		}
		Bukkit.broadcastMessage(ChatColor.RED + "部活 <" + clubname + "> は存在しません");
		return  null;
	}
	
	/**
	 * システム名が対象の部活が存在するか判定します。
	 * @param clubname
	 * @return 対象が存在するか(true or false)
	 */
	public static boolean isClub(String clubname){
		
		if(clubname == null){
			return false;
		}
		
		for(Club club : ClubManager.getClubList()){
			if(clubname.equals(club.getName())){
				return true;
			}
		}
		return  false;
	}
	
	public static Club getRandomClub() {
		
		List<Club> clubs = clublist;
		
		Collections.shuffle(clubs);
		
		return clubs.get(0);
		
	}
}




























