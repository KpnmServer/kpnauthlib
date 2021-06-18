
package com.github.zyxgad.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.github.zyxgad.authlib.storage.OffUserStorage;
import com.github.zyxgad.authlib.util.ColorTextBuilder;

public final class RegisterCommand implements CommandExecutor{
	private final String REGISTER_SUCCESS_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("注册成功!请登录").toString();

	public RegisterCommand(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}
		Player player = (Player)sender;
		if(args.length != 1){
			return false;
		}
		if(OffUserStorage.getInstance().playerRegister(player, args[0])){
			sender.sendMessage(REGISTER_SUCCESS_MSG);
			return true;
		}
		sender.sendMessage("[SERVER]Error");
		return true;
	}
}