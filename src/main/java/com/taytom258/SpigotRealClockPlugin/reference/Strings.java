/**
 * 
 */
package com.taytom258.SpigotRealClockPlugin.reference;

/**
 * Holds various string variables
 * 
 * @author taytom258
 *
 */
public class Strings {

	public static String geodb = "plugins/RealClock/GeoLite2-City.mmdb";
	public static String geojar = "GeoLite2-City.mmdb";
	public static String icon = "server-icon.png";
	public static String dberror = "GeoLite Database not found, creating!";
	public static String dbsuccess = "GeoLite Database Found!";
	public static String apierror = "TimeZoneDB API key not set. Please set key in config.yml.";
	public static String protoloaderror = "ProtocolLib is not loaded and is required.";
	public static String permloaderror = "PermissionEx plugin is not loaded. Commands will follow default assignment";
	public static String protoload = "ProtocolLib Plugin Loaded";
	public static String permload = "PermissionEx Plugin Loaded";
	public static String usegoogle = "Server is running on your local network, using Google DNS (8.8.8.8) as IP";
	public static String mmenabledkick = "§cThe server is currently undergoing maintenance and you cannot join at this time! Try again later";

	public static String confignf = "Config.yml not found, creating defaults!";
	public static String configf = "Config.yml found, loading!";
	public static String configold = "Config.yml is an old version, creating new one!";
	public static String configload = "Config Loading complete...";
	public static String confighead = "Configuration for RealClock Plugin";

	public static String commandconsole = "You must be a player to issue this command!";
	public static String commandbypass = "§4You are bypassing the clock command cooldown! This is not intended, please change perms ASAP!";
	public static String commanddeny = "§4You do not have access to that command";

	public static String reloadComplete = "Config Reloaded";

	public static String mmenabling = "Maintenance mode will be enabled in 10 seconds!";
	public static String mmenabled = "Maintenance mode has been enabled!";
	public static String mmdisabled = "Maintenance mode has been disabled!";
}
