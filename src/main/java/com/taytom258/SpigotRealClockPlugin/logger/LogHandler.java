/**
 * 
 */
package com.taytom258.SpigotRealClockPlugin.logger;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import com.taytom258.SpigotRealClockPlugin.Plugin;
import com.taytom258.SpigotRealClockPlugin.config.Configuration;

/**
 * Loghandler for printing errors and messages to server console. Automatically
 * adds plugin name, as stated in plugin.yml, to all messages.
 * 
 * @author taytom258
 * @see JavaPlugin#getLogger()
 *
 */
public class LogHandler {

	public static Logger log;

	/**
	 * Initialize logger. Should be called in onLoad method.
	 * 
	 * @see JavaPlugin#onLoad()
	 */
	public static void init() {

		log = JavaPlugin.getPlugin(Plugin.class).getLogger();
	}

	/**
	 * Send info message to console.
	 * 
	 * @param msg
	 *            string, to send
	 */
	public static void info(String msg) {
		if (Configuration.log.equalsIgnoreCase("info")) {
			log.info(msg);
		}
	}

	/**
	 * Send info message to console regardless of config setting.
	 * 
	 * @param msg
	 *            string, to send
	 */
	public static void bypass(String msg) {
		log.info(msg);
	}

	/**
	 * Send warning message, with exception information, to the console
	 * 
	 * @param error
	 *            string, message to send
	 * @param e
	 *            exception, exception thrown
	 * 
	 * @see Exception
	 * @see Exception#getCause()
	 * @see Exception#getClass()
	 * 
	 */
	public static void warning(String error, Exception e) {

		if (Configuration.log.equalsIgnoreCase("info") || Configuration.log.equalsIgnoreCase("warning")) {
			String msg = "Exception ".concat(e.getCause().toString()).concat(" by ").concat(e.getClass().toString());

			log.warning(msg);
			if (error != "") {
				log.warning(error);
			}
			e.printStackTrace();
		}
	}

	/**
	 * Send warning message to console
	 * 
	 * @param error
	 *            string, message to send
	 */
	public static void warning(String error) {

		if (Configuration.log.equalsIgnoreCase("info") || Configuration.log.equalsIgnoreCase("warning")) {
			log.warning(error);
		}
	}

	/**
	 * Send severe message, with exception information, to the console Also gives
	 * option to disable plugin.
	 * 
	 * @param error
	 *            string, message to send
	 * @param disable
	 *            boolean, should plugin be disabled after message is sent
	 * @param e
	 *            exception, exception thrown
	 * 
	 * @see Exception
	 * @see Exception#getCause()
	 * @see Exception#getClass()
	 * @see Server#getPluginManager()
	 * 
	 */
	public static void severe(String error, boolean disable, Exception e) {

		if (Configuration.log.equalsIgnoreCase("info") || Configuration.log.equalsIgnoreCase("warning")
				|| Configuration.log.equalsIgnoreCase("severe")) {
			String msg = "Exception ".concat(e.getCause().toString()).concat(" by ").concat(e.getClass().toString());

			log.severe(msg);
			e.printStackTrace();

			if (disable) {
				JavaPlugin.getPlugin(Plugin.class).getServer().getPluginManager()
						.disablePlugin(JavaPlugin.getPlugin(Plugin.class));
			}
		}
	}

	/**
	 * Send warning message to console with option to disable plugin
	 * 
	 * @param error
	 *            string, message to send
	 * @param disable
	 *            boolean, should plugin be disabled after message is sent
	 * 
	 * @see Server#getPluginManager()
	 */
	public static void severe(String error, boolean disable) {

		if (Configuration.log.equalsIgnoreCase("info") || Configuration.log.equalsIgnoreCase("warning")
				|| Configuration.log.equalsIgnoreCase("severe")) {
			log.severe(error);

			if (disable) {
				JavaPlugin.getPlugin(Plugin.class).getServer().getPluginManager()
						.disablePlugin(JavaPlugin.getPlugin(Plugin.class));
			}
		}
	}
}
