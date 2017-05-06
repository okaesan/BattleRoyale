package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

	public int pastSize = 0;
	public int pastSizePast = 0;

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {

		/* 猶予エリアの描画
		 * 追加されたときのみ描画する。
		 */
		if(pastSize != PlusThreadClass.deathRanCount.size()){
			pastSize = PlusThreadClass.deathRanCount.size();

			Bukkit.broadcastMessage(prefix + ChatColor.RED + "30秒後" + ChatColor.GRAY + "に禁止区域が追加されます。");

			for(int i:PlusThreadClass.deathRanCount){
				int r = PlusThreadClass.deathRan.get(i);

				int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
				int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);
				if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) - x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)122);
						}
					}
				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)122);
						}
					}
				}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc)- x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)122);
						}
					}

				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
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
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) - x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
						}
					}
				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
						}
					}
				}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc)- x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
						}
					}

				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
						}
					}
				}
			}
		}

		if(pastSizePast != PlusThreadClass.deathRanCountPast.size()){
			pastSizePast = PlusThreadClass.deathRanCountPast.size();

			Bukkit.broadcastMessage(prefix + ChatColor.RED + "禁止区域が追加されました。");

			for(int i:PlusThreadClass.deathRanCountPast){

				int r = PlusThreadClass.deathRan.get(i);
				int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
				int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);
				if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) - x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
						}
					}
				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
						}
					}
				}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc)- x, (int)((pdaZ - edgeZ)/pixPerLoc) + z, (byte)18);
						}
					}

				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(int x=0; x<locPerPix; x++){
						for(int z=0; z<locPerPix; z++){
							canvas.setPixel((int)((pdaX - edgeX)/pixPerLoc) + x, (int)((pdaZ - edgeZ)/pixPerLoc) - z, (byte)18);
						}
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
