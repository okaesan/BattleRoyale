package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

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
	public static MapCanvas mainCanvas;

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		mainCanvas = canvas;
		if(deathRan.isEmpty()){
			return;
		}
		//canvas.setPixel(0, 0, (byte)10);
		plugin.reloadConfig();

		String scaleString = plugin.getConfig().getString("mapScale");
		Scale scale = Scale.FARTHEST;
		if(scaleString.equalsIgnoreCase("CLOSEST")){
			scale = Scale.CLOSEST;
		}if(scaleString.equalsIgnoreCase("CLOSE")){
			scale = Scale.CLOSE;
		}if(scaleString.equalsIgnoreCase("NORMAL")){
			scale = Scale.NORMAL;
		}if(scaleString.equalsIgnoreCase("FAR")){
			scale = Scale.FAR;
		}if(scaleString.equalsIgnoreCase("FARTHEST")){
			scale = Scale.FARTHEST;
		}

		int edgeX = map.getCenterX();
		int edgeZ = map.getCenterZ();

		//マップの左下の座標を計算
		//CLOSESTの時、1ピクセル=座標1
		float locPerPix = 16;
		float pixPerLoc = 1;

		if(scale.equals(Scale.CLOSEST)){
			edgeX -= 64;
			edgeZ -= 64;
		}if(scale.equals(Scale.CLOSE)){
			edgeX -= 128;
			edgeZ -= 128;
			locPerPix /= 2;
			pixPerLoc *= 2;
		}
		if(scale.equals(Scale.NORMAL)){
			edgeX -= 256;
			edgeZ -= 256;
			locPerPix /= 4;
			pixPerLoc *= 4;
		}
		if(scale.equals(Scale.FAR)){
			edgeX -= 512;
			edgeZ -= 512;
			locPerPix /= 8;
			pixPerLoc *= 8;
		}
		if(scale.equals(Scale.FARTHEST)){
			edgeX -= 1024;
			edgeZ -= 1024;
			locPerPix /= 16;
			pixPerLoc *= 16;
		}

		for(int i:PlusThreadClass.deathRanCount){

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

	// ブロードキャスト
	public void broadcast(Object message) {
		BattleRoyale.broadcast(message.toString());
	}
}
