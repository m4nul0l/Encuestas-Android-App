package com.dssd.encuestas;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.Encuestado;
import com.dssd.encuestas.datos.Pregunta;
import com.dssd.encuestas.datos.Respuesta;
import com.dssd.encuestas.datos.TipoPregunta;
import com.dssd.encuestas.datos.TipoPreguntaOpcion;
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
			TableUtils.createTable(cs, TipoPregunta.class);
			TableUtils.createTable(cs, TipoPreguntaOpcion.class);
			
			//TableUtils.createTable(cs, Cliente.class);
			TableUtils.createTable(cs, Encuesta.class);
			TableUtils.createTable(cs, Pregunta.class);
			TableUtils.createTable(cs, Encuestado.class);
			TableUtils.createTable(cs, Respuesta.class);
			
			/* Encuesta de prueba */
			/*Encuesta e1 = new Encuesta();
			Dao<Encuesta, Long> encuestas = getDao(Encuesta.class);
			encuestas.create(e1);*/
			
			/* Preguntas de prueba */
			/*Dao<Pregunta, Long> preguntas = getDao(Pregunta.class);
			
			Pregunta p1 = new Pregunta();
			p1.setEncuesta(e1);
			p1.setPregunta("¿Conoce nuestro producto?");
			p1.setTipo("si-no");
			preguntas.create(p1);
			
			Pregunta p2 = new Pregunta();
			p2.setEncuesta(e1);
			p2.setPregunta("¿Le gusta nuestro producto?");
			p2.setTipo("valores");
			preguntas.create(p2);*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource cs, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
