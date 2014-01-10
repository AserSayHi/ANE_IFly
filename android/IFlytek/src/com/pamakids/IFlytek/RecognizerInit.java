package com.pamakids.IFlytek;

import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.iflytek.speech.*;

import java.io.InputStream;

/**
 * Created by Administrator on 14-1-10.
 */
public class RecognizerInit implements FREFunction {

    public static final String TAG = "init";
    public static final String IFLYTEK_ID = "appid=52b7d6b1";

    private SpeechRecognizer mRecognizer;
    private String mLocalGrammar = null;

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {

        SpeechUtility.getUtility(freContext.getActivity().getApplicationContext()).setAppid( IFLYTEK_ID );

        mRecognizer = new SpeechRecognizer(freContext.getActivity().getApplicationContext(), mInitListener);

        mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_ENCODEING,"utf-8");
        //本地语法构建需要指定
        mRecognizer.setParameter("local_scn", "call");
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
        mRecognizer.startListening( mRecogListener );
        mRecognizer.stopListening( mRecogListener );
        mRecognizer.cancel( mRecogListener );

        mLocalGrammar = readFile(freContext,"call.bnf", "utf-8");
        String grammarContent = new String(mLocalGrammar);

        //构建语法
        int ret = mRecognizer.buildGrammar("abnf", grammarContent, grammarListener);
        if(ret != ErrorCode.SUCCESS)
           System.out.println("Error： 语法构建失败！");

        //更新词典
        mRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, "local");
        mRecognizer.setParameter(SpeechRecognizer.GRAMMAR_LIST, "call");
        mRecognizer.updateLexicon("<contact>", "张海羊\n刘婧\n王锋\n", lexiconListener);
        return null;
    }

    private GrammarListener grammarListener = new GrammarListener.Stub() {
        @Override
        public void onBuildFinish(String grammarId, int errorCode) throws RemoteException {
            if(errorCode == ErrorCode.SUCCESS){
                System.out.println("语法构建成功：" + grammarId);
            }else{
                System.out.println("语法构建失败，错误码：" + errorCode);
            }
        }
    };

    public InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(ISpeechModule iSpeechModule, int i) {

        }
    };

    private LexiconListener lexiconListener = new LexiconListener.Stub() {

        @Override
        public void onLexiconUpdated(String arg0, int arg1) throws RemoteException {
            if(ErrorCode.SUCCESS == arg1)
                System.out.println("词典更新成功");
            else
                System.out.println("词典更新失败，错误码："+arg1);
        }
    };

    public RecognizerListener mRecogListener = new RecognizerListener() {

         @Override
         public void onVolumeChanged(int i) throws RemoteException {

         }

         @Override
         public void onBeginOfSpeech() throws RemoteException {

         }

         @Override
         public void onEndOfSpeech() throws RemoteException {

         }

         @Override
         public void onResult(RecognizerResult recognizerResult, boolean b) throws RemoteException {

         }

         @Override
         public void onError(int i) throws RemoteException {

         }

         @Override
         public IBinder asBinder() {
             return null;
         }
     };

    /**
     * 读取语法文件。
     * @return
     */
    private String readFile(FREContext freContext,String file,String code) {
        int len = 0;
        byte []buf = null;
        String grammar = "";
        try {
            InputStream in = freContext.getActivity().getApplicationContext().getAssets().open(file);
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

