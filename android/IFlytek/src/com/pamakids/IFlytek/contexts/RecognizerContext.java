package com.pamakids.IFlytek.contexts;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.adobe.fre.*;
import com.iflytek.speech.*;
import com.pamakids.IFlytek.utils.EventCode;
import com.pamakids.IFlytek.utils.KeyCode;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kc2ong on 14-2-25.
 */
public class RecognizerContext extends FREContext {
    public static final String TAG = "IFlytekContext";

    public SpeechRecognizer mRecognizer;

    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
        functions.put( KeyCode.KEY_INITRECOG, new InitRecog() );        //初始化识别控件
        functions.put( KeyCode.KEY_INITGRAMMAR, new InitGrammar() );    //构建语法，指明语法文件路径
        functions.put( KeyCode.KEY_LEXCION, new Lexcion() );            //更新词典
        functions.put( KeyCode.KEY_START_RECOG, new StartRecog() );      //开始识别
        functions.put( KeyCode.KEY_STOP_RECOG, new StopRecog() );        //识别结束
        functions.put( KeyCode.KEY_CANCLE_RECOG, new CancleRecog() );    //取消识别
        functions.put( KeyCode.KEY_RECOG_AUDIO, new WriteAudio() );
        return functions;
    }

    //初始化识别控件
    void initmRecognizer() {
        Log.d(TAG, "initRecognizer!");
        Context context = this.getActivity().getApplicationContext();
        /*if(context == null){
            Log.d(TAG, "context 初始化失败！");
        }else{
            Log.d(TAG, "context 初始化成功!");
        }*/
        mRecognizer = new SpeechRecognizer(context, mInitListener);
        setParams();
    }


    private void setParams(){
        Log.d(TAG, "setParams");
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
        mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_ENCODEING, "utf-8");
        mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_LIST, "call");
        mRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
        mRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        mRecognizer.setParameter(SpeechConstant.PARAMS, "local_grammar=call,mixed_threshold=40");
    }

    //语法构建
    void initGrammar(String file) {
        //缓存语法文件
        String grammarContent = new String( readFile(file, "utf-8") );
        Log.d(TAG, "语法内容 grammarContent ：" + grammarContent);

        //语法构建
        int ret = mRecognizer.buildGrammar("abnf", grammarContent, grammarListener);
        if(ret != ErrorCode.SUCCESS){
            Log.d(TAG, "Error：语法构建失败！ret = " + ret);
        }
    }

    //更新词典
    void lexicon(String title,String words){
        String[] arr = words.split("_");
        String content = "";
        for(String s:arr){
            content += (s + "\n");
        }
        Log.d(TAG, "更新词典：\ntitle = " +title + ";\ncontent = " + content);
        int code = mRecognizer.updateLexicon(title, content, lexiconListener);
        if(code != ErrorCode.SUCCESS){
            Log.d(TAG, "词典更新失败，错误码： " + code);
        }
    }

    void startRecog() {
        int recode = mRecognizer.startListening( mRecogListener );
        if(recode != ErrorCode.SUCCESS){
            Log.d(TAG, "语法识别失败！错误码： " + recode);
        }
    }

    void stopRecog() {
        mRecognizer.stopListening(mRecogListener);
        Log.d(TAG, "StopRecog!");
    }

    void cancleRecog() {
        mRecognizer.cancel(mRecogListener);
        Log.d(TAG, "CancleRecog！");
    }

    void writeAudio(String fileName){
        String path = this.getActivity().getFilesDir().getAbsolutePath();
        String file = fileName + ".amr";
        File f = new File(path, file);
        try {
            FileInputStream fin = this.getActivity().openFileInput(file);
            int length  = fin.available();
            byte[] buf = new byte[length];
            fin.read(buf, 0, length);
            mRecognizer.setParameter(SpeechConstant.PARAMS, "audio_source=-1");
            mRecognizer.writeAudio(buf, 0, length);
            //fin.close();
        } catch (Exception e) {
            Log.d(TAG, "readFile Error ：" + e.getMessage() );
            e.printStackTrace();
        }
    }

    private GrammarListener grammarListener = new GrammarListener.Stub() {
        @Override
        public void onBuildFinish(String grammarId, int errorCode) throws RemoteException {
            if(errorCode == ErrorCode.SUCCESS){
                Log.d(TAG, "语法构建成功！语法ID： " + grammarId);
                dispatchStatusEventAsync(EventCode.INITRECOG_GRAMMER_SUCCESS, "");
            }else{
                dispatchStatusEventAsync(EventCode.INITRECOG_GRAMMER_FAILED, Integer.toString(errorCode));
                Log.d(TAG, "语法构建失败，错误码：" + errorCode);
            }
        }
    };

    public InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(ISpeechModule iSpeechModule, int code) {
            if (code == ErrorCode.SUCCESS) {
                Log.d(TAG, "mRecognizer initialized!");
                dispatchStatusEventAsync(EventCode.INITRECOG_SUCCESS, "");
            }else{
                Log.d(TAG, "mRecognizer 初始化失败，错误码："+Integer.toString(code));
                dispatchStatusEventAsync(EventCode.INITRECOG_FAILED, Integer.toString(code));
            }
        }
    };

    private LexiconListener lexiconListener = new LexiconListener.Stub() {
        @Override
        public void onLexiconUpdated(String arg0, int arg1) throws RemoteException {
            if(ErrorCode.SUCCESS == arg1){
                Log.d(TAG, "词典更新成功");
                dispatchStatusEventAsync(EventCode.UPDATE_LEXCION_SUCCESS, "");
            }else{
                Log.d(TAG, "词典更新失败！错误码：" + arg1);
                dispatchStatusEventAsync(EventCode.UPDATE_LEXCION_FAILED, Integer.toString( arg1 ) );
            }
        }
    };

    public RecognizerListener mRecogListener = new RecognizerListener.Stub() {

        @Override
        public void onVolumeChanged(int i) throws RemoteException {
            Log.d(TAG, "onVolumeChanged！ " + Integer.toString(i));
            dispatchStatusEventAsync(EventCode.VOLUME_CHANGED, Integer.toString(i));
        }

        @Override
        public void onBeginOfSpeech() throws RemoteException {
            Log.d(TAG, "Begin of Speech!");
            dispatchStatusEventAsync(EventCode.RECOG_BEGIN, "");
        }

        @Override
        public void onEndOfSpeech() throws RemoteException {
            Log.d(TAG, "End of Speech!");
            dispatchStatusEventAsync(EventCode.RECOG_END, "");
        }

        @Override
        public void onResult(RecognizerResult result, boolean b) throws RemoteException{
            if(result != null) {
                String text = result.getResultString();
                Log.d(TAG, "recognizer result" + text);
                dispatchStatusEventAsync(EventCode.RECOG_RESULT, result.getResultString());
            } else {
                Log.d(TAG, "recognizer result : null");
                dispatchStatusEventAsync(EventCode.RECOG_RESULT, "");
            }
        }

        @Override
        public void onError(int errorcode) throws RemoteException {
            Log.d(TAG, "onError Code：" + errorcode);
            dispatchStatusEventAsync(EventCode.RECOG_ERROR, Integer.toString(errorcode));
        }
    };
    @Override
    public void dispose() {
        Log.d(TAG, "Context disposed.");
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

//初始化识别控件
class InitRecog implements FREFunction {

    public static final String TAG = "InitRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        Log.d(TAG, "initmRecognizer!");
        RecognizerContext context = (RecognizerContext) freContext;
        context.initmRecognizer();
        return null;
    }
}

