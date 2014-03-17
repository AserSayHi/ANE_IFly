package com.pamakids.IFlytek.contexts;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import com.adobe.fre.*;
import com.pamakids.IFlytek.utils.ApkInstaller;
import com.pamakids.IFlytek.utils.EventCode;
import com.pamakids.IFlytek.utils.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kc2ong on 14-2-24.
 */
public class ApkInstallContext extends FREContext {

    public static final String TAG = "ApkInstall";

    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
        functions.put( KeyCode.KEY_CHECK_SERVICE_INSTALL, new CheckServiceInstall());
        functions.put( KeyCode.KEY_SERVICE_INSTALL, new ServiceInstall());
        return functions;
    }



    @Override
    public void dispose() {}

    void installService(String assetsApk) {
        // 直接下载方式
//		ApkInstaller.openDownloadWeb(context, url);
        // 本地安装方式
        Context context = this.getActivity().getApplicationContext();
        if(!ApkInstaller.installFromAssets(context, assetsApk)){
            dispatchStatusEventAsync(EventCode.INSTALL_SERVICE_FAILED, "");
        }
    }

    FREObject checkServiceInstall() throws FREWrongThreadException {
        String packageName = "com.iflytek.speechcloud";
        List<PackageInfo> packages = this.getActivity().getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(packageName)) {
                Log.d(TAG, "已安装");
                return FREObject.newObject(true);
            }
        }
        Log.d(TAG, "未安装");
        return FREObject.newObject(false);
    }
}

class CheckServiceInstall implements FREFunction{

    public static final String TAG = "check_service_install";
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        ApkInstallContext context = ( ApkInstallContext ) freContext;
        try {
            return context.checkServiceInstall();
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class ServiceInstall implements FREFunction{

    public static final String TAG = "service_install";
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        ApkInstallContext context = (ApkInstallContext) freContext;
        try {
            String assetsApk = freObjects[0].getAsString();
            context.installService( assetsApk );
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        }
        return null;
    }
}