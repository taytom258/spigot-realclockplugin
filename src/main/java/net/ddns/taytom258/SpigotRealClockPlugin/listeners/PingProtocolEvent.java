/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.listeners;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import net.ddns.taytom258.SpigotRealClockPlugin.Plugin;
import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;

/**
 * Handles changing the status screen for a server under certain conditions
 * 
 * @author taytom258
 *
 */
public class PingProtocolEvent {

	private static ProtocolManager proto;
	private static Plugin plugin;
	private static File iconfile;

	/**
	 * Constructor and initilization for class
	 */
	public PingProtocolEvent() {
		proto = ProtocolLibrary.getProtocolManager();
		plugin = JavaPlugin.getPlugin(Plugin.class);
		String iconpath = plugin.getDataFolder().toString()
				+ "/server-icon.png";
		iconfile = new File(iconpath);
	}

	/**
	 * Add ping packet listener to change status screen
	 */
	public void addPingResponsePacketListener() {
		try {
			proto.addPacketListener(new PacketAdapter(PacketAdapter
					.params(plugin,
							new PacketType[]{
									PacketType.Status.Server.OUT_SERVER_INFO})
					.serverSide().gamePhase(GamePhase.BOTH)
					.listenerPriority(ListenerPriority.HIGHEST).optionAsync()) {
				@Override
				public void onPacketSending(PacketEvent event) {
					try {
						if (Plugin.mmenable) {

							WrappedServerPing ping = event.getPacket()
									.getServerPings().getValues().get(0);

							String pingMessage = "Maintenance";
							ping.setVersionProtocol(-1);
							ping.setVersionName(pingMessage);

							if (iconfile.exists()) {

								WrappedServerPing.CompressedImage favicon = WrappedServerPing.CompressedImage
										.fromPng(new FileInputStream(iconfile));
								ping.setFavicon(favicon);

							}

							event.getPacket().getServerPings().getValues()
									.set(0, ping);

						}

					} catch (Exception e) {
						LogHandler.warning("Exception", e);
					}
				}
			});
		} catch (Exception e) {
			LogHandler.warning("Exception", e);
		}
	}
}
