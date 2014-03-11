package com.dssd.encuestas.datos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

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
			return null;
		}
	}
	
	public TipoPregunta refreshTipoPregunta(TipoPregunta tipoPregunta) {
		return refreshObject(this, tipoPregunta);
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
