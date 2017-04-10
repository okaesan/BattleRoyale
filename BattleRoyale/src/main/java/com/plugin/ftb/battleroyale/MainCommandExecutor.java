package com.plugin.ftb.battleroyale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommandExecutor implements CommandExecutor {

	/*
	 * onEnable()メソッドで定義したコマンドが実行されるとこのメソッドを通る。
	 * @param sender コマンド実行者
	 * @param cmd 実行されたコマンド
	 * @param label コマンドエイリアス
	 * @param args コマンドの引数
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
}
