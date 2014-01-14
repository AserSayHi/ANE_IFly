package com.pamakids.IFlytek;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.iflytek.speech.SpeechRecognizer;
import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.GrammarListener;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.LexiconListener;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-1-10.
 */
public class IFlytekContext extends FREContext{

    public static final String TAG = "IFlytekContext";

    public SpeechRecognizer mRecognizer;
    private String mLocalGrammar = null;
    //private String appid = null;

    void initmRecognizer() {

        Log.d(TAG, "initRecognizer!");

        Context context;
        //初始化识别控件
        try{
            Log.d(TAG, "try to init context!");
            context= this.getActivity().getApplicationContext();
            if(context != null)
            {
                mRecognizer = new SpeechRecognizer(context, mInitListener);
                if( mRecognizer != null )
                    Log.d(TAG, "mRecognizer initialized!");
                //语法构建参数
                mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_ENCODEING,"utf-8");
                mRecognizer.setParameter("local_scn", "call");
                mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");

                //缓存语法文件
                mLocalGrammar = readFile("call.bnf", "utf-8");
                String grammarContent = new String( mLocalGrammar );

                Log.d(TAG, "语法文件：" + grammarContent);

                //语法构建
                int ret = mRecognizer.buildGrammar("abnf", grammarContent, grammarListener);
                if(ret != ErrorCode.SUCCESS)
                    Log.d(TAG, "Error：语法构建失败！ret = " + ret);
                else
                    Log.d(TAG, "Error：语法构建成功！");

                //本地语法构建
                mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
                mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_LIST, "call");
                //更新词典
                //mRecognizer.updateLexicon("<contact>", "�ź���\n���\n����\n", lexiconListener);
            }
        }catch (Error e)
        {
            e.printStackTrace();
        }
    }

    private GrammarListener grammarListener = new GrammarListener.Stub() {
        @Override
        public void onBuildFinish(String grammarId, int errorCode) throws RemoteException {
            if(errorCode == ErrorCode.SUCCESS){
                Log.d(TAG, "语法构建成功！" + grammarId);
            }else{
                Log.d(TAG, "语法构建失败，错误码：" + errorCode);
            }
        }
    };

    public InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(ISpeechModule iSpeechModule, int code) {
            if (code == ErrorCode.SUCCESS) {
                Log.d(TAG, "mRecognizer initialized!");
            }
        }
    };

    private LexiconListener lexiconListener = new LexiconListener.Stub() {
        @Override
        public void onLexiconUpdated(String arg0, int arg1) throws RemoteException {
            if(ErrorCode.SUCCESS == arg1)
                Log.d(TAG, "词典更新成功");
            else
                Log.d(TAG, "词典更新失败！错误码：" + arg1);
        }
    };

    public RecognizerListener mRecogListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i) throws RemoteException {
            Log.d(TAG, "onVolumeChanged！ " + Integer.toString(i));
        }

        @Override
        public void onBeginOfSpeech() throws RemoteException {
            Log.d(TAG, "Begin of Speech!");
        }

        @Override
        public void onEndOfSpeech() throws RemoteException {
            Log.d(TAG, "End of Speech!");
        }

        @Override
        public void onResult(RecognizerResult result, boolean b) throws RemoteException{

            if(result != null) {
                String text = result.getResultString();
                Log.d(TAG, "recognizer result" + text);
            } else {
                Log.d(TAG, "recognizer result : null");
            }

            dispatchStatusEventAsync("result", result.getResultString());
        }

        @Override
        public void onError(int errorcode) throws RemoteException {
            Log.d(TAG, "onError Code：" + errorcode);
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };
    @Override
    public void dispose() {
        Log.d(TAG, "Context disposed.");
    }

    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
        functions.put( initRecog.TAG, new initRecog() );
        functions.put( startRecog.TAG, new startRecog() );
        functions.put( stopRecog.TAG, new stopRecog() );
        functions.put( cancleRecog.TAG, new cancleRecog() );
        return functions;
    }

    /**
     * 读取文件
     * @return
     */
    private String readFile(String file,String code) {
        int len = 0;
        byte []buf = null;
        String grammar = "";
        try {
            InputStream in = this.getActivity().getApplicationContext().getAssets().open(file);
            len  = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            grammar = new String(buf,code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grammar;
    }

}

class startRecog implements FREFunction {

    public static final String TAG = "start";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
        context.mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_LIST, "call");
        context.mRecognizer.startListening( context.mRecogListener );
        return null;
    }
}

class stopRecog implements FREFunction {

    public static final String TAG = "stop";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.mRecognizer.stopListening(context.mRecogListener);
        return null;
    }
}

class  cancleRecog implements FREFunction {

    public static final String TAG = "cancle";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.mRecognizer.cancel(context.mRecogListener);
        return null;
    }
}

class initRecog implements FREFunction {

    public static final String TAG = "init";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        Log.d(TAG, "initmRecognizer!");
        IFlytekContext context = (IFlytekContext) freContext;
        context.initmRecognizer();
        return null;
    }
}
