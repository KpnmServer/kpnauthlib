
package com.github.zyxgad.authlib;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.zyxgad.authlib.command.LoginCommand;
import com.github.zyxgad.authlib.command.RegisterCommand;
import com.github.zyxgad.authlib.command.ChangepasswordCommand;
import com.github.zyxgad.authlib.command.AuthLibCommand;
import com.github.zyxgad.authlib.storage.OffUserStorage;
import com.github.zyxgad.authlib.storage.WhiteListStorage;
import com.github.zyxgad.authlib.event.PlayerStatusListener;


public final class AuthLib extends JavaPlugin{
	public static AuthLib INSTANCE = null;
	public static Logger LOGGER = null;

	public static final String PLUGIN_NAME = "AuthLib";

	public AuthLib(){}

	@Override
	public void onLoad(){
		if(AuthLib.INSTANCE != this){
			AuthLib.INSTANCE = this;
			AuthLib.LOGGER = this.getLogger();
		}
		LOGGER.info("Authlib is on load");

		this.saveDefaultConfig();
	}

	@Override
	public void onEnable(){
		LOGGER.info("Authlib is on enable");

		OffUserStorage.getInstance().setEnable(true);
		this.onReload();

		this.addListener(Bukkit.getPluginManager());
		this.bindCommands();
	}

	@Override
	public void onDisable(){
		LOGGER.info("Authlib is on disable");

		this.onSave();
		OffUserStorage.getInstance().setEnable(false);
		WhiteListStorage.getInstance().setEnable(false);

		AuthLib.INSTANCE = null;
		AuthLib.LOGGER = null;
	}

	public void onReload(){
		this.reloadConfig();
		Configuration config = this.getConfig();
		OffUserStorage.getInstance().setMaxTick(config.getInt("max_wait_tick"));
		OffUserStorage.getInstance().setMaxTryNum(config.getInt("max_try_num"));

		OffUserStorage.getInstance().reload();
		WhiteListStorage.getInstance().reload();
		WhiteListStorage.getInstance().setEnable(config.getBoolean("whitelist"));
	}

	public void onSave(){
		this.saveConfig();
		OffUserStorage.getInstance().save();
		WhiteListStorage.getInstance().save();
	}

	private void addListener(PluginManager manager){
		manager.registerEvents(new PlayerStatusListener(), this);
	}

	private void bindCommands(){
		this.getCommand("login").setExecutor(new LoginCommand());
		this.getCommand("register").setExecutor(new RegisterCommand());
		this.getCommand("changepassword").setExecutor(new ChangepasswordCommand());
		this.getCommand("authlib").setExecutor(new AuthLibCommand());
	}
}