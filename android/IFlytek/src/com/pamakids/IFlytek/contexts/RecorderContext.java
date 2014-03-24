package com.pamakids.IFlytek.contexts;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;
import com.adobe.fre.*;
import com.pamakids.IFlytek.utils.EventCode;
import com.pamakids.IFlytek.utils.KeyCode;
import com.pamakids.IFlytek.utils.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kc2ong on 14-2-24.
 */
public class RecorderContext extends FREContext {

    private static final String TAG = "RecorderContext";

    @Override
    public Map<String, FREFunction> getFunctions() {

        Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
        functions.put(KeyCode.KEY_INIT_RECORD, new InitRecord());
        functions.put(KeyCode.KEY_START_RECORD, new StartRecord());
        functions.put(KeyCode.KEY_STOP_RECORD, new StopRecord());
        functions.put(KeyCode.KEY_AUDIO_PLAY, new AudioPlayer());
        return functions;
    }

    private MediaRecorder mRecorder;
    private String mFileName;

    void initRecord(){
        if(mFileName.equals(null))
            mFileName = this.getActivity().getFilesDir().getAbsolutePath();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);      //音频格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);       //编码方式
    }

    void startRecord(String fileName){
        fileName = fileName + ".amr";
        File f = new File(mFileName, fileName);
        Log.d(TAG, "录音文件路径：" + f.getAbsolutePath());
        mRecorder.setOutputFile(f.getAbsolutePath());
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        Log.d(TAG, "录音开始！");
        mRecorder.start();
        dispatchStatusEventAsync(EventCode.RECORD_BEGIN, "");
    }

    void stopRecord(){
        Log.d(TAG, "stopRecord.");
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        dispatchStatusEventAsync(EventCode.RECORD_END, "");
    }

//    private Player player;
    void openFile( String name){
        String fileName = name + ".amr";
        Log.d(TAG, "openFile: "+fileName);
        File f = new File(mFileName, fileName);
        Intent intent = this.getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "audio");
        this.getActivity().startActivity(intent);
//        if(player == null)
//            player = new Player(this);
//        player.openFile(mFileName, name);
    }

    @Override
    public void dispose() {
        Log.d(TAG, "Context disposed.");
    }
}

class InitRecord implements FREFunction{

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecorderContext context = (RecorderContext)freContext;
        context.initRecord();
        return null;
    }
}

class StartRecord implements FREFunction{

    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecorderContext context = (RecorderContext)freContext;
        try {
            String fileName = freObjects[0].getAsString();
            context.startRecord(fileName);
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

class StopRecord implements FREFunction{
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecorderContext context = (RecorderContext)freContext;
        context.stopRecord();
        return null;
    }
}

class AudioPlayer implements FREFunction{
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        RecorderContext context = (RecorderContext)freContext;
        try {
            String name = freObjects[0].getAsString();
            context.openFile(name);
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
