package com.huwenkai.com.mobilesafe.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * Created by huwenkai on 2016/5/2.
 */
public class ServiceUtils {
    public static boolean IsRunning(String serviceName, Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices) {
            if(runningServiceInfo.service.getClassName().equals(serviceName)){
                return true;
            }
        }

        return false;
    }
}
