package net.wurstclient.forge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.wurstclient.forge.KeybindList.Keybind;
import net.wurstclient.forge.utils.JsonUtils;

public class Friends {
	private final Path path;
	private final ArrayList<Friend> friends = new ArrayList<>();
	private final TreeSet<String> friends2 = new TreeSet<>();
	
	public Friends(Path file)
	{
		path = file;
	}
	public static class Friend
	{
		private final String name;
		private final String commands;
		
		public Friend(String name, String commands)
		{
			this.name = name;
			this.commands = commands;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getCommands()
		{
			return commands;
		}
	}
	private void save()
	{
		JsonObject json = new JsonObject();
		for(Friend friend : friends)
			json.addProperty(friend.getName(), friend.getCommands());
		
		try(BufferedWriter writer = Files.newBufferedWriter(path))
		{
			JsonUtils.prettyGson.toJson(json, writer);
			
		}catch(IOException e)
		{
			System.out.println("Failed to save " + path.getFileName());
			e.printStackTrace();
		}
	}
	public void add(String name, String commands)
	{
		friends.removeIf(friend -> name.equals(friend.getName()));
		friends.add(new Friend(name, commands));
		friends.sort(Comparator.comparing(Friend::getName));
		save();
	}
	
	public void remove(String key)
	{
		friends.removeIf(friend -> key.equals(friend.getName()));
		save();
	}
	
	public void removeAll()
	{
		friends.clear();
		save();
	}
	public String getCommands(String name)
	{
		for(Friend friend : friends)
		{
			if(!name.equals(friend.getName()))
				continue;
			
			return friend.getCommands();
		}
		
		return null;
	}
	public void init()
	{
		JsonObject json;
		try(BufferedReader reader = Files.newBufferedReader(path))
		{
			json = JsonUtils.jsonParser.parse(reader).getAsJsonObject();
			
		}catch(NoSuchFileException e)
		{
			loadDefaults();
			return;
			
		}catch(Exception e)
		{
			System.out.println("Failed to load " + path.getFileName());
			e.printStackTrace();
			
			loadDefaults();
			return;
		}
		
		friends.clear();
		
		TreeMap<String, String> keybinds2 = new TreeMap<>();
		for(Entry<String, JsonElement> entry : json.entrySet())
		{
			String name = entry.getKey().toUpperCase();
			
			if(!entry.getValue().isJsonPrimitive()
				|| !entry.getValue().getAsJsonPrimitive().isString())
				continue;
			String commands = entry.getValue().getAsString();
			
			keybinds2.put(name, commands);
		}
		
		for(Entry<String, String> entry : keybinds2.entrySet())
			friends.add(new Friend(entry.getKey(), entry.getValue()));
		
		save();
	}
	public void loadDefaults() {
		friends.clear();
		friends.add(new Friend("ice_speed", "hh"));
		save();
	}
	public void addAndSave(String name)
	{
		friends2.add(name);
		save();
	}
	
	public void removeAndSave(String name)
	{
		friends2.remove(name);
		save();
	}
	
	public void removeAllAndSave()
	{
		friends2.clear();
		save();
	}
	
}
