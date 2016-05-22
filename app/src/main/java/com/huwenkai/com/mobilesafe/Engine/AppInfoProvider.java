package com.huwenkai.com.mobilesafe.Engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.huwenkai.com.mobilesafe.db.Domian.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwenkai on 2016/5/14.
 * 回去应用的信息类
 */
public class AppInfoProvider {
    /**
     * 返回当前手机所有的应用的相关信息(名称,包名,图标,(手机内存,sd卡),(系统,用户));
     *
     * @param context 获取包管理者的上下文环境
     * @return 包含手机安装应用相关信息的集合
     */
    public static List<AppInfo> getAppinfo(Context context) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = packageInfo.packageName;
            appInfo.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            appInfo.icon = packageInfo.applicationInfo.loadIcon(packageManager);
            //7,判断是否为系统应用(每一个手机上的应用对应的flag都不一致)
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                appInfo.isSystem = true;
            } else {
                appInfo.isSystem = false;
            }
            //8,是否为sd卡中安装应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                //系统应用
                appInfo.isSdCard = true;
            } else {
                //非系统应用
                appInfo.isSdCard = false;
            }
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
