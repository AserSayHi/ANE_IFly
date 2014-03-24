package com.pamakids.IFlytek.utils;

/**
 * 交互编码
 * Created by kc2ong on 14-2-25.
 */
public class KeyCode {

    /** 拓展ID   */
    public static final String EXTENSION_ID = "com.pamakids.IFlytek";

    //获取不同功能 context ==================================================================

    public static final String CONTEXT_RECOGNIZER = "Recognizer";
    public static final String CONTEXT_RECORDER = "Recorder";
    public static final String CONTEXT_APKINSTALL = "ApkInstall";

    //讯飞语音+ 服务相关  =====================================================================

    /** 检查设备是否已安装讯飞语音+ */
    public static final String KEY_CHECK_SERVICE_INSTALL = "check_service_install";
    /** 安装讯飞语音+ */
    public static final String KEY_SERVICE_INSTALL = "service_install";

    //离线关键词识别相关  ======================================================================

    /** 初始化识别控件 */
    public static final String KEY_INITRECOG = "initRecog";
    /** 语法构建 */
    public static final String KEY_INITGRAMMAR = "initGrammar";
    /** 关键词库构建 */
    public static final String KEY_LEXCION = "lexcion";
    /** 开始识别 */
    public static final String KEY_START_RECOG = "startRecog";
    /** 结束识别，等待结果返回 */
    public static final String KEY_STOP_RECOG = "stopRecog";
    /** 取消识别 */
    public static final String KEY_CANCLE_RECOG = "cancleRecog";
    /** 识别录音文件 */
    public static final String KEY_RECOG_AUDIO = "writeAudio";		//识别语音文件

    //录音功能=============================================================================

    public static final String KEY_INIT_RECORD = "initRecord";
    /** 开始录音 */
    public static final String KEY_START_RECORD = "startRecord";
    /** 取消录音 */
    public static final String KEY_STOP_RECORD = "stopRecord";
    /** 播放录音 */
    public static final String KEY_AUDIO_PLAY = "playAudio";

    // 播放功能 ===========================================================================


}