//开始识别
class StartRecog implements FREFunction {

    public static final String TAG = "StartRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecognizerContext context = (RecognizerContext) freContext;
        context.startRecog();
        return null;
    }
}

//停止识别，等待返回结果
class StopRecog implements FREFunction {

    public static final String TAG = "StopRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecognizerContext context = (RecognizerContext) freContext;
        context.stopRecog();
        return null;
    }
}

//取消识别
class CancleRecog implements FREFunction {

    public static final String TAG = "CancleRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecognizerContext context = (RecognizerContext) freContext;
        context.cancleRecog();
        return null;
    }
}

//语法构建
class InitGrammar implements FREFunction {

    public static final String TAG = "InitGrammar";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        Log.d(TAG, "InitGrammar!");
        RecognizerContext context = (RecognizerContext) freContext;
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
class Lexcion implements FREFunction{

    public static final String TAG = "Lexcion";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecognizerContext context = (RecognizerContext) freContext;
        try {
            String title = freObjects[0].getAsString();
            String words = freObjects[1].getAsString();
            context.lexicon(title, words);
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

class WriteAudio implements FREFunction{

    public static final String TAG = "writeAudio";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecognizerContext context = (RecognizerContext) freContext;
        try {
            String fileName = freObjects[0].getAsString();
            context.writeAudio(fileName);
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