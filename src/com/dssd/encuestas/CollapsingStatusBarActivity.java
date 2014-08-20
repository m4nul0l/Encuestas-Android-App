package com.dssd.encuestas;

import com.bugsnag.android.activity.BugsnagFragmentActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public abstract class CollapsingStatusBarActivity extends BugsnagFragmentActivity {
	
	AsyncTask<Void, Void, Void> statusBarHiderAsyncTask;
	
	static boolean enableStatusBarHack = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(enableStatusBarHack) {
			statusBarHiderAsyncTask = TemplateUtils.getStatusBarHiderAsyncTask(this);
			executeAsyncTask(statusBarHiderAsyncTask);
		}
		
		/* Impido que se apague la pantalla */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//TemplateUtils.hideSystemUI(this);
	};
	
	@Override
	protected void onDestroy() {
		if(enableStatusBarHack && statusBarHiderAsyncTask != null) {
			statusBarHiderAsyncTask.cancel(true);
		}
		super.onDestroy();
	}
	
	public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task) {
		executeAsyncTask(task, (T[])null);
	}
	
	@SuppressLint("NewApi")
	public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T ... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		else
		    task.execute(params);
	}
}
