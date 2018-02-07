/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.lang.StringUtils;

import com.maxmind.geoip2.exception.GeoIp2Exception;

import net.ddns.taytom258.SpigotRealClockPlugin.chat.ChatHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;
import net.ddns.taytom258.SpigotRealClockPlugin.geoIP.GeoIP;
import net.ddns.taytom258.SpigotRealClockPlugin.http.TimeZoneDB;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Sections of clock command that need to run in a separate thread
 * 
 * @author taytom258
 * @see Runnable
 * @see Thread
 *
 */
public class ClockRunnable {

	/**
	 * Lookup portion of clock command
	 * 
	 */
	private static void lookup() {

		String latlng = null, lat = null, lng = null, time = "Error";

		// Run GeoIP Lookup logic
		try {
			if (ClockCommand.player.getAddress().getHostString().equals("127.0.0.1")
					|| ClockCommand.player.getAddress().getHostString().startsWith("172.16")
					|| ClockCommand.player.getAddress().getHostString().startsWith("10")
					|| ClockCommand.player.getAddress().getHostString().startsWith("192.168")) {
				latlng = GeoIP.getLocation(ClockCommand.getIpAddress());
			} else {
				latlng = GeoIP.getLocation(ClockCommand.player.getAddress().getHostString());
			}
		} catch (IOException e) {
			LogHandler.warning("", e);
		} catch (GeoIp2Exception e) {
			LogHandler.warning(Strings.apierror);
		}

		// Process GeoIP Output
		String[] newlatlng = StringUtils.split(latlng, ';');
		for (int i = 0; i < newlatlng.length; i++) {
			if (i == 0) {
				lat = newlatlng[i];
			} else {
				lng = newlatlng[i];
			}
		}

		// Get time from lat & lng
		try {
			time = TimeZoneDB.sendRequest(lat, lng);
		} catch (URISyntaxException e) {
			LogHandler.warning("URI", e);
		} catch (Exception e) {
			LogHandler.warning("TimeZoneDB", e);
		}
		ChatHandler.sendPlayer(ClockCommand.player, Configuration.chatcolor, time);
	}

	/**
	 * Clock command cooldown
	 * 
	 */
	// TODO Use this as the timer for when to fire the save and backup commands
	public static void clock() {
		boolean cooldown = false;
		int cooldownTime = 60;
		// Command cooldown
		if (!ClockCommand.bypass) {
			if (Configuration.cool >= 2) {
				cooldownTime = Configuration.cool;
			} else if (Configuration.cool < 2) {
				cooldownTime = 2;
			}
			// Time in seconds
			if (ClockCommand.cooldowns.containsKey(ClockCommand.player.getName())) {
				long secondsLeft = ((ClockCommand.cooldowns.get(ClockCommand.player.getName()) / 1000) + cooldownTime)
						- (System.currentTimeMillis() / 1000);
				if (secondsLeft > 0) {
					// Still cooling down
					ChatHandler.sendPlayer(ClockCommand.player, Configuration.chatcolor,
							"You can not use that command for another " + secondsLeft + " seconds!");
					cooldown = true;
				} else {
					// Cooldown has expired, save new cooldown
					ClockCommand.cooldowns.put(ClockCommand.player.getName(), System.currentTimeMillis());
				}
			} else {
				// Cooldown not found, save new cooldown
				ClockCommand.cooldowns.put(ClockCommand.player.getName(), System.currentTimeMillis());
			}
		}
		if (!cooldown) {
			ChatHandler.sendPlayer(ClockCommand.player, Configuration.chatcolor, "Loading...");
			lookup();
		}
	}
}