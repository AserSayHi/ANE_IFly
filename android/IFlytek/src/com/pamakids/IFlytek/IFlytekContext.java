package com.pamakids.IFlytek;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
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
import java.util.List;
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
        Context context = this.getActivity().getApplicationContext();
        if(context == null){
            Log.d(TAG, "context 初始化失败！");
        }else{
            Log.d(TAG, "context 初始化成功!");
        }
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
        mLocalGrammar = readFile(file, "utf-8");
        String grammarContent = new String( mLocalGrammar );
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
        Log.d(TAG, "更新词典： title = " +title);
        Log.d(TAG, "更新词典： content = " + content);
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
        Log.d(TAG, "stopRecog!");
    }

    void cancleRecog() {
        mRecognizer.cancel(mRecogListener);
        Log.d(TAG, "cancleRecog！");
    }

    private GrammarListener grammarListener = new GrammarListener.Stub() {
        @Override
        public void onBuildFinish(String grammarId, int errorCode) throws RemoteException {
            if(errorCode == ErrorCode.SUCCESS){
                Log.d(TAG, "语法构建成功！语法ID： " + grammarId);
                dispatchStatusEventAsync(IFlytekRecogEventType.INITRECOG_GRAMMER_SUCCESS, "");
            }else{
                dispatchStatusEventAsync(IFlytekRecogEventType.INITRECOG_GRAMMER_FAILED, Integer.toString(errorCode));
                Log.d(TAG, "语法构建失败，错误码：" + errorCode);
            }
        }
    };

    public InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(ISpeechModule iSpeechModule, int code) {
            if (code == ErrorCode.SUCCESS) {
                Log.d(TAG, "mRecognizer initialized!");
                dispatchStatusEventAsync(IFlytekRecogEventType.INITRECOG_SUCCESS, "");
            }else{
                Log.d(TAG, "mRecognizer 初始化失败，错误码："+Integer.toString(code));
                dispatchStatusEventAsync(IFlytekRecogEventType.INITRECOG_FAILED, Integer.toString(code));
            }
        }
    };

    private LexiconListener lexiconListener = new LexiconListener.Stub() {
        @Override
        public void onLexiconUpdated(String arg0, int arg1) throws RemoteException {
            if(ErrorCode.SUCCESS == arg1){
                Log.d(TAG, "词典更新成功");
                dispatchStatusEventAsync(IFlytekRecogEventType.UPDATE_LEXCION_SUCCESS, "");
            }else{
                Log.d(TAG, "词典更新失败！错误码：" + arg1);
                dispatchStatusEventAsync(IFlytekRecogEventType.UPDATE_LEXCION_FAILED, Integer.toString( arg1 ) );
            }
        }
    };

    public RecognizerListener mRecogListener = new RecognizerListener.Stub() {

        @Override
        public void onVolumeChanged(int i) throws RemoteException {
            Log.d(TAG, "onVolumeChanged！ " + Integer.toString(i));
            dispatchStatusEventAsync(IFlytekRecogEventType.VOLUME_CHANGED, Integer.toString(i));
        }

        @Override
        public void onBeginOfSpeech() throws RemoteException {
            Log.d(TAG, "Begin of Speech!");
            dispatchStatusEventAsync(IFlytekRecogEventType.RECOG_BEGIN, "");
        }

        @Override
        public void onEndOfSpeech() throws RemoteException {
            Log.d(TAG, "End of Speech!");
            dispatchStatusEventAsync(IFlytekRecogEventType.RECOG_END, "");
        }

        @Override
        public void onResult(RecognizerResult result, boolean b) throws RemoteException{
            if(result != null) {
                String text = result.getResultString();
                Log.d(TAG, "recognizer result" + text);
                dispatchStatusEventAsync(IFlytekRecogEventType.RECOG_RESULT, result.getResultString());
            } else {
                Log.d(TAG, "recognizer result : null");
                dispatchStatusEventAsync(IFlytekRecogEventType.RECOG_RESULT, "");
            }
        }

        @Override
        public void onError(int errorcode) throws RemoteException {
            Log.d(TAG, "onError Code：" + errorcode);
            dispatchStatusEventAsync(IFlytekRecogEventType.RECOG_ERROR, Integer.toString(errorcode));
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
        functions.put( checkServiceInstall.TAG, new checkServiceInstall() );
        functions.put( serviceInstall.TAG, new serviceInstall() );
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

    FREObject checkServiceInstall() throws FREWrongThreadException {
        String packageName = "com.iflytek.speechcloud";
        List<PackageInfo> packages = this.getActivity().getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(packageName)) {
                return FREObject.newObject(true);
            }
        }
        return FREObject.newObject(false);
    }

    void installService(String assetsApk) {
        // 直接下载方式
//		ApkInstaller.openDownloadWeb(context, url);
        // 本地安装方式
        Context context = this.getActivity().getApplicationContext();
        if(!ApkInstaller.installFromAssets(context, assetsApk)){
            dispatchStatusEventAsync(IFlytekRecogEventType.INSTALL_SERVICE_FAILED, "");
        }else{
            dispatchStatusEventAsync(IFlytekRecogEventType.INSTALL_SERVICE_SUCCESS, "");
        }
    }
}

//开始识别
class startRecog implements FREFunction {

    public static final String TAG = "startRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.startRecog();
        return null;
    }
}

//停止识别，等待返回结果
class stopRecog implements FREFunction {

    public static final String TAG = "stopRecog";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.stopRecog();
        return null;
    }
}

//取消识别
class  cancleRecog implements FREFunction {

    public static final String TAG = "cancle";

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        context.cancleRecog();
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

class checkServiceInstall implements FREFunction{

    public static final String TAG = "check_service_install";
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        try {
            return context.checkServiceInstall();
        } catch (FREWrongThreadException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class serviceInstall implements FREFunction{

    public static final String TAG = "service_install";
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        IFlytekContext context = (IFlytekContext) freContext;
        try {
            String assetsApk = freObjects[0].getAsString();
            context.installService(assetsApk);
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
