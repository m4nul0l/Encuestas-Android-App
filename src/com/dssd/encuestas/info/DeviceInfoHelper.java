package com.dssd.encuestas.info;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.BatteryManager;
import android.util.Xml;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class DeviceInfoHelper {
	
    private static Context context;
    
    private DeviceInfoHelper() {
	}
    
	private static class DeviceInfoHelperHolder {
		public static final DeviceInfoHelper INSTANCE = new DeviceInfoHelper();
	}
	
    public static DeviceInfoHelper getInstance(Context context) {
    	synchronized (DeviceInfoHelperHolder.INSTANCE) {
			if(DeviceInfoHelper.context == null)
				DeviceInfoHelper.context = context.getApplicationContext();
		}
    	return DeviceInfoHelperHolder.INSTANCE;
    }
    
    public String getDeviceInfoXML() {
    	String xmlStr = null;
    	try {
        	XmlSerializer xmlSerializer = Xml.newSerializer();
        	StringWriter sw = new StringWriter();
			xmlSerializer.setOutput(sw);
			xmlSerializer.startDocument(null, null);
			xmlSerializer.startTag(null, "device");
			
			Map<String, String> deviceMap = getDeviceInfoMap();
			for(Map.Entry<String, String> entry : deviceMap.entrySet()){
				xmlSerializer.startTag(null, entry.getKey());
				xmlSerializer.text(entry.getValue());
				xmlSerializer.endTag(null, entry.getKey());
			}
			
			xmlSerializer.endTag(null, "device");
			xmlSerializer.endDocument();
			xmlSerializer.flush();
			
			xmlStr = sw.toString();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return xmlStr;
    }
    
    public String getDeviceInfoJSON() {
    	JSONObject json = new JSONObject(getDeviceInfoMap());
    	return json.toString();
    }
    
    public Map<String, String> getDeviceInfoMap() {
    	HashMap<String, String> deviceMap = new HashMap<String, String>();
    	
    	deviceMap.put("api-level", String.valueOf(getAPILevel()));
    	deviceMap.put("processor-architecture", getProcessorArchitecture());
    	deviceMap.put("brand", getBrand());
    	deviceMap.put("device", getDevice());
    	deviceMap.put("manufacturer", getManufacturer());
    	deviceMap.put("model", getModel());
    	deviceMap.put("product", getProduct());
    	deviceMap.put("serial", getProduct());
    	deviceMap.put("display-size", getDisplaySizeString());
    	
    	return deviceMap;
    }
    
    public int getAPILevel() {
    	return android.os.Build.VERSION.SDK_INT;
    }
    
    public String getProcessorArchitecture() {
    	return System.getProperty("os.arch");
    }
    
    public String getBrand() {
    	return android.os.Build.BRAND;
    }
    
    public String getDevice() {
    	return android.os.Build.DEVICE;
    }
    
    public String getManufacturer() {
    	return android.os.Build.MANUFACTURER;
    }
    
    public String getModel() {
    	return android.os.Build.MODEL;
    }
    
    public String getProduct() {
    	return android.os.Build.PRODUCT;
    }
    
    public String getSerial() {
    	if(android.os.Build.VERSION.SDK_INT >= 9)
    		return getSerial9();
    	else
    		return "";
    }
    
    @TargetApi(9)
    public String getSerial9() {
   		return android.os.Build.SERIAL;
    }
    
    public String getDisplaySizeString() {
    	Point size = getDisplaySize();
    	return "" + size.x + "x" + size.y;
    }
    
    public Point getDisplaySize() {
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	
    	Point size;
    	
    	if(android.os.Build.VERSION.SDK_INT >= 17)
    		size = getDisplay17(display);
    	else if(android.os.Build.VERSION.SDK_INT >= 13)
    		size = getDisplay13(display);
    	else
    		size = getDisplay8(display);
    	
		/* Always get the 0 rotation size */
		int rotation = display.getRotation();
		if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
			int tmp = size.x;
			size.x = size.y;
			size.y = tmp;
		}
    	
    	return size;
    }
    
    @SuppressWarnings("deprecation")
	@TargetApi(8)
    public Point getDisplay8(Display display) {
		int width = display.getWidth();
		int heigth = display.getHeight();
		Point size = new Point(width, heigth);
		return size;
    }
    
    @TargetApi(13)
    public Point getDisplay13(Display display) {
		Point size = new Point();
		display.getSize(size);
    	return size;
    }
    
    @TargetApi(17)
    public Point getDisplay17(Display display) {
		Point size = new Point();
		display.getRealSize(size);
    	return size;
    }
    
    public float getBatteryLevel() {
    	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    	Intent batteryStatus = context.registerReceiver(null, ifilter);
    	
    	int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    	int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

    	float batteryPct = level / (float)scale;
    	return batteryPct;
    }
}
