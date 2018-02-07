/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.chat;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Handles various chat related methods
 * 
 * @author taytom258
 *
 */
public class ChatHandler {

	/**
	 * Sends message to a player with specified color
	 * 
	 * @param player
	 *            the player
	 * @param color
	 *            number in minecraft color code
	 * @param message
	 *            to send
	 * @see Player
	 */
	public static void sendPlayer(Player player, String color, String message) {
		player.sendMessage("§" + color + message);
	}

	/**
	 * Sends a message to the console with specified color
	 * 
	 * @param console
	 *            the console
	 * @param color
	 *            number in minecraft color code
	 * @param message
	 *            to send
	 * @see ConsoleCommandSender
	 */
	public static void sendConsole(ConsoleCommandSender console, String color, String message) {
		console.sendMessage("§" + color + message);
	}
}
