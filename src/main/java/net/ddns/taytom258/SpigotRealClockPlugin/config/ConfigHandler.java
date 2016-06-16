/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.config;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Handles configuration saving and reading
 * 
 * @author taytom258
 *
 */
public class ConfigHandler {

	private static File folder, file;
	private static FileConfiguration config;
	private static Logger log;
	private static JavaPlugin plugin;

	/**
	 * Reload Configuration
	 */
	public static void reload(){
		JavaPlugin.getPlugin(Plugin.class).reloadConfig();
		config = JavaPlugin.getPlugin(Plugin.class).getConfig();
		checkConfig();
	}
	
	/**
	 * Initilize config variables
	 */
	public static void init() {
		
		folder = JavaPlugin.getPlugin(Plugin.class).getDataFolder();
		log = JavaPlugin.getPlugin(Plugin.class).getLogger();
		config = JavaPlugin.getPlugin(Plugin.class).getConfig();
		plugin = JavaPlugin.getPlugin(Plugin.class);
	}

	/**
	 * Checks if config folders exist and if the config file exists.
	 * Creates them both if they do not exist.
	 */
	public static void checkConfig() {

		if (!folder.exists()) {
			folder.mkdirs();
		}
		file = new File(folder, "config.yml");
		checkDefaults();
		loadValues();
		file = null;
		//log.info(Strings.configload);
	}

	/**
	 * Check if the default config needs to be read in
	 */
	private static void checkDefaults() {
		if (!file.exists()) {
			log.info(Strings.confignf);
			makeDefaults();
		} else {
			log.info(Strings.configf);
			Configuration.ver = config.getDouble(Configuration.path_ver);
			if (Configuration.ver != Double.parseDouble(Plugin.ver)) {
				log.info(Strings.configold);
				file.delete();
				makeDefaults();
			}
		}
	}

	/**
	 * Copy default config from jar
	 */
	private static void makeDefaults() {
		plugin.saveDefaultConfig();
	}

	/**
	 * Load config values from existing file
	 */
	private static void loadValues() {
		Configuration.latlng = config.getBoolean(Configuration.path_latlng);
		Configuration.timeformat = config.getString(Configuration.path_timeformat);
		Configuration.chatcolor = config.getString(Configuration.path_chatcolor);
		Configuration.develop = config.getBoolean(Configuration.path_develop);
		Configuration.api = config.getString(Configuration.path_api);
		Configuration.log = config.getString(Configuration.path_log);
	}

}
