package utils
{
	/**
	 * 交互编码
	 */	
	public class KeyCode
	{
		/** 拓展ID   */
		public static const EXTENSION_ID:String = "com.pamakids.IFlytek";
		
		//获取不同功能 context ==================================================================
		
		public static const CONTEXT_RECOGNIZER:String = "Recognizer";
		public static const CONTEXT_RECORDER:String = "Recorder";
		public static const CONTEXT_APKINSTALL:String = "ApkInstall";
		
		//讯飞语音+ 服务相关  =====================================================================
		
		/** 检测讯飞语音+是否已安装   */
		public static const KEY_CHECK_SERVICE_INSTALL:String = "check_service_install";
		/** 安装讯飞语音+  */	
		public static const KEY_SERVICE_INSTALL:String = "service_install";
		
		//离线关键词识别相关  ======================================================================
		
		/** 初始化识别控件   */	
		public static const KEY_INITRECOG:String = "initRecog";			//初始化识别控件
		/** 语法构建  */	
		public static const KEY_INITGRAMMAR:String = "initGrammar";		//构建语法
		/** 关键词库构建  */
		public static const KEY_LEXCION:String = "lexcion";				//更新词典
		/** 开始识别 */
		public static const KEY_START_RECOG:String = "startRecog";		//开始识别
		/** 结束识别，等待结果返回 */
		public static const KEY_STOP_RECOG:String = "stopRecog";		//结束识别
		/** 取消识别 */
		public static const KEY_CANCLE_RECOG:String = "cancleRecog";	//取消识别
		/** 识别录音文件 */
		public static const KEY_RECOG_AUDIO:String = "writeAudio";		//识别语音文件
		
		//录音功能=============================================================================
		
		/** 开始录音 */		
		public static const KEY_START_RECORD:String = "startRecord";	//开始录音
		/** 取消录音 */
		public static const KEY_STOP_RECORD:String = "stopRecord";		//录音结束
		
		//========================================================================
		
	}
}