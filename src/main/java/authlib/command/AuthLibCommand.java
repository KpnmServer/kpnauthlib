
package com.github.zyxgad.authlib.command;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.zyxgad.authlib.AuthLib;
import com.github.zyxgad.authlib.storage.WhiteListStorage;
import com.github.zyxgad.authlib.util.ColorTextBuilder;

public final class AuthLibCommand implements CommandExecutor, TabCompleter{
	private final List<String> COMMAND_LIST = Arrays.asList(
		"help",
		"whitelist",
		"reload",
		"save"
	);
	private final String HELP_MSG = new ColorTextBuilder()
		.add("use `/authlib help` to get help massage").line()
		.add("use `/authlib whitelist help` to get the whitelist help").line()
		.toString();

	public AuthLibCommand(){}

	public boolean cmd_help(CommandSender sender, String[] args){
		sender.sendMessage(HELP_MSG);
		return true;
	}

	public boolean cmd_whitelist(CommandSender sender, String[] args){
		if(args.length < 2){
			sender.sendMessage("use `/authlib whitelist help` to get help message");
			return true;
		}
		switch(args[1]){
			case "help": {
				sender.sendMessage("help message");
				break;
			}
			case "add": {
				if(args.length != 3){
					sender.sendMessage("数据不合法, 需要1个参数");
					break;
				}
				WhiteListStorage.getInstance().addWhiteName(args[2]);
				sender.sendMessage("add user success");
				break;
			}
			case "remove": {
				if(args.length != 3){
					sender.sendMessage("数据不合法, 需要1个参数");
					break;
				}
				WhiteListStorage.getInstance().removeWhiteName(args[2]);
				sender.sendMessage("remove user success");
				break;
			}
			case "query": {
				if(args.length != 3){
					sender.sendMessage("数据不合法, 需要1个参数");
					break;
				}

				sender.sendMessage(new ColorTextBuilder().add("user is ")
						.yellow(WhiteListStorage.getInstance().queryWhiteName(args[2]) ?"in" :"not in")
						.add(" the white list").toString());
				break;
			}
			case "enable": {
				if(args.length == 2){
					sender.sendMessage(new ColorTextBuilder().add("white list is on ")
						.yellow(WhiteListStorage.getInstance().getEnable() ?"enable" :"disable").toString());
					break;
				}
				final String value = args[2];
				if(value.equals("true")){
					WhiteListStorage.getInstance().setEnable(true);
					sender.sendMessage("white list change on enable");
					break;
				}else if(value.equals("false")){
					WhiteListStorage.getInstance().setEnable(false);
					sender.sendMessage("white list change on disable");
				}else{
					sender.sendMessage("数据不合法, 只能为'true'或'false'");
				}
				break;
			}
		}
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length == 0){
			return false;
		}
		switch(args[0]){
			case "help":      return this.cmd_help(sender, args);
			case "whitelist": return this.cmd_whitelist(sender, args);
			case "reload": {
				AuthLib.INSTANCE.onReload();
				return true;
			}
			case "save": {
				AuthLib.INSTANCE.onSave();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
		List<String> comps = new ArrayList<>();
		if(args.length == 1){
			for(String cmd0 :COMMAND_LIST){
				if(cmd0.startsWith(args[0])){
					comps.add(cmd0);
				}
			}
		}else if(args.length >= 2){
			//
		}
		return comps;
	}
}