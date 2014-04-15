package com.dssd.encuestas.sync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.dssd.encuestas.DBHelper;
import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.datos.Encuestado;
import com.dssd.encuestas.datos.Respuesta;
import com.dssd.encuestas.datos.TipoPreguntaOpcion;
import com.dssd.encuestas.webservices.ItemsResult;
import com.dssd.encuestas.webservices.Result;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class EncuestasSyncHelper {
	
	public static final String action_sync_started = "com.dssd.encuestas.action.start_sync";
	public static final String action_sync_end = "com.dssd.encuestas.action.end_sync";
	
	public static final String serverBaseUrl = "http://www.loyalmaker.com/services/device/";
	public static final String serverBaseUrlTemplate1 = "http://www.loyalmaker.com/services/device/{service}";
	public static final String serverBaseUrlTemplate2 = "http://www.loyalmaker.com/services/device/{service}/{data}";
	
	public static final String assetsBaseUrl = "http://www.loyalmaker.com/assets/uploads/files/";
	public static final Uri assetsBaseUri = Uri.parse(assetsBaseUrl);
	
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
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				itemsResult.deleteAllFromDatabase(db);
				itemsResult.insertIntoDatabase(db);
			} finally {
				OpenHelperManager.releaseHelper();
			}
		}
		
		return result;
	}
	
	public static void sincronizarAssetsTiposPreguntasOpciones(Context context) {
		EncuestaManager em = new EncuestaManager(context);
		List<TipoPreguntaOpcion> opciones = em.getTiposPreguntasOpciones();
		em.close();
		String path = "ratings";
		for (TipoPreguntaOpcion opcion : opciones) {
			String imgDefaultName = opcion.getImagenDefault();
			if(imgDefaultName != null && imgDefaultName.trim().length() > 0)
				sincronizarAsset(path, imgDefaultName, context);
			
			String imgPresionadaName = opcion.getImagenPresionada();
			if(imgPresionadaName != null && imgPresionadaName.trim().length() > 0)
				sincronizarAsset(path, imgPresionadaName, context);
			
			String imgSeleccionadaName = opcion.getImagenSeleccionada();
			if(imgSeleccionadaName != null && imgSeleccionadaName.trim().length() > 0)
				sincronizarAsset(path, imgSeleccionadaName, context);
		}
	}
	
	public static void sincronizarAssetsEncuestas(Context context) {
		EncuestaManager em = new EncuestaManager(context);
		List<Encuesta> encuestas = em.getEncuestas();
		em.close();
		String path = "";
		for (Encuesta encuesta : encuestas) {
			String imgName = encuesta.getLogo();
			if(imgName != null && imgName.trim().length() > 0)
				sincronizarAsset(path, imgName, context);
			
			String imgFondoName = encuesta.getImagenFondo();
			if(imgFondoName != null && imgFondoName.trim().length() > 0)
				sincronizarAsset(path, imgFondoName, context);
		}
	}
	
	public static boolean sincronizarAsset(String path, String fileName, Context context) {
		Uri fullURL = assetsBaseUri.buildUpon().appendPath(path).appendPath(fileName).build();
		//Uri fullURL =  Uri.withAppendedPath(assetsBaseUri, assetURL);
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			URL url = new URL(fullURL.toString());
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			
			long newAssetUnixEpochDate = urlConnection.getHeaderFieldDate("Last-Modified", new Date().getTime());
			
			File dir = context.getDir(path, Context.MODE_PRIVATE);
			File file = new File(dir, fileName);
			boolean needsUpdate = true;
			if(file.exists()) {
				long lastModifiedUnixEpochDate = file.lastModified();
				if(lastModifiedUnixEpochDate != 0) {
					long dateDiff = newAssetUnixEpochDate - lastModifiedUnixEpochDate;
					if(dateDiff <= 1800000) { // 1800000 = 30 minutos
						// el newAsset es mas viejo que el de Android, no actualizar
						needsUpdate = false;
					}
				}
			}
			
			if(needsUpdate) {
				// update file
				inputStream = urlConnection.getInputStream();
				outputStream = new FileOutputStream(file);
				
				byte[] buf = new byte[16384];
				int tam = inputStream.read(buf);
				while(tam >= 0) {
					outputStream.write(buf, 0, tam);
					tam = inputStream.read(buf);
				}
				
				inputStream.close();
				outputStream.close();
			}
			
			return true;
		} catch (MalformedURLException e) {
			Log.e("EncuestasSyncHelper", "sincronizarAsset: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("EncuestasSyncHelper", "sincronizarAsset: " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			try {
				if(inputStream != null) inputStream.close();
				if(outputStream != null) outputStream.close();
			} catch (IOException e) {
				Log.e("EncuestasSyncHelper", "sincronizarAsset: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static Long nuevoEncuestado(String device, Long idEncuesta, Encuestado encuestado) {
		try {
			RestTemplate restTemplate = new RestTemplate(true);
			
			//String uri = serverBaseUrl + "sendInfoUser/" + user;
			MultiValueMap<String, String> content = new LinkedMultiValueMap<String, String>(3);
			content.add("idEncuesta", ""+idEncuesta);
			
			String nombre = encuestado.getNombre();
			if(nombre != null && nombre.trim().length() > 0)
				content.add("nombre", nombre);
			
			String email = encuestado.getEmail();
			if(email != null && email.trim().length() > 0)
				content.add("email", email);
			
			String telefono = encuestado.getTelefono();
			if(telefono != null && telefono.trim().length() > 0)
				content.add("telefono", telefono);
			
			Result result = restTemplate.postForObject(serverBaseUrlTemplate2, content, Result.class,
					"encuestado", device);
			return Long.valueOf(result.getResult());
		} catch(HttpClientErrorException e) {
			return null;
		}
		//return Long.valueOf(result.getIdEncuestado());
	}
	
	public static Long nuevaRespuesta(String device, Respuesta respuesta, Long idEncuestado) {
		RestTemplate restTemplate = new RestTemplate(true);
		
		//String uri = serverBaseUrl + "sendInfoUser/" + user;
		MultiValueMap<String, String> content = new LinkedMultiValueMap<String, String>(2);
		content.add("idEncuestado", ""+idEncuestado);
		content.add("idPregunta", ""+respuesta.getPregunta().get_id());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS", Locale.US);
		//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String newDate = sdf.format(respuesta.getFecha());
		content.add("fecha", newDate);
		
		content.add("respuesta", respuesta.getRespuesta());
		
		Result result = restTemplate.postForObject(serverBaseUrlTemplate2, content, Result.class,
				"respuesta", device);
		return Long.valueOf(result.getResult());
	}
	
	public static void sincronizarRespuestas(String device, Context context) {
		EncuestaManager em = new EncuestaManager(context);
		
		Long idEncuesta = null;
		
		List<Encuesta> list = em.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			idEncuesta = encuesta.get_id();
		}
		
		List<Encuestado> encuestados = em.getEncuestados();
		if(encuestados.size() > 0) {
			/* sincronizo las respuestas con la Web */
			for (Encuestado encuestado : encuestados) {
				Long idEncuestado = nuevoEncuestado(device, idEncuesta, encuestado);
				if(idEncuestado != null && idEncuestado > 0) {
					ForeignCollection<Respuesta> respuestas = encuestado.getRespuestas();
					for (Respuesta respuesta : respuestas) {
						EncuestaManager.refreshObject(em, respuesta);
						nuevaRespuesta(device, respuesta, idEncuestado);
					}
				}
			}
			
			/* Borro las respuestas de la tablet */
			try {
				Dao<Encuestado, Long> daoEncuestado = EncuestaManager.getDao(em, Encuestado.class);
				Dao<Respuesta, Long> daoRespuesta = EncuestaManager.getDao(em, Respuesta.class);
				
				for (Encuestado encuestado : encuestados) {
					ForeignCollection<Respuesta> respuestas = encuestado.getRespuestas();
					daoRespuesta.delete(respuestas);
				}
				daoEncuestado.delete(encuestados);
			} catch (SQLException e) {
				Log.e("EncuestasSyncHelper", "sincronizarRespuestas: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
}
