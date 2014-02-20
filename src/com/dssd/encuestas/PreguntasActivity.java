package com.dssd.encuestas;

import java.sql.SQLException;
import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.Pregunta;
import com.dssd.encuestas.datos.Respuesta;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class PreguntasActivity extends FragmentActivity {
	
	Pregunta[] preguntas;
	int preguntaActual = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preguntas);
		
		initPreguntas();
		mostrarSiguientePregunta();
	}
	
	public void initPreguntas() {
		Encuesta encuesta = null;
		
		DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
		try {
			Dao<Encuesta, Long> dao = dbHelper.getDao(Encuesta.class);
			encuesta = dao.queryForId(1L);
			
			preguntas = encuesta.getPreguntasArray();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
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
	
	public Pregunta getSiguientePregunta() {
		Pregunta pregunta = null;
		
		if((preguntaActual+1) < preguntas.length) {
			pregunta = preguntas[++preguntaActual];
		}
		
		return pregunta;
	}
	
	public void mostrarSiguientePregunta() {
		Pregunta p = getSiguientePregunta();
		
		if(p != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			
			PreguntaFragment fragment;
			
			if(p.getTipo().compareTo("si-no") == 0) {
				fragment = new PreguntaSiNoFragment();
			}
			else if(p.getTipo().compareTo("valores") == 0) {
					fragment = new PreguntaValoresFragment();
					Bundle b = new Bundle();
					b.putStringArray("valores", new String[] {"1", "2", "3", "4", "5"});
					fragment.setArguments(b);
			} else {
				fragment = new PreguntaFragment();
			}
			
			fragment.setPregunta(p);
			
			//if(preguntaActual == 0) {
			//	fragmentTransaction.add(R.id.preguntasMainLayout, fragment);
			//} else {
				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				fragmentTransaction.replace(R.id.preguntasMainLayout, fragment);
				fragmentTransaction.addToBackStack(null);
			//}
			fragmentTransaction.commit();
		} else {
			// no hay mas preguntas
			startActivity(new Intent(this, FinActivity.class));
			finish();
		}
	}
	
	public void responderPregunta(Respuesta respuesta) {
		mostrarSiguientePregunta();
	}
}