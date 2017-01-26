/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.listeners;

import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;

/**
 * Handles various basic listener methods for plugin
 * 
 * @author taytom258
 *
 */
public class ListenerHandler {

	/**
	 * Register various listeners
	 * 
	 * @param listen
	 *            class that implements Listener
	 */
	public static void registerListener(Listener listen) {

		Server server = JavaPlugin.getPlugin(Plugin.class).getServer();
		JavaPlugin plugin = JavaPlugin.getPlugin(Plugin.class);

		server.getPluginManager().registerEvents(listen, plugin);

	}
}
