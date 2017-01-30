/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.backup;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;
import net.ddns.taytom258.SpigotRealClockPlugin.chat.ChatHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;

/**
 * @author taytom258
 *
 */
public class Backup {

	private static Runnable backupRunnable, saveRunnable, restartRunnable;
	private static int ticker = 0;
	// TODO
	// Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
	// "tm abc Testing...");

	public static void init() {

		backupRunnable = new Runnable() {

			public void run() {
				copyWorldFiles();

			}
		};
		saveRunnable = new Runnable() {

			public void run() {
				ChatHandler.sendConsole(Bukkit.getConsoleSender(), "9", "Saving world...");
				for (World worlds : Bukkit.getWorlds()) {
					worlds.save();
				}
				if (Plugin.tm) {
					Bukkit.getServer().dispatchCommand(
							Bukkit.getServer().getConsoleSender(),
							"tm abc §9Saving world...");
				}
			}
		};
		restartRunnable = new Runnable() {

			public void run() {
				ticker++;
				switch (ticker) {
					case 285: //say 15 min
						break;
					case 286: //say 10 min
						break;
					case 287: //say 5 min
						break;
					case 288:
						Bukkit.shutdown();
				}
			}
		};

		Bukkit.getServer().getScheduler().runTaskTimer(
				JavaPlugin.getPlugin(Plugin.class), backupRunnable, 1200 * 30,
				1200 * 60);
		Bukkit.getServer().getScheduler().runTaskTimer(
				JavaPlugin.getPlugin(Plugin.class), saveRunnable, 1200 * 15,
				1200 * 15);
		Bukkit.getServer().getScheduler().runTaskTimer(
				JavaPlugin.getPlugin(Plugin.class), restartRunnable, 1200 * 5,
				1200 * 5);
	}

	public static void copyWorldFiles() {
		List<World> worldList = Bukkit.getWorlds();
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);

		File depth = new File("backups" + "/" + year + "/" + month + "/" + day
				+ "/" + hour + "/" + min);

		if (!depth.exists()) {
			depth.mkdirs();
		}

		Bukkit.getServer().dispatchCommand(
				Bukkit.getServer().getConsoleSender(), "save-off");
		ChatHandler.sendConsole(Bukkit.getConsoleSender(), "9", "Backing up world...");
		if (Plugin.tm) {
			Bukkit.getServer().dispatchCommand(
					Bukkit.getServer().getConsoleSender(),
					"tm abc §9Backing up world...");
		}
		for (int i = 0; i < worldList.size(); i++) {
			File worldFolder = new File(
					worldList.get(i).getWorldFolder().toString());
			try {
				FileUtils.copyDirectoryToDirectory(worldFolder, depth);
			} catch (IOException e) {
				LogHandler.warning(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		Bukkit.getServer().dispatchCommand(
				Bukkit.getServer().getConsoleSender(), "save-on");
	}
}
