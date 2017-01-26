/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;

/**
 * Handles various basic command methods for plugin
 * 
 * @author taytom258
 *
 */
public class CommandHandler {

	/**
	 * Register command. Must be pre-loaded with plugin.yml before register.
	 * 
	 * @param command
	 *            string, command name (as listed in plugin.yml)
	 * @param ex
	 *            commandExecutor, class that implements CommandExecutor
	 * @return If command was found and registered
	 * @see CommandExecutor
	 */
	public static boolean registerCommand(String command, CommandExecutor ex) {

		JavaPlugin plugin = JavaPlugin.getPlugin(Plugin.class);

		if (plugin.getCommand(command) != null) {
			plugin.getCommand(command).setExecutor(ex);
			return true;
		}

		return false;
	}
}
