package com.pamakids.IFlytek;

import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.pamakids.IFlytek.contexts.ApkInstallContext;
import com.pamakids.IFlytek.contexts.RecognizerContext;
import com.pamakids.IFlytek.contexts.RecorderContext;
import com.pamakids.IFlytek.utils.KeyCode;

/**
 * Created by Administrator on 14-1-10.
 */
class IFlytek implements FREExtension {

    public static final String TAG = "IFlytek";

    @Override
    public void initialize() {
        Log.d(TAG, "Extension initialized.");
    }

    @Override
    public FREContext createContext(String s) {
        Log.d(TAG, "s = "+s);
        if(s.equals(KeyCode.CONTEXT_RECOGNIZER)){
            Log.d(TAG, "KeyCode.CONTEXT_RECOGNIZER = "+KeyCode.CONTEXT_RECOGNIZER);
            return new RecognizerContext();
        }else if(s.equals(KeyCode.CONTEXT_RECORDER)){
            Log.d(TAG, "KeyCode.CONTEXT_RECORDER = "+KeyCode.CONTEXT_RECORDER);
            return new RecorderContext();
        }else if(s.equals( KeyCode.CONTEXT_APKINSTALL)){
            Log.d(TAG, "KeyCode.CONTEXT_APKINSTALL = "+KeyCode.CONTEXT_APKINSTALL);
            return new ApkInstallContext();
        }else{
            Log.d(TAG, "FREContext = null");
            return null;
        }
    }

    @Override
    public void dispose() {
        Log.d(TAG, "Extension disposed.");
    }
}
