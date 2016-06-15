/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.api.client.util.Sleeper;

import net.ddns.taytom258.SpigotRealClockPlugin.chat.ChatHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Base clock command class
 * 
 * @author taytom258
 *
 */
public class ClockCommand implements CommandExecutor {

	public static Player player;
	public static HashMap<String, Long> cooldowns;
	public static boolean bypass, cooldown;
	public static Runnable lookupRun, cooldownRun;

	/**
	 * Initialize command, run in onEnable
	 */
	public static void init(){
		
		cooldowns = new HashMap<String, Long>();
		lookupRun = new Runnable() {
			public void run() {
				ClockRunnable.lookup();
			}
		};
		cooldownRun = new Runnable() {
			public void run() {
				ClockRunnable.cooldown();
			}
		};
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
				new Thread(cooldownRun).start();
				if (cooldown){
					return true;
				}else{
					ChatHandler.sendPlayer(player, Configuration.chatcolor, "Loading...");
					new Thread(lookupRun).start();
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
}
