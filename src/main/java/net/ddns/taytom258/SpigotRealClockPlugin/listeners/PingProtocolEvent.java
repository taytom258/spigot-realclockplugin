package net.ddns.taytom258.SpigotRealClockPlugin.listeners;

import java.io.File;
import java.io.FileInputStream;

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

public class PingProtocolEvent {
	
	private static ProtocolManager proto;
	private static Plugin plugin;
	private static File iconfile;
	
  public PingProtocolEvent(){
	  proto = ProtocolLibrary.getProtocolManager();
	  plugin = Plugin.getPlugin(Plugin.class);
	  String iconpath = plugin.getDataFolder().toString() + "/server-icon.png";
      iconfile = new File(iconpath);
  }
	
  public void addPingResponsePacketListener()
  {
    try
    {
      proto.addPacketListener(new PacketAdapter(PacketAdapter.params(plugin, new PacketType[] { PacketType.Status.Server.OUT_SERVER_INFO }).serverSide().gamePhase(GamePhase.BOTH).listenerPriority(ListenerPriority.HIGHEST).optionAsync())
      {
        public void onPacketSending(PacketEvent event)
        {
          try
          {
        	  if(Plugin.mmenable) {
        		  
            WrappedServerPing ping = (WrappedServerPing)event.getPacket().getServerPings().getValues().get(0);
            
            String pingMessage = "Maintenance";
            ping.setVersionProtocol(-1);
            ping.setVersionName(pingMessage);
            //ping.setPlayersMaximum(0);
            
            //String motd = BukkitPlugin.getInstance().getMotd().replaceAll("%newline", "\n");
            
            //ping.setMotD(motd);
            
            if (iconfile.exists())
            {
            	
              WrappedServerPing.CompressedImage favicon = WrappedServerPing.CompressedImage.fromPng(new FileInputStream(iconfile));
              ping.setFavicon(favicon);
              
            }
            
            event.getPacket().getServerPings().getValues().set(0, ping);
            
        	  }
            
          }
          catch (Exception e)
          {
            LogHandler.warning("Exception", e);
          }
        }
      });
    }
    catch (Exception e)
    {
    	LogHandler.warning("Exception", e);
    }
  }
}
