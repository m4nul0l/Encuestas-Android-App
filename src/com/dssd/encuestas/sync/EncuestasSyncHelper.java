package com.dssd.encuestas.sync;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.dssd.encuestas.webservices.Result;

public class EncuestasSyncHelper {
	
	public static final String serverBaseUrl = "http://www.loyalmaker.com/services/device/";
	public static final String serverBaseUrlTemplate1 = "http://www.loyalmaker.com/services/device/{service}";
	public static final String serverBaseUrlTemplate2 = "http://www.loyalmaker.com/services/device/{service}/{data}";
	
	public static boolean registerUser(String user) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
		
		//String uri = serverBaseUrl + "registerUser/" + user;
		Result result = restTemplate.getForObject(serverBaseUrlTemplate2, Result.class,
				"registerUser", user);
		return result.isOk();
	}
	
	public static boolean sendInfoUser(String user, String info) {
		
		RestTemplate restTemplate = new RestTemplate(true);
		
		//String uri = serverBaseUrl + "sendInfoUser/" + user;
		MultiValueMap<String, String> content = new LinkedMultiValueMap<String, String>(2);
		content.add("info", info);
		
		Result result = restTemplate.postForObject(serverBaseUrlTemplate2, content, Result.class,
				"sendInfoUser", user);
		return result.isOk();
	}
}
