package com.huwenkai.com.mobilesafe.Engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.huwenkai.com.mobilesafe.R;
import com.huwenkai.com.mobilesafe.db.Domian.ProcessInfo;
import com.lidroid.xutils.db.annotation.Id;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwenkai on 2016/5/15.
 */
public class ProcessInfoProvider {
    /**
     * @param context 上下文
     * @return 运行进程的个数
     */
    public static int getProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        return runningAppProcesses.size();
    }


    /**
     * @param context 上下文
     * @return    当前手机的剩余内存大小
     */
    public static long getAvailSpace(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * @param context 上下文
     * @return    当前手机的内存大小
     */
    public static long getTotalSpace(Context context) {
        //由于4.1版本一下不能用所以不用
        /*ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;*/
        FileInputStream fis = null;
        BufferedReader buff = null;


        try {
            fis = new FileInputStream("proc/meminfo");
            buff = new BufferedReader(new InputStreamReader(fis));
            String lineOne = buff.readLine();
            char[] chars = lineOne.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] >= '0' && chars[i] <= '9') {
                    sb.append(chars[i]);
                }
            }
            return Long.parseLong(sb.toString())*1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis!=null&&buff!=null){
                try {
                    fis.close();
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        return 0;
    }

    public static List<ProcessInfo> getProcessInfo(Context context){
        List<ProcessInfo> processInfoList = new ArrayList<ProcessInfo>();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info : runningAppProcesses){
            ProcessInfo processInfo = new ProcessInfo();
            //获取进程的名字
             processInfo.packageName  = info.processName;
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            //获取应用的已使用的内存
           processInfo.memSize = memoryInfo.getTotalPrivateDirty()*1024;
            PackageManager  pm = context.getPackageManager();
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packageName, 0);
                processInfo.icon = applicationInfo.loadIcon(pm);
                processInfo.name = applicationInfo.loadLabel(pm).toString();
                if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM )== ApplicationInfo.FLAG_SYSTEM ){
                    processInfo.isSystem = true;
                }else {
                    processInfo.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                processInfo.name = processInfo.packageName;
                processInfo.icon = context.getResources().getDrawable(R.mipmap.ic_launcher);
                processInfo.isSystem = true;
                e.printStackTrace();
            }
            processInfoList.add(processInfo);

        }
        return processInfoList;
    }

    public static void KillProcess(Context context,ProcessInfo processInfo) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(processInfo.packageName);
    }

    public static void KillAll(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info : runningAppProcesses){
            if(!info.processName.equals(context.getPackageName()))
            am.killBackgroundProcesses(info.processName);
        }
    }
}
