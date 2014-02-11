package com.dssd.encuestas;

import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;

public class FinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fin);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			finish();
			return true;
		}
		
		return super.onTouchEvent(event);
	}
}
