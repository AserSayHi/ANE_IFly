package com.pamakids.IFlytek.utils;

import android.content.Intent;
import android.net.Uri;
import com.adobe.fre.FREContext;
import com.pamakids.IFlytek.contexts.RecorderContext;

import java.io.File;

/**
 * Created by kc2ong on 14-2-26.
 */
public class Player {

    public Player(FREContext freContext) {
        this.context = freContext;
    }
    private FREContext context;

    /* 打开播放录音文件的程序 */
    public void openFile(String path, String name){
        File f = new File(path, name);
        Intent intent = new Intent();
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setAction( android.content.Intent.ACTION_VIEW );
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        this.context.getActivity().startActivity(intent);
    }

    private String getMIMEType(File f){
        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")|| end.equals("amr") || end.equals("mpeg")|| end.equals("mp4")){
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif")|| end.equals("png") || end.equals("jpeg")){
            type = "image";
        } else{
            type = "*";
        }
        type += "/*";
        return type;
    }
}
