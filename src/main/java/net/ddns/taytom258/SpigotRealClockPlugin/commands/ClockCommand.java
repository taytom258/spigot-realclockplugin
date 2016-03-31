/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import net.ddns.taytom258.SpigotRealClockPlugin.chat.ChatHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.geoIP.GeoIP;
import net.ddns.taytom258.SpigotRealClockPlugin.http.TimeZoneDB;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Base clock command class
 * 
 * @author taytom258
 *
 */
public class ClockCommand implements CommandExecutor {

	private static Player player;
	private static HashMap<String, Long> cooldowns;
	private static boolean bypass;

	/**
	 * Initialize command, run in onEnable
	 */
	public static void init(){
		
		cooldowns = new HashMap<String, Long>();
	}
	
	/**
	 * De-Initialize command, run in onDisable
	 */
	public static void deinit(){
		
		cooldowns.clear();
		cooldowns = null;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		//Cast sender to player variable if sender is a player
		if (sender instanceof Player) {
			player = (Player) sender;
				
			//Check for command permissions
			boolean superperm = false, clock = false, clockIP = false;
			bypass = false;
			if (player.hasPermission("realclock.clock.*")){
				superperm = true;
			}
			if (player.hasPermission("realclock.clock")){
				clock = true;
			}
			if (player.hasPermission("realclock.clock.ip")){
				clockIP = true;
			}
			if (player.hasPermission("realclock.bypass")){
				bypass = true;
			}
			
			//Check for clock command
			if ((clock || superperm) && label.equalsIgnoreCase("clock") && args.length == 0){
				if (cooldown()){
					return true;
				}else{
					ChatHandler.sendPlayer(player, Configuration.chatcolor, timeLookup());
					return true;
				}
			}
			
			//Check for ip sub command
			if ((label.equalsIgnoreCase("clock") && (args.length > 0))){
				if((clockIP || superperm) && args[0].equalsIgnoreCase("ip")){
					ChatHandler.sendPlayer(player, Configuration.chatcolor, player.getAddress().getHostString());
					return true;
				}else if(!args[0].equalsIgnoreCase("ip")){
					return false;
				}else if(args[0].equalsIgnoreCase("ip")){
					ChatHandler.sendPlayer(player, "6", Strings.commanddeny);
					return true;
				}
			}
			
			//If nothing else hits
			ChatHandler.sendPlayer(player, "6", Strings.commanddeny);
			return true;
			
		}else{
			
			//If anything other then a player sends the command
			sender.sendMessage(Strings.commandconsole);
			return true;
		}
	}
	
	/**
	 * Command cooldown
	 * 
	 * @return Should command stop
	 */
	private static boolean cooldown(){
		
		//Command cooldown
		if (!bypass){
			int cooldownTime = 60; // Time in seconds
			if (cooldowns.containsKey(player.getName())) {
				long secondsLeft = ((cooldowns.get(player.getName())
						/ 1000) + cooldownTime)
						- (System.currentTimeMillis() / 1000);
				if (secondsLeft > 0) {
					// Still cooling down
					ChatHandler.sendPlayer(player, Configuration.chatcolor, "You can not use that command for another "
											+ secondsLeft
											+ " seconds!");
					return true;
				}else{
					// Cooldown has expired, save new cooldown
					cooldowns.put(player.getName(), System.currentTimeMillis());
				}
			}else{
				// Cooldown not found, save new cooldown
				cooldowns.put(player.getName(), System.currentTimeMillis());
			}
		}
		return false;
	}
	
	/**
	 * Get lat & lng from database and use that to get local time
	 * @return local time for player
	 */
	private static String timeLookup(){
		
		String latlng = null, lat = null, lng = null, time = null;
		
		//Run GeoIP Lookup logic
		ChatHandler.sendPlayer(player, Configuration.chatcolor, "Loading...");
		try {
			if (Configuration.develop) {
				latlng = GeoIP.getLocation("70.170.27.129");
			} else {
				latlng = GeoIP.getLocation(player.getAddress().getHostString());
			}
		} catch (IOException e) {
			LogHandler.warning("", e);
		} catch (GeoIp2Exception e) {
			LogHandler.warning("", e);
		}
		
		//Process GeoIP Output
		String[] newlatlng = StringUtils.split(latlng, ';');
		for (int i = 0; i < newlatlng.length; i++) {
			if (i == 0) {
				lat = newlatlng[i];
			} else {
				lng = newlatlng[i];
			}
		}
		
		//Get time from lat & lng
		try {
			time = TimeZoneDB.sendRequest(lat, lng);
		} catch (URISyntaxException e) {
			LogHandler.warning("", e);
		} catch (Exception e) {
			LogHandler.warning("", e);
		}
		return time;
	}
}
