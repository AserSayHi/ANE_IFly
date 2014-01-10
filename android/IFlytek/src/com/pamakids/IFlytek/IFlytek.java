package com.pamakids.IFlytek;

import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

/**
 * Created by Administrator on 14-1-10.
 */
class IFlytek implements FREExtension {

    public static final String TAG = "UMSocial";

    @Override
    public void initialize() {
        Log.d(TAG, "Extension initialized.");
    }

    @Override
    public FREContext createContext(String s) {
        return new IFlytekContext() ;
    }

    @Override
    public void dispose() {
        Log.d(TAG, "Extension disposed.");
    }
}
