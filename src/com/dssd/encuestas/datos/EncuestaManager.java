package com.dssd.encuestas.datos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.dssd.encuestas.DBHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class EncuestaManager {
	
    private Context context;
    private DBHelper dbHelper;
    
	public EncuestaManager(Context context) {
		this.context = context;
	}
	
	private void initDBHelper() {
		if(dbHelper == null)
			dbHelper = OpenHelperManager.getHelper(this.context, DBHelper.class);
	}
	
	public static <T> List<T> getElements(EncuestaManager em, Class<T> elementType) {
		em.initDBHelper();
		try {
			Dao<T, Long> dao = em.dbHelper.getDao(elementType);
			List<T> list = dao.queryForAll();
			return list;
		} catch (SQLException e) {
			Log.e("EncuestaManager", "getElements: " + e.getLocalizedMessage());
			e.printStackTrace();
			Bugsnag.notify(e);
			return new ArrayList<T>(0);
		}
	}
	
	public List<Encuesta> getEncuestas() {
		return getElements(this, Encuesta.class);
	}
	
	public List<TipoPreguntaOpcion> getTiposPreguntasOpciones() {
		return getElements(this, TipoPreguntaOpcion.class);
	}
	
	public static <T> T refreshObject(EncuestaManager em, T object) {
		em.initDBHelper();
		try {
			Dao<T, Long> dao = em.dbHelper.getDao(object.getClass());
			dao.refresh(object);
			return object;
		} catch (SQLException e) {
			Log.e("EncuestaManager", "refreshTipoPregunta: " + e.getLocalizedMessage());
			e.printStackTrace();
			Bugsnag.notify(e);
			return null;
		}
	}
	
	public TipoPregunta refreshTipoPregunta(TipoPregunta tipoPregunta) {
		return refreshObject(this, tipoPregunta);
	}
	
	public Encuesta refreshEncuesta(Encuesta encuesta) {
		return refreshObject(this, encuesta);
	}
	
	public List<Encuestado> getEncuestados() {
		return getElements(this, Encuestado.class);
	}
	
	public void guardarRespuestas(String nombre, String email, String telefono, String comentario, Pregunta[] preguntas, String[] respuestas) {
		initDBHelper();
		
		Encuestado e = new Encuestado();
		e.setNombre(nombre);
		e.setEmail(email);
		e.setTelefono(telefono);
		e.setComentario(comentario);
		
		if(preguntas == null) {
			List<Encuesta> list = getEncuestas();
			if(list.size() > 0) {
				Encuesta encuesta = list.get(0);
				preguntas = encuesta.getPreguntasArray();
			}
		}
		
		try {
			Dao<Encuestado, Long> dao = dbHelper.getDao(Encuestado.class);
			dao.create(e);
			
			Dao<Respuesta, Long> daoResp = dbHelper.getDao(Respuesta.class);
			
			Date fecha = new Date();
			
			for (int i = 0; i < preguntas.length; i++) {
				Pregunta pregunta = preguntas[i];
				if(respuestas[i] != null) {
					Respuesta r = new Respuesta();
					r.setPregunta(pregunta);
					r.setRespuesta(respuestas[i]);
					r.setFecha(fecha);
					r.setEncuestado(e);
					
					daoResp.create(r);
				}
			}
			
		} catch (SQLException ex) {
			Log.e("EncuestaManager", "guardarRespuestas: " + ex.getLocalizedMessage());
			ex.printStackTrace();
			Bugsnag.notify(ex);
		}
		
		close();
	}
	
	public static <T> Dao<T, Long> getDao(EncuestaManager em, Class<T> object) {
		em.initDBHelper();
		try {
			Dao<T, Long> dao = em.dbHelper.getDao(object);
			return dao;
		} catch (SQLException e) {
			Log.e("EncuestaManager", "getDao: " + e.getLocalizedMessage());
			e.printStackTrace();
			Bugsnag.notify(e);
			return null;
		}
	}
	
	public void close() {
		if(dbHelper != null) {
			OpenHelperManager.releaseHelper();
			dbHelper = null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
