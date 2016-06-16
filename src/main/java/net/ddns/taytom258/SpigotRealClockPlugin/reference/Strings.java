/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.reference;

/**
 * Holds various string variables
 * 
 * @author taytom258
 *
 */
public class Strings {

	public static String geodb = "plugins/RealClock/GeoLite2-City.mmdb";
	public static String geojar = "GeoLite2-City.mmdb";
	public static String dberror = "GeoLite Database not found, creating!";
	public static String dbsuccess = "GeoLite Database Found!";
	public static String apierror = "TimeZoneDB API key not set. Plugin will not work without this.";
	
	public static String confignf = "Config.yml not found, creating defaults!";
	public static String configf = "Config.yml found, loading!";
	public static String configold = "Config.yml is old version, creating new one!";
	public static String configload = "Config Loading complete...";
	public static String confighead = "Configuration for RealClock Plugin";
	
	public static String commandconsole = "You must be a player to issue this command!";
	public static String commandbypass = "§4You are bypassing the clock command cooldown! This is not intended, please change perms ASAP!";
	public static String commanddeny = "§4You do not have access to that command";
	
	public static String reloadComplete = "Config Reloaded";
}
