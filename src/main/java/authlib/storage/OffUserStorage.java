
package com.github.zyxgad.authlib.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.github.zyxgad.authlib.AuthLib;
import com.github.zyxgad.authlib.json.UserDataJsonType;
import com.github.zyxgad.authlib.util.ColorTextBuilder;
import com.github.zyxgad.authlib.util.ErrorStackGetter;


public final class OffUserStorage{
	public static final OffUserStorage INSTANCE = new OffUserStorage();
	public static OffUserStorage getInstance(){
		return INSTANCE;
	}

	static final class TicketTimer extends BukkitRunnable{
		TicketTimer(){
		}
		@Override
		public void run(){
			OffUserStorage.getInstance().update();
		}
	}

	static final class Item{
		Player player;
		int type;
		int tick;
		int count;
		Item(Player player, int type){
			this.player = player;
			this.type = type;
			this.tick = 0;
			this.count = 0;
		}
	}

	private final String REG_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("请输入/register <password>注册用户").toString();
	private final String LOGIN_MSG = new ColorTextBuilder()
		.red().add("[SERVER]")
		.add("请输入/login <password>登录用户").toString();
	private final String TIME_OUT_MSG = new ColorTextBuilder()
		.add("[SERVER]")
		.add("登录超时").toString();
	private final String TRY_A_LOT_MSG = new ColorTextBuilder()
		.add("[SERVER]")
		.add("尝试次数过多, 请重新登录").toString();


	private TicketTimer timer = null;
	private boolean enable;
	private Map<String, UserDataJsonType> userdata;
	private Map<UUID, Item> storage;
	private int max_tick;
	private int max_try_num;

	private OffUserStorage(){
		this.enable = false;
		this.userdata = new HashMap<>();
		this.storage = new HashMap<>();
		this.max_tick = 10;
		this.max_try_num = 3;
	}

	public boolean getEnable(){
		return this.enable;
	}

	public void setEnable(boolean enable){
		this.enable = enable;
		if(this.enable){
			if(this.timer != null && this.timer.isCancelled()){
				this.timer = null;
			}
			if(this.timer == null){
				this.timer = new OffUserStorage.TicketTimer();
				this.timer.runTaskTimer(AuthLib.INSTANCE, 0, 40);
			}
			for(Item item: this.storage.values()){
				this.onPlayerJoin(item.player);
			}
		}else{
			if(this.timer != null){
				this.timer.cancel();
				this.timer = null;
			}
		}
	}

	public void reload(){
		final File file = new File(AuthLib.INSTANCE.getDataFolder(), "userdata.json");
		this.userdata.clear();
		if(!file.exists()){
			return;
		}
		try(
			FileReader filer = new FileReader(file);
			JsonReader jreader = new JsonReader(filer)
		){
			UserDataJsonType udata;
			jreader.beginObject();
			while(jreader.hasNext()){
				udata = new UserDataJsonType(jreader);
				this.userdata.put(udata.getName(), udata);
			}
			jreader.endObject();
		}catch(IOException e){
			AuthLib.LOGGER.severe("Read userdata json error:\n" + ErrorStackGetter.getErrorStack(e));
		}
	}

	public void save(){
		final File file = new File(AuthLib.INSTANCE.getDataFolder(), "userdata.json");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				AuthLib.LOGGER.severe("Create userdata.json error:\n" + ErrorStackGetter.getErrorStack(e));
				return;
			}
		}

		try(
			FileWriter filew = new FileWriter(file);
			JsonWriter jwriter = new JsonWriter(filew)
		){
			jwriter.beginObject();
			for(UserDataJsonType udata: this.userdata.values()){
				udata.toJson(jwriter);
			}
			jwriter.endObject();
		}catch(IOException e){
			AuthLib.LOGGER.severe("Write userdata json error:\n" + ErrorStackGetter.getErrorStack(e));
		}
	}

	public int getMaxTick(){
		return this.max_tick;
	}
	public void setMaxTick(int tick){
		if(tick <= 0){
			tick = 3;
		}
		this.max_tick = tick;
	}

	public int getMaxTryNum(){
		return this.max_try_num;
	}
	public void setMaxTryNum(int num){
		if(num <= 0){
			num = 1;
		}
		this.max_try_num = num;
	}

	public boolean onPlayerJoin(Player player){
		if(!WhiteListStorage.getInstance().onPlayerJoin(player)){
			return false;
		}
		if(this.enable){
			Item item = new Item(player, 0);
			if(this.userdata.containsKey(player.getName())){
				item.type = 1;
			}
			this.storage.put(player.getUniqueId(), item);
		}
		return true;
	}

	public void onPlayerQuit(Player player){
		WhiteListStorage.getInstance().onPlayerQuit(player);
		this.storage.remove(player.getUniqueId());
	}

	public boolean isPlayerLogin(Player player){
		Item item;
		return !this.enable || (item = this.storage.get(player.getUniqueId())) == null || item.type == -1;
	}

	public int playerLogin(Player player, String pwd){
		Item item;
		if((item = this.storage.get(player.getUniqueId())) == null){
			return -2;
		}
		if(item.type != 1){
			return -2;
		}
		UserDataJsonType udata;
		if((udata = this.userdata.get(item.player.getName())) == null){
			return -2;
		}
		if(udata.equalsPassword(pwd)){
			item.type = -1;
			return -1;
		}
		item.count++;
		if(item.count >= this.max_try_num){
			player.kickPlayer(TRY_A_LOT_MSG);
			return 0;
		}
		return this.max_try_num - item.count;
	}

	public boolean playerRegister(Player player, String pwd){
		Item item;
		if((item = this.storage.get(player.getUniqueId())) == null){
			return false;
		}
		if(item.type != 0){
			return false;
		}
		if(this.userdata.containsKey(item.player.getName())){
			return false;
		}
		UserDataJsonType udata = new UserDataJsonType(item.player.getName(), pwd);
		this.userdata.put(udata.getName(), udata);
		item.type = 1;
		item.tick = 0;
		return true;
	}

	public boolean playerChangepwd(Player player, String oldpwd, String newpwd){
		if(!this.isPlayerLogin(player)){
			return false;
		}
		UserDataJsonType udata = this.userdata.get(player.getName());
		if(udata == null){
			return false;
		}
		if(!udata.equalsPassword(oldpwd)){
			return false;
		}
		udata.setPassword(newpwd);
		this.storage.put(player.getUniqueId(), new Item(player, 1));
		return true;
	}

	public void update(){
		for(Item item: this.storage.values()){
			switch(item.type){
				case -1: this.storage.remove(item.player.getUniqueId());continue;
				case 0: item.player.sendRawMessage(REG_MSG);break;
				case 1: item.player.sendRawMessage(LOGIN_MSG);break;
			}
			item.tick++;
			if(item.tick >= this.max_tick){
				item.player.kickPlayer(TIME_OUT_MSG);
			}
		}
	}
}
