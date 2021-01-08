package com.ma.hololstore.Service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {


    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);

         return  START_STICKY;
    }
}
