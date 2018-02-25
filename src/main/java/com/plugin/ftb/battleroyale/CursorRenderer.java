package com.plugin.ftb.battleroyale;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CursorRenderer extends MapRenderer {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	public static float locPerPix = CustomMap.locPerPix;
	public static float pixPerLoc = CustomMap.pixPerLoc;
	public static int edgeX = CustomMap.edgeX;
	public static int edgeZ = CustomMap.edgeZ;

	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		//カーソルは中心0, 0
		int centerX = map.getCenterX();
		int centerZ = map.getCenterZ();

		float yaw = player.getLocation().getYaw();
		byte mapYaw;
		if(yaw >= 0){
			mapYaw = (byte)(yaw/360 * 15);
		}else{//逆回転になる
			mapYaw = (byte)(15 + (yaw/360 * 15));// (yaw/360 * 15)はマイナス
		}

		MapCursorCollection cursors=new MapCursorCollection();
		cursors.addCursor(new MapCursor((byte)((player.getLocation().getX() - centerX) / (pixPerLoc/2)), (byte)((player.getLocation().getZ() - centerZ) / (pixPerLoc/2)), mapYaw,
					MapCursor.Type.WHITE_POINTER.getValue(), true));
	    canvas.setCursors(cursors);
	}

}