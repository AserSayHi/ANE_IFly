package com.pamakids.IFlytek.contexts;

import android.media.MediaRecorder;
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
        functions.put(KeyCode.KEY_START_RECORD, new StartRecord());
        functions.put(KeyCode.KEY_STOP_RECORD, new StopRecord());
        functions.put(KeyCode.KEY_AUDIO_PLAY, new AudioPlayer());
        return functions;
    }

    private MediaRecorder mRecorder;
    void startRecord(String fileName){
        fileName = fileName + ".amr";
        String mFileName = this.getActivity().getFilesDir().getAbsolutePath();
        File f = new File(mFileName, fileName);
        Log.d(TAG, "录音文件路径：" + f.getAbsolutePath());
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);      //音频格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);       //编码方式
        mRecorder.setOutputFile(f.getAbsolutePath());
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        mRecorder.start();
        dispatchStatusEventAsync(EventCode.RECORD_BEGIN, "");
    }

    void stopRecord(){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        dispatchStatusEventAsync(EventCode.RECORD_END, "");
    }

    private Player player;
    void openFile(String path, String name){
        if(player == null)
            player = new Player(this);
        player.openFile(path, name);
    }

    @Override
    public void dispose() {
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
            String path = freObjects[0].getAsString();
            String name = freObjects[1].getAsString();
            context.openFile(path, name);
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
