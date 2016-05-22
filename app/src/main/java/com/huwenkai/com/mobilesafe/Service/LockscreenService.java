package com.huwenkai.com.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.huwenkai.com.mobilesafe.Engine.ProcessInfoProvider;

public class LockscreenService extends Service {

    private IntentFilter mIntentFilter;
    private InnerMyRecive mInnerMyRecive;

    public LockscreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mInnerMyRecive = new InnerMyRecive();
        registerReceiver(mInnerMyRecive, mIntentFilter);

    }
    class InnerMyRecive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ProcessInfoProvider.KillAll(context);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mInnerMyRecive!=null){
            unregisterReceiver(mInnerMyRecive);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
