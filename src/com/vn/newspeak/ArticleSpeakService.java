package com.vn.newspeak;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ArticleSpeakService extends Service {
	
	public class ArticleSpeakBinder extends Binder {
		ArticleSpeakService getService() {
			return ArticleSpeakService.this;			
		}
	}
	
	private ArticleSpeakBinder mBinder = new ArticleSpeakBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
    public void onCreate() {
        // Tell the user we stopped.
        Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
     
        // Tell the user we stopped.
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
    }

}
