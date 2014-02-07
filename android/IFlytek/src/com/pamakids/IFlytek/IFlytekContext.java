package com.pamakids.IFlytek;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.adobe.fre.*;
import com.iflytek.speech.SpeechRecognizer;
import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.GrammarListener;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.LexiconListener;
import org.apache.http.util.EncodingUtils;

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

    //初始化识别控件
    void initmRecognizer() {
        Log.d(TAG, "initRecognizer!");
        //Context context = this.getActivity().getApplicationContext();
        Context context = this.getActivity().getBaseContext();
        if(context == null){
            Log.d(TAG, "context 初始化失败！");
        }else{
            Log.d(TAG, "context 初始化成功!");
        }
        mRecognizer = new SpeechRecognizer(context, mInitListener);
    }

    //语法构建
    void initGrammar(String file) {
        //缓存语法文件
        Log.d(TAG, file);
        mLocalGrammar = readFile(file, "utf-8");
        String grammarContent = new String( mLocalGrammar );
        Log.d(TAG, "语法内容 grammarContent ：" + grammarContent);

        //语法构建参数
        mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_ENCODEING, "utf-8");
        mRecognizer.setParameter("local_scn", "call");
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");

        //语法构建
        int ret = mRecognizer.buildGrammar("abnf", grammarContent, grammarListener);
        if(ret != ErrorCode.SUCCESS)
        Log.d(TAG, "Error：语法构建失败！ret = " + ret);
    }

    //更新词典
    void lexicon(String title,String words){
        String[] arr = words.split("_");
        String content = "";
        for(String s:arr)
        {
            content += (s + "\n");
        }
        //更新词典
        mRecognizer.updateLexicon(title, content, lexiconListener);
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
                dispatchStatusEventAsync(IFlytekEventType.INITRECOG_SUCCESS, "");
            }else{
                Log.d(TAG, "mRecognizer 初始化失败，错误码："+Integer.toString(code));
                dispatchStatusEventAsync(IFlytekEventType.INITRECOG_FAILED, Integer.toString(code));
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
        functions.put( initRecog.TAG, new initRecog() );        //初始化识别控件
        functions.put( initGrammar.TAG, new initGrammar() );    //构建语法，指明语法文件路径
        functions.put( lexcion.TAG, new lexcion() );            //更新词典
        functions.put( startRecog.TAG, new startRecog() );      //开始识别
        functions.put( stopRecog.TAG, new stopRecog() );        //识别结束
        functions.put( cancleRecog.TAG, new cancleRecog() );    //取消识别
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
            Log.d(TAG, "尝试读取文件！file = " + file);
            InputStream in = this.getActivity().getAssets().open(file);
            len  = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);
            grammar = EncodingUtils.getString(buf, code);
            in.close();
        } catch (Exception e) {
            Log.d(TAG, "readFile Error ：" + e.getMessage() );
            e.printStackTrace();
        }
        return grammar;
    }

}

//开始识别
class startRecog implements FREFunction {

    public static final String TAG = "startRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
        context.mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_LIST, "call");
        context.mRecognizer.startListening( context.mRecogListener );
        return null;
    }
}

//停止识别，等待返回结果
class stopRecog implements FREFunction {

    public static final String TAG = "stopRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.mRecognizer.stopListening(context.mRecogListener);
        return null;
    }
}

//取消识别
class  cancleRecog implements FREFunction {

    public static final String TAG = "cancle";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.mRecognizer.cancel(context.mRecogListener);
        return null;
    }
}

//初始化识别控件
class initRecog implements FREFunction {

    public static final String TAG = "initRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        Log.d(TAG, "initmRecognizer!");
        IFlytekContext context = (IFlytekContext) freContext;
        context.initmRecognizer();
        return null;
    }
}

//语法构建
class initGrammar implements FREFunction {

    public static final String TAG = "initGrammar";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        Log.d(TAG, "initGrammar!");
        IFlytekContext context = (IFlytekContext) freContext;
        FREObject object = freObjects[0];
        try {
            context.initGrammar(object.getAsString());
        } catch (FRETypeMismatchException e) {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        } catch (FREInvalidObjectException e) {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
        return null;
    }
}

//更新词典
class lexcion implements FREFunction{

    public static final String TAG = "lexcion";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        FREObject title = freObjects[0];
        FREObject words = freObjects[1];
        context.lexicon(title.toString(), words.toString());
        return null;
    }
}
