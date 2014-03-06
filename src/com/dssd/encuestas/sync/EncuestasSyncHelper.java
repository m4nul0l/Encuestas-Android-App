package com.dssd.encuestas.sync;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dssd.encuestas.DBHelper;
import com.dssd.encuestas.webservices.ItemsResult;
import com.dssd.encuestas.webservices.Result;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class EncuestasSyncHelper {
	
	public static final String action_sync_started = "com.dssd.encuestas.action.start_sync";
	public static final String action_sync_end = "com.dssd.encuestas.action.end_sync";
	
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
	
	/*public static <T extends ItemsResult<U>, U> boolean sincronizarTabla(String serviceName, String device, Context context, Class<T> resultType, Class<U> elementType) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
		
		//String uri = serverBaseUrl + "registerUser/" + user;
		T tipos = restTemplate.getForObject(serverBaseUrlTemplate2, resultType,
				serviceName, device);
		
		boolean result = tipos.isOk();
		
		if(tipos.isOk()) {
			
			DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
			try {
				TableUtils.clearTable(dbHelper.getConnectionSource(), elementType);
				
				Dao<U, Long> dao = dbHelper.getDao(elementType);
				
				for(U tipo : tipos.getItems()) {
					dao.create(tipo);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			} finally {
				OpenHelperManager.releaseHelper();
			}
		}
		
		return result;
	}*/
	
	/*public static boolean sincronizarTiposPreguntas(String device, Context context) {
		boolean res = sincronizarTabla("tipospreguntas", device, context,
				TiposPreguntas.class, TipoPregunta.class);
		return res;
	}
	
	public static boolean sincronizarTiposPreguntasOpciones(String device, Context context) {
		boolean res = sincronizarTabla("tipospreguntas_opciones", device, context,
				TiposPreguntasOpciones.class, TipoPreguntaOpcion.class);
		return res;
	}*/
	
	public static <T extends ItemsResult<U>, U> boolean sincronizarTabla(String serviceName, String device, Context context,
			Class<T> resultType, Class<U> elementType) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
		
		//String uri = serverBaseUrl + "registerUser/" + user;
		T itemsResult = restTemplate.getForObject(serverBaseUrlTemplate2, resultType,
				serviceName, device);
		
		boolean result = itemsResult.isOk();
		
		if(itemsResult.isOk()) {
			
			DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
			try {
				//TableUtils.clearTable(dbHelper.getConnectionSource(), TiposPreguntas.class);
				//Dao<U, Long> dao = dbHelper.getDao(elementType);
				
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				itemsResult.deleteAllFromDatabase(db);
				itemsResult.insertIntoDatabase(db);
				
			/*} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;*/
			} finally {
				OpenHelperManager.releaseHelper();
			}
		}
		
		return result;
	}
	
	
	/*public static boolean sincronizarEncuestas(String device, Context context) {
		
		ItemsResult<Encuesta> ir = new ItemsResult<Encuesta>();
		
		boolean res = sincronizarTabla("encuestas", device, context,
				ItemsResult.class, Encuesta.class);
		return res;
	}*/
	
	/*
	public static boolean sincronizarTiposPreguntas(String user, Context context) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
		
		//String uri = serverBaseUrl + "registerUser/" + user;
		TiposPreguntas tipos = restTemplate.getForObject(serverBaseUrlTemplate2, TiposPreguntas.class,
				"tipospreguntas", user);
		
		boolean result = tipos.isOk();
		
		if(tipos.isOk()) {
			
			DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
			try {
				TableUtils.clearTable(dbHelper.getConnectionSource(), TipoPregunta.class);
				
				Dao<TipoPregunta, Long> dao = dbHelper.getDao(TipoPregunta.class);
				
				for(TipoPregunta tipo : tipos.getItems()) {
					dao.create(tipo);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			} finally {
				OpenHelperManager.releaseHelper();
			}
		}
		
		return result;
	}
	*/
}
