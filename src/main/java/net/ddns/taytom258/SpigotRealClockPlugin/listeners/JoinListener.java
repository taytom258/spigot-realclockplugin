/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Listener to check if player has commands on join and issues chat message
 * 
 * @author taytom258
 *
 */
public class JoinListener implements Listener {

	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerJoin (PlayerLoginEvent event){
		if (Plugin.mmenable && !event.getPlayer().hasPermission("realclock.mm.bypass")){
			event.setKickMessage(Strings.mmenabledkick);
			event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			return;
		}
		if(event.getPlayer().hasPermission("realclock.bypass")){
			event.getPlayer().sendMessage(Strings.commandbypass);
		}
	}
}
