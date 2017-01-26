package net.ddns.taytom258.SpigotRealClockPlugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import net.ddns.taytom258.SpigotRealClockPlugin.commands.ClockCommand;
import net.ddns.taytom258.SpigotRealClockPlugin.commands.CommandHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.ConfigHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.geoIP.GeoIP;
import net.ddns.taytom258.SpigotRealClockPlugin.listeners.JoinListener;
import net.ddns.taytom258.SpigotRealClockPlugin.listeners.ListenerHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.listeners.PingProtocolEvent;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Main plugin class
 * 
 * @author taytom258
 * @see JavaPlugin
 *
 */
public class Plugin extends JavaPlugin {

	public static String ver;
	public static boolean proto = false, permex = false;
	public static boolean mmenable = false;

	@Override
	public void onLoad() {

		// Plugin properties
		ver = "1.2";

		// Log Initialization
		LogHandler.init();

		// Config Loader
		ConfigHandler.init();
		ConfigHandler.checkConfig();
	}

	@Override
	public void onEnable() {

		// GeoDB Checker & Initializer
		File db = new File(Strings.geodb);
		if (!db.exists()) {
			LogHandler.warning(Strings.dberror);
			this.saveResource(Strings.geojar, true);
			GeoIP.init();
		} else {
			LogHandler.info(Strings.dbsuccess);
			GeoIP.init();
		}

		// Register & Initialize Commands
		ClockCommand.init();
		CommandHandler.registerCommand("realclock", new ClockCommand());

		// Dependency Checker
		permex = false;
		if (this.getServer().getPluginManager()
				.getPlugin("PermissionsEx") != null) {
			permex = true;
			LogHandler.info(Strings.permload);
		} else {
			LogHandler.warning(Strings.permloaderror);
		}
		proto = false;
		if (this.getServer().getPluginManager()
				.getPlugin("ProtocolLib") != null) {
			proto = true;
			LogHandler.info(Strings.protoload);
		} else {
			LogHandler.severe(Strings.protoloaderror, true);
			return;
		}

		// Register Listeners
		ListenerHandler.registerListener(new JoinListener());

		this.saveResource(Strings.icon, true);
		PingProtocolEvent listener = new PingProtocolEvent();
		listener.addPingResponsePacketListener();

		// TimeZoneDB Checker
		if (Configuration.api.equalsIgnoreCase("InsertKeyHere")
				|| Configuration.api.equalsIgnoreCase("")) {
			LogHandler.severe(Strings.apierror, true);
			return;
		}

		// Metrics Loader
		Metrics m;
		try {
			m = new Metrics(this);
			m.start();
		} catch (IOException e) {
			LogHandler.warning("", e);
		}

	}

	@Override
	public void onDisable() {

		GeoIP.deinit();
		ClockCommand.deinit();
	}

	/**
	 * Kicks all online players without specific permission
	 */
	public static void kick() {
		for (Player player : Bukkit.getOnlinePlayers()) {

			if (player.isOp() || player.hasPermission("realclock.mm.bypass")) {
				return;
			} else {
				player.kickPlayer("§cServer undergoing maintenance");
			}
		}
	}
}
