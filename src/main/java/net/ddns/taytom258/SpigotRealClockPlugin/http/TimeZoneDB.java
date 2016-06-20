/**
 * 
 */
package net.ddns.taytom258.SpigotRealClockPlugin.http;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;

import net.ddns.taytom258.SpigotRealClockPlugin.config.Configuration;

/**
 * Manages connections to TimeZone DB API using a private API key from
 * <a href="https://timezonedb.com/">TimeZoneDB</a>
 * 
 * @author taytom258
 *
 */
public class TimeZoneDB {

	public static String sendRequest(String lat, String lng) throws Exception{
		
		CloseableHttpResponse response = null;
		//System.out.println(buildURI(lat, lng).toString());
		
		//Open HTTP client and build GET request
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(buildURI(lat, lng));
		
		//Execute request and receive response
		response = httpclient.execute(httpget);
		
		//Process resulting XML response
		String xml = processReponse(response);
		response.close();
		
		//Convert timestamp to normal time
		return convertTime(xml);
		
	}
	
	/**
	 * Build URI to send to the HTTP server
	 * 
	 * @param lat parameter
	 * @param lng parameter
	 * @return complete URI
	 * @throws URISyntaxException
	 */
	private static URI buildURI(String lat, String lng) throws URISyntaxException  {

		String api = Configuration.api;
		
		URI uri = new URIBuilder().setScheme("http")
				.setHost("api.timezonedb.com").setPath("/").setParameter("key", api)
				.setParameter("lat", lat)
				.setParameter("lng", lng).build();
		return uri;
	}
	
	/**
	 * Process XML response from HTTP server
	 * 
	 * @param response XML response
	 * @return String representation of the XML document
	 * @throws Exception multiple exceptions
	 */
	private static String processReponse(CloseableHttpResponse response) throws Exception{
		
		InputStream in = response.getEntity().getContent();
		DocumentBuilderFactory dbf = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(in);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc),new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r","");
		in.close();
		writer.close();
		return output;
	}
	
	/**
	 * Convert UNIX timestamp to normal date and time
	 * 
	 * @param input XML string
	 * @return Normal date and time
	 */
	private static String convertTime(String input){
		
		String timestamp = StringUtils.substringBetween(input,"<timestamp>", "</timestamp>");
		//String timezone = StringUtils.substringBetween(input,"<abbreviation>", "</abbreviation>");
		Date date = new Date(Long.parseLong(timestamp) * 1000L); 
		SimpleDateFormat sdf = new SimpleDateFormat(Configuration.timeformat);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String datetime = sdf.format(date);
		date = null;
		sdf = null;
		return datetime;
	}
}
