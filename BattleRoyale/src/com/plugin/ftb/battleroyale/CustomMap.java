package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

public class CustomMap extends MapRenderer {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	public static ArrayList<Integer> plusDeathX = PlusDeathArea.plusDeathX;
	public static ArrayList<Integer> plusDeathZ = PlusDeathArea.plusDeathZ;
	public static ArrayList<Integer> deathRan = PlusThreadClass.deathRan;
	public static ArrayList<Integer> locL = PlusDeathArea.locL;
	public static ArrayList<Integer> locR = PlusDeathArea.locR;
	public static String scaleString;
	public static Scale scale = Scale.FARTHEST;
	public static float locPerPix;
	public static float pixPerLoc;
	public static int edgeX;
	public static int edgeZ;

	public static String prefix = BattleRoyale.prefix;

	//描画済みの座標を記憶
	public static HashMap<Player, ArrayList<Location>> pastLoc = new HashMap<>();
	public static HashMap<Player, ArrayList<Location>> pastLocP = new HashMap<>();

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {

		if(PlusThreadClass.deathRanCount.isEmpty() || PlusThreadClass.deathRan.isEmpty()){
			return;
		}

		/* 猶予エリアの描画
		 * 追加されたときのみ描画する。
		 */
		for(int i:PlusThreadClass.deathRanCount){
			if(i >= PlusThreadClass.deathRan.size()){
				break;
			}
			int r = PlusThreadClass.deathRan.get(i);

			int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
			int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);

			/*
			 * 過去に描画した座標ならスキップ
			 */
			Location loc = new Location(Bukkit.getWorlds().get(0), pdaX, 0, pdaZ);
			if(!pastLoc.containsKey(player)){
				pastLoc.put(player, new ArrayList<Location>());
			}else if(pastLoc.get(player).contains(loc)){
				continue;
			}
			pastLoc.get(player).add(loc);

			if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) - x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)122);
					}
				}
			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)122);
					}
				}
			}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc)- x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)122);
					}
				}

			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)122);
					}
				}
			}
		}

		for(int i:PlusThreadClass.deathRanCountPast){

			int r = PlusThreadClass.deathRan.get(i);

			int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
			int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);
			if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) - x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
					}
				}
			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
					}
				}
			}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc)- x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
					}
				}

			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
					}
				}
			}
		}

		for(int i:PlusThreadClass.deathRanCountPast){

			int r = PlusThreadClass.deathRan.get(i);
			int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
			int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);

			/*
			 * 過去に描画した座標ならスキップ
			 */
			Location loc = new Location(Bukkit.getWorlds().get(0), pdaX, 0, pdaZ);
			if(!pastLocP.containsKey(player)){
				pastLocP.put(player, new ArrayList<Location>());
			}else if(pastLocP.get(player).contains(loc)){
				continue;
			}
			pastLocP.get(player).add(loc);

			if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) - x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
					}
				}
			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
					}
				}
			}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc)- x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
					}
				}

			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(int x=0; x<locPerPix*4; x++){
					for(int z=0; z<locPerPix*4; z++){
						canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
					}
				}
			}
		}
	}

	// ブロードキャスト
	public void broadcast(Object message) {
		BattleRoyale.broadcast(message.toString());
	}
}
