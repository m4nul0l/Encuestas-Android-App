package com.dssd.encuestas;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dssd.encuestas.datos.Cliente;
import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.Encuestado;
import com.dssd.encuestas.datos.Pregunta;
import com.dssd.encuestas.datos.Respuesta;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	
	public static final String DB_NAME = "encuestas";
	public static final int DB_VERSION = 1;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource cs) {
		try {
			TableUtils.createTable(cs, Cliente.class);
			TableUtils.createTable(cs, Encuesta.class);
			TableUtils.createTable(cs, Pregunta.class);
			TableUtils.createTable(cs, Encuestado.class);
			TableUtils.createTable(cs, Respuesta.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource cs, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
