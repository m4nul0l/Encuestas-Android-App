package com.dssd.encuestas.sync;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.webservices.EncuestaItem;
import com.dssd.encuestas.webservices.EncuestasResult;
import com.dssd.encuestas.webservices.PreguntaItem;
import com.dssd.encuestas.webservices.PreguntasResult;
import com.dssd.encuestas.webservices.TipoPreguntaItem;
import com.dssd.encuestas.webservices.TipoPreguntaOpcionItem;
import com.dssd.encuestas.webservices.TiposPreguntasResult;
import com.dssd.encuestas.webservices.TiposPreguntasOpcionesResult;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
			EncuestasSyncHelper.sincronizarTabla("tipospreguntas_opciones", device, getContext(),
					TiposPreguntasOpcionesResult.class, TipoPreguntaOpcionItem.class);
			// Sincronizo los assets (imagenes) de los TiposPreguntasOpciones
			EncuestasSyncHelper.sincronizarAssetsTiposPreguntasOpciones(getContext());
			// Sincronizo Encuestas y Preguntas
			EncuestasSyncHelper.sincronizarTabla("encuestas", device, getContext(),
					EncuestasResult.class, EncuestaItem.class);
			EncuestasSyncHelper.sincronizarTabla("preguntas", device, getContext(),
					PreguntasResult.class, PreguntaItem.class);
		}
		
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
		lbm.sendBroadcast(new Intent(EncuestasSyncHelper.action_sync_end));
	}
}
