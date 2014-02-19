package
{
	import flash.events.EventDispatcher;
	import flash.external.ExtensionContext;

	/**
	 * 识别控件
	 * @author Administrator
	 */	
	public class Recognizer extends EventDispatcher
	{
		internal var extension:ExtensionContext;
		
		public function Recognizer()
		{
			extension = IFlyTek.context;
		}
		
		/**
		 * 词典更新
		 * @param key 语法文件中定义的槽名，槽名前后不需要加<>符号
		 * @param words 多个关键词以"_"连接
		 * @param cover 是否覆盖原词库
		 */		
		public function updateLexcion(key:String, words:String):void
		{
			if(extension)
			{
				if(!key)
					throw new Error("无效槽名");
				if(!words)
					throw new Error("无有效关键词");
				key = "<" + key + ">";
				extension.call( KeyCode.KEY_LEXCION, key, words);
			}
		}
		/**
		 * 语法构建
		 * @param urlFile 语法文件名称（包含路径）
		 */
		public function initGrammar(urlFile:String):void
		{	
			if(extension)
				extension.call( KeyCode.KEY_INITGRAMMAR, urlFile );
		}
		/**
		 * 开始识别
		 */		
		public function startRecog():void
		{
			if(extension)
				extension.call( KeyCode.KEY_START_RECOG );
		}
		/**
		 * 停止识别，等待结果返回
		 */		
		public function stopRecog():void
		{
			if(extension)
				extension.call( KeyCode.KEY_STOP_RECOG );
		}
		/**
		 * 取消识别
		 */		
		public function cancle():void
		{
			if(extension)
				extension.call( KeyCode.KEY_CANCLE_RECOG );
		}
		/**
		 * 控件初始化，初始化完成后派发  IFlytekRecogEvent.INITRECOG_SUCCESS 事件
		 */		
		public function initialize():void
		{
			if(extension)
				extension.call( KeyCode.KEY_INITRECOG );
		}
		
		public function dispose():void
		{
			extension = null;
		}
	}
}