package com.dssd.encuestas.datos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

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

	public List<Encuesta> getEncuestas() {
		initDBHelper();
		try {
			Dao<Encuesta, Long> dao = dbHelper.getDao(Encuesta.class);
			List<Encuesta> list = dao.queryForAll();
			
			return list;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<Encuesta>(0);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		OpenHelperManager.releaseHelper();
	}
}
