
package com.github.zyxgad.authlib.json;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class UserDataJsonType{

	private String name;
	private String password;

	public UserDataJsonType(JsonReader reader) throws IOException{
		this.fromJson(reader);
	}

	public UserDataJsonType(String name, String password){
		this.name = name;
		this.setPassword(password);
	}

	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}

	public boolean equalsPassword(String password){
		return this.password.equals(password);
	}
	public void setPassword(String password){
		this.password = password;
	}

	public void toJson(JsonWriter writer) throws IOException{
		writer.name(this.name);
		writer.beginObject();
		writer.name("password");
		writer.value(this.password);
		writer.endObject();
	}
	public void fromJson(JsonReader reader) throws IOException{
		this.name = reader.nextName();
		reader.beginObject();
		while(reader.hasNext()){
			final String key = reader.nextName();
			if(key.equals("password")){
				this.password = reader.nextString();
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
	}
}
