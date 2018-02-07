/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.geoIP;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;

import net.ddns.taytom258.SpigotRealClockPlugin.logger.LogHandler;
import net.ddns.taytom258.SpigotRealClockPlugin.reference.Strings;

/**
 * Handles reading of GeoLite databases <br>
 * <br>
 * This product includes GeoLite2 data created by MaxMind, available from
 * <a href="http://www.maxmind.com">http://www.maxmind.com</a>.
 * 
 * @author taytom258
 *
 */
public class GeoIP {

	private static DatabaseReader reader;
	private static File database;

	/**
	 * Initialize database reader
	 */
	public static void init() {

		database = new File(Strings.geodb);

		try {
			reader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();
		} catch (IOException e) {
			LogHandler.warning("", e);
		}
	}

	/**
	 * De-initialize database reader
	 */
	public static void deinit() {

		try {
			reader.close();
		} catch (IOException e) {
			LogHandler.warning("", e);
		}
		reader = null;
		database = null;
	}

	/**
	 * Get latitude and longitude from GeoLite Database with IP address
	 * 
	 * @param IP
	 *            string, IPv4 or IPv6 address for lookup
	 * @return Latitude and Longitude in format: "Latitude;Longitude"
	 * @see Reader
	 * @throws IOException
	 * @throws GeoIp2Exception
	 */
	public static String getLocation(String IP) throws IOException, GeoIp2Exception {

		InetAddress ipAddress = null;
		ipAddress = InetAddress.getByName(IP);

		CityResponse response = null;
		response = reader.city(ipAddress);

		Location location = response.getLocation();

		String LatLong = String.format("%s;%s", location.getLatitude(), location.getLongitude());

		return LatLong;
	}
}
