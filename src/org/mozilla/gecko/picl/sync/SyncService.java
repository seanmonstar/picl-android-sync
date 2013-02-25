package org.mozilla.gecko.picl.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

	private static final Object LOCK = new Object();
	
	private static SyncAdapter syncAdapter = null;
	
	@Override
	public void onCreate() {
		synchronized (LOCK) {
			if (syncAdapter == null) {
				syncAdapter = new SyncAdapter(getApplicationContext(), true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return syncAdapter.getSyncAdapterBinder();
	}

}
