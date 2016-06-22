/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.commands;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;
import net.ddns.taytom258.SpigotRealClockPlugin.chat.ChatHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.ConfigHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Base clock command class
 * 
 * @author taytom258
 *
 */
public class ClockCommand implements CommandExecutor {

	public static Player player;
	public static ConsoleCommandSender console;
	public static HashMap<String, Long> cooldowns;
	public static boolean bypass, cooldown;
	public static Runnable clockRunnable, mmrunnable;

	/**
	 * Initialize command, run in onEnable
	 */
	public static void init(){
		
		
		cooldowns = new HashMap<String, Long>();
		clockRunnable = new Runnable() {
			public void run() {
				ClockRunnable.clock();
			}
		};
//		mmrunnable = new Runnable() {
//			public void run() {
//				ClockRunnable.mm();
//			}
//		};
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
			boolean superperm = false, clock = false, clockIP = false, clockRe = false, mm = false;
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
			if (player.hasPermission("realclock.reload")){
				clockRe = true;
			}
			if (player.hasPermission("realclock.mm.toggle")){
				mm = true;
			}
			
				//Check for base clock command
			if ((clock || superperm) && cmd.getName().equalsIgnoreCase("realclock") && args.length == 0){					
				new Thread(clockRunnable, "RealClock Clock CMD").start();
				return true;
			}
				
				//Check for sub commands
			if (cmd.getName().equalsIgnoreCase("realclock") && args.length > 0){
				
				if((clockIP || superperm) && args[0].equalsIgnoreCase("ip")){
					if (player.getAddress().getHostString().equals("127.0.0.1")){
						ChatHandler.sendPlayer(player, Configuration.chatcolor, Strings.usegoogle);
					}else{
						ChatHandler.sendPlayer(player, Configuration.chatcolor, player.getAddress().getHostString());
					}
					return true;
				}else if(args[0].equalsIgnoreCase("ip")){
					ChatHandler.sendPlayer(player, "6", Strings.commanddeny);
					return true;
				}
				
				if(clockRe && args[0].equalsIgnoreCase("reload")){
					ConfigHandler.reload();
					ChatHandler.sendPlayer(player, Configuration.chatcolor, Strings.reloadComplete);
					return true;
				}else if(args[0].equalsIgnoreCase("reload")){
					ChatHandler.sendPlayer(player, "6", Strings.commanddeny);
					return true;
				}
				
				if(mm && args[0].equalsIgnoreCase("mm")){
//					new Thread(mmrunnable, "RealClock MM CMD").start();
					mm(player);
					return true;
				}else if(args[0].equalsIgnoreCase("mm")){
					ChatHandler.sendPlayer(player, "6", Strings.commanddeny);
					return true;
				}
			}
			
			return false;
			
		}else if (sender instanceof ConsoleCommandSender && args.length != 0){
			
			console = (ConsoleCommandSender) sender;
			if(args[0].equalsIgnoreCase("reload")){
				ConfigHandler.reload();
				ChatHandler.sendConsole(console, Configuration.chatcolor, Strings.reloadComplete);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("mm")){
				mm(console);
				return true;
			}
			
			sender.sendMessage(Strings.commandconsole);
			return true;
			
		}else{
			//If anything other then a player or the console sends the command
			sender.sendMessage(Strings.commandconsole);
			return true;
		}
	}
	
	private static void mm(CommandSender sender){
		if (!Plugin.mmenable){
//			ChatHandler.sendPlayer(ClockCommand.player, Configuration.chatcolor, Strings.mmenabling);
//	        //Bukkit.getServer().broadcastMessage(Strings.mmenabling);
//	        try {
//	            Thread.sleep(1000);
//	        } catch (InterruptedException e) {
//	            LogHandler.warning("InterruptedException", e);
//	        }
//	        for (int i = 10; i > 0; i--) {
//	                //Bukkit.getServer().broadcastMessage(Messages.colour(format(parent.getCountdownMessage(), i)));
//	            try {
//	                Thread.sleep(1000);
//	            } catch (InterruptedException e) {
//	            	LogHandler.warning("InterruptedException", e);
//	            }
//	            if (i == 1) {
	                Plugin.kick();
	                Plugin.mmenable = true;
	                JavaPlugin.getPlugin(Plugin.class).getConfig().set(Configuration.path_mm, true);
	                try {
						ConfigHandler.save();
					} catch (IOException e) {
						LogHandler.warning("IOException", e);
					}
	                sender.sendMessage("§" + Configuration.chatcolor + Strings.mmenabled);
	                //Bukkit.getServer().broadcastMessage(Strings.mmenabled);
//	            }
//	        }
		}else if (Plugin.mmenable){
			Plugin.mmenable = false;
			JavaPlugin.getPlugin(Plugin.class).getConfig().set(Configuration.path_mm, false);
            try {
				ConfigHandler.save();
			} catch (IOException e) {
				LogHandler.warning("IOException", e);
			}
            sender.sendMessage("§" + Configuration.chatcolor + Strings.mmdisabled);
		}
	}
}
