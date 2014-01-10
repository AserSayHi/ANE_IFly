package com.pamakids.IFlytek;

import android.content.Context;
import android.content.Intent;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechRecognizer;

/**
 * Created by Administrator on 14-1-10.
 */
public class RecognizerInit extends Context implements FREFunction {

    public static final String TAG = "init";

    private SpeechRecognizer mRecognizer;

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        InitListener mInitListener;
        mRecognizer = new SpeechRecognizer(this, mInitListener);
        return null;
    }

    @Override
    public void sendBroadcast(Intent intent, String s) {

    }
}
