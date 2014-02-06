package com.dssd.encuestas;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Toast.makeText(this, "se presiono", Toast.LENGTH_SHORT).show();
		
		if(event.getAction() == MotionEvent.ACTION_UP) {
			startActivity(new Intent(this, PreguntasActivity.class));
			return true;
		}
		
		return super.onTouchEvent(event);
	}

}
