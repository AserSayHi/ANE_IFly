package com.pamakids.IFlytek;

import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-1-10.
 */
public class IFlytekContext extends FREContext{

    public static final String TAG = "IFlytekContext";

    @Override
    public void dispose() {
        Log.d(TAG, "Context disposed.");
    }

    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
        functions.put( Init.TAG, new RecognizerInit() );
        return functions;
    }
}
