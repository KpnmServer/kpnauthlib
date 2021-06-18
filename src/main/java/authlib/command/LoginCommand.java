
package com.github.zyxgad.authlib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.github.zyxgad.authlib.storage.OffUserStorage;
import com.github.zyxgad.authlib.util.ColorTextBuilder;

public final class LoginCommand implements CommandExecutor{
	private final String PWD_WRONG_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("密码错误, 您还有%1$d次尝试机会").toString();

	public LoginCommand(){}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}
		Player player = (Player)sender;
		if(args.length != 1){
			return false;
		}
		int num = 0;
		if((num = OffUserStorage.getInstance().playerLogin(player, args[0])) == -1){
			sender.sendMessage(new ColorTextBuilder()
				.line("=====================================================")
				.add ("--Hello ").green(player.getName()).line()
				.line("--Welcome to play on this server")
				.line("--Have a nice day!")
				.line("=====================================================")
				.toString());
			return true;
		}
		if(num == -2){
			return false;
		}
		sender.sendMessage(String.format(PWD_WRONG_MSG, num));
		return true;
	}
}