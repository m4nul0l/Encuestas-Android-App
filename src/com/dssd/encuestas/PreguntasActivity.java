package com.dssd.encuestas;

import java.sql.SQLException;
import java.util.List;

import com.dssd.encuestas.datos.Pregunta;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class PreguntasActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preguntas);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		PreguntaFragment fragment = new PreguntaFragment();
		Pregunta p = getPregunta();
		if(p != null)
			fragment.setPregunta(p);
		
		fragmentTransaction.add(R.id.preguntasMainLayout, fragment);
		fragmentTransaction.commit();
		
//		TextView textViewPregunta = (TextView) findViewById(R.id.textViewPregunta);
//		
//		if(textViewPregunta != null && p.getPregunta() != null)
//			textViewPregunta.setText(p.getPregunta());
	}
	
	public Pregunta getPregunta() {
		Pregunta pregunta = null;
		
		DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
		try {
			Dao<Pregunta, Long> dao = dbHelper.getDao(Pregunta.class);
			List<Pregunta> list = dao.queryForEq("encuesta_id", 1);
			
			if(list.size() > 0)
				pregunta = list.get(0);
			
			/*for (Pregunta pregunta : list) {
				System.out.println("preg: " + pregunta.getPregunta() + " (" + pregunta.getTipo() + ")");
			}*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
		
		return pregunta;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preguntas, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.cerrar_preguntas_esta_seguro)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// no hacer nada
				}
			});
		builder.show();
	}
}
