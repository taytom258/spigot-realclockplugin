/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Listener to check if player has commands on join and issues chat message
 * 
 * @author taytom258
 *
 */
public class JoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event){
		
		if(event.getPlayer().hasPermission("realclock.bypass")){
			event.getPlayer().sendMessage(Strings.commandbypass);
		}
	}
}
