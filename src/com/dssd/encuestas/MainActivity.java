package com.dssd.encuestas;

import java.sql.SQLException;
import java.util.List;

import com.dssd.encuestas.datos.Pregunta;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
		try {
			Dao<Pregunta, Long> dao = dbHelper.getDao(Pregunta.class);
			List<Pregunta> list = dao.queryForEq("encuesta_id", 1);
			for (Pregunta pregunta : list) {
				System.out.println("preg: " + pregunta.getPregunta() + " (" + pregunta.getTipo() + ")");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//return super.onTouchEvent(event);
		Toast.makeText(this, "se presiono", Toast.LENGTH_SHORT).show();
		return true;
	}

}
