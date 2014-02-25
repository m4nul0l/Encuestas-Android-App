package com.dssd.encuestas.sync;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class EncuestasSyncHelper {
	
	public static final String serverBaseUrl = "http://www.loyalmaker.com/services/device/";
	
	private static boolean verifyResult(InputStream openStream) throws XmlPullParserException, IOException {
		XmlPullParser xmlParser = Xml.newPullParser();
		xmlParser.setInput(openStream, "UTF-8");
		
		xmlParser.nextTag();
		xmlParser.require(XmlPullParser.START_TAG, null, "xml");
		
		while (xmlParser.next() != XmlPullParser.END_TAG) {
			
			if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = xmlParser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("result")) {
	            //entries.add(readEntry(parser));
	        	xmlParser.next();
	        	String res = xmlParser.getText();
	        	
	        	if(res.equalsIgnoreCase("ok"))
	        		return true;
	        }
	    }
		
		return false;
	}
	
	public static boolean registerUser(String user) {
		try {
			URL u = new URL(serverBaseUrl + "registerUser/" + user);
			HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
			
			try {
				InputStream openStream = urlConnection.getInputStream();
				
				return verifyResult(openStream);
				
			} finally {
				urlConnection.disconnect();
			}
		    
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean sendInfoUser(String user, String info) {
		try {
			URL u = new URL(serverBaseUrl + "sendInfoUser/" + user);
			HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
			
			try {
			     urlConnection.setDoOutput(true);
			     urlConnection.setChunkedStreamingMode(0);
			     
			     OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
			     OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
			     //writeStream(out);
			     osw.write(URLEncoder.encode("info", "UTF-8"));
			     osw.write("=");
			     osw.write(URLEncoder.encode(info, "UTF-8"));
			     osw.flush();

			     //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			     //readStream(in);
				InputStream openStream = urlConnection.getInputStream();
				
				return verifyResult(openStream);
				
			} finally {
				urlConnection.disconnect();
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
