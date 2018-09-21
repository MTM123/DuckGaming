package me.skorrloregaming;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigurationManager {
	static ConfigurationManager instance = new ConfigurationManager();
	FileConfiguration data;
	File dfile;
	public List<Plugin> plugins = new ArrayList<Plugin>();

	public static ConfigurationManager getInstance() {
		return instance;
	}

	public void setup(File file) {
		this.dfile = file;
		if (!this.dfile.exists()) {
			try {
				this.dfile.createNewFile();
			} catch (IOException e) {
				Logger.severe(ChatColor.RED + "Could not create file configuration!");
			}
		}
		this.data = YamlConfiguration.loadConfiguration(this.dfile);
	}

	public FileConfiguration getData() {
		return this.data;
	}

	public void clearData() {
		try {
			this.dfile.delete();
			setup(dfile);
		} catch (Exception e) {
		}
	}

	public void saveData() {
		try {
			this.data.save(this.dfile);
		} catch (IOException e) {
			Logger.severe(ChatColor.RED + "Could not save file configuration!");
		}
	}

	public void reloadData() {
		this.data = YamlConfiguration.loadConfiguration(this.dfile);
	}
}
