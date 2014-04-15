package com.dssd.encuestas.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.webservices.EncuestaItem;
import com.dssd.encuestas.webservices.EncuestasResult;
import com.dssd.encuestas.webservices.PreguntaItem;
import com.dssd.encuestas.webservices.PreguntasResult;
import com.dssd.encuestas.webservices.TipoPreguntaItem;
import com.dssd.encuestas.webservices.TipoPreguntaOpcionItem;
import com.dssd.encuestas.webservices.TiposPreguntasOpcionesResult;
import com.dssd.encuestas.webservices.TiposPreguntasResult;

public class WebSyncAdapter extends AbstractThreadedSyncAdapter {

	public WebSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		// TODO Auto-generated constructor stub
	}

	@TargetApi(11)
	public WebSyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		// TODO PONER proceso :sync en manifiesto!
		Log.i("WebSyncAdapter", "onPerformSync: sync started");
		
		String device = AppConfig.getInstance(getContext()).getDevice();
		if(device != null) {
			// Sincronizo TiposPreguntas y sus Opciones
			EncuestasSyncHelper.sincronizarTabla("tipospreguntas", device, getContext(),
					TiposPreguntasResult.class, TipoPreguntaItem.class);
			Log.i("WebSyncAdapter", "onPerformSync: sync tipospreguntas");
			
			EncuestasSyncHelper.sincronizarTabla("tipospreguntas_opciones", device, getContext(),
					TiposPreguntasOpcionesResult.class, TipoPreguntaOpcionItem.class);
			Log.i("WebSyncAdapter", "onPerformSync: sync tipospreguntas_opciones");
			
			// Sincronizo los assets (imagenes) de los TipotasOpciones
			EncuestasSyncHelper.sincronizarAssetsTiposPreguntasOpciones(getContext());
			Log.i("WebSyncAdapter", "onPerformSync: sync started");
			
			// Sincronizo Encuestas y Preguntas
			EncuestasSyncHelper.sincronizarTabla("encuestas", device, getContext(),
					EncuestasResult.class, EncuestaItem.class);
			Log.i("WebSyncAdapter", "onPerformSync: sync encuestas");
			
			EncuestasSyncHelper.sincronizarTabla("preguntas", device, getContext(),
					PreguntasResult.class, PreguntaItem.class);
			Log.i("WebSyncAdapter", "onPerformSync: sync preguntas");
			
			/*EncuestasSyncHelper.sincronizarRespuestas(device, getContext());
			Log.i("WebSyncAdapter", "onPerformSync: sync respuestas");*/
			
			// Sincronizo los assets (imagenes) de las encuestas (logo)
			EncuestasSyncHelper.sincronizarAssetsEncuestas(getContext());
		}
		Log.i("WebSyncAdapter", "onPerformSync: sync ended");
		
		//LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
		//lbm.sendBroadcast(new Intent(EncuestasSyncHelper.action_sync_end));
		getContext().sendBroadcast(new Intent(EncuestasSyncHelper.action_sync_end));
	}
}
