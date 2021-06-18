
package com.github.zyxgad.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.github.zyxgad.authlib.storage.OffUserStorage;
import com.github.zyxgad.authlib.util.ColorTextBuilder;

public final class ChangepasswordCommand implements CommandExecutor{
	private final String CHANGE_FAILED_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("密码错误").toString();
	private final String CHANGE_SUCCESS_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("密码修改成功, 请重新登录").toString();

	public ChangepasswordCommand(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}
		Player player = (Player)sender;
		if(args.length != 2){
			return false;
		}
		if(!OffUserStorage.getInstance().playerChangepwd(player, args[0], args[1])){
			sender.sendMessage(CHANGE_FAILED_MSG);
			return true;
		}
		sender.sendMessage(CHANGE_SUCCESS_MSG);
		return true;
	}
}