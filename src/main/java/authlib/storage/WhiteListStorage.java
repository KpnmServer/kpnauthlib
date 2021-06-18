
package com.github.zyxgad.authlib.storage;


import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.github.zyxgad.authlib.AuthLib;
import com.github.zyxgad.authlib.util.ColorTextBuilder;
import com.github.zyxgad.authlib.util.ErrorStackGetter;


public final class WhiteListStorage{
	private static final WhiteListStorage INSTANCE = new WhiteListStorage();
	public static WhiteListStorage getInstance(){
		return INSTANCE;
	}

	private final String NOT_IN_WHITELIST_MSG = new ColorTextBuilder()
		.add("[SERVER]")
		.add("您不在白名单内").toString();

	private boolean enable;
	private Map<UUID, Player> storage;
	private Set<String> whitenames;

	private WhiteListStorage(){
		this.enable = false;
		this.storage = new HashMap<>();
		this.whitenames = new HashSet<>();
	}

	public boolean getEnable(){
		return this.enable;
	}

	public void setEnable(boolean enable){
		if(this.enable == enable){
			return;
		}
		this.enable = enable;
		if(this.enable){
			for(Player p: this.storage.values()){
				if(!this.whitenames.contains(p.getName())){
					p.kickPlayer(NOT_IN_WHITELIST_MSG);
					this.storage.remove(p);
				}
			}
		}
	}

	public void reload(){
		final File file = new File(AuthLib.INSTANCE.getDataFolder(), "whitelist.json");
		this.whitenames.clear();
		if(!file.exists()){
			return;
		}
		try(
			FileReader filer = new FileReader(file);
			JsonReader jreader = new JsonReader(filer)
		){
			jreader.beginObject();
			while(jreader.hasNext()){
				final String key = jreader.nextName();
				if(key.equals("users")){
					jreader.beginArray();
					while(jreader.hasNext()){
						this.whitenames.add(jreader.nextString());
					}
					jreader.endArray();
				}else{
					jreader.skipValue();
				}
			}
			jreader.endObject();
		}catch(IOException e){
			AuthLib.LOGGER.severe("Read whitelist json error:\n" + ErrorStackGetter.getErrorStack(e));
		}
	}

	public void save(){
		final File file = new File(AuthLib.INSTANCE.getDataFolder(), "whitelist.json");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				AuthLib.LOGGER.severe("Create whitelist.json error:\n" + ErrorStackGetter.getErrorStack(e));
				return;
			}
		}

		try(
			FileWriter filew = new FileWriter(file);
			JsonWriter jwriter = new JsonWriter(filew)
		){
			jwriter.beginObject();
			jwriter.name("users");
			jwriter.beginArray();
			for(String name: this.whitenames){
				jwriter.value(name);
			}
			jwriter.endArray();
			jwriter.endObject();
		}catch(IOException e){
			AuthLib.LOGGER.severe("Write whitelist json error:\n" + ErrorStackGetter.getErrorStack(e));
		}
	}

	public Set getWhiteList(){
		return this.whitenames;
	}

	public void addWhiteName(String name){
		this.whitenames.add(name);
	}

	public void removeWhiteName(String name){
		this.whitenames.remove(name);
	}

	public boolean queryWhiteName(String name){
		return this.whitenames.contains(name);
	}

	public boolean onPlayerJoin(Player player){
		if(this.storage.containsKey(player.getUniqueId())){
			return false;
		}
		if(this.enable){
			if(!this.whitenames.contains(player.getName())){
				player.kickPlayer(NOT_IN_WHITELIST_MSG);
				return false;
			}
		}
		this.storage.put(player.getUniqueId(), player);
		return true;
	}

	public void onPlayerQuit(Player player){
		this.storage.remove(player.getUniqueId());
	}
}