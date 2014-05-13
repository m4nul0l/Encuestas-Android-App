package com.dssd.encuestas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class CollapsingStatusBarActivity extends FragmentActivity {
	
	AsyncTask<Void, Void, Void> statusBarHiderAsyncTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		statusBarHiderAsyncTask = TemplateUtils.getStatusBarHiderAsyncTask(this);
		statusBarHiderAsyncTask.execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		TemplateUtils.hideSystemUI(this);
	};
	
	@Override
	protected void onDestroy() {
		statusBarHiderAsyncTask.cancel(true);
		super.onDestroy();
	}
}
