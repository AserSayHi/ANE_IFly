package
{
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;

	/**
	 * 识别控件
	 * @author Administrator
	 */	
	public class Recognizer
	{
		private const KEY_INITRECOG:String = "initRecog";		//初始化识别控件
		private const KEY_INITGRAMMAR:String = "initGrammar";	//构建语法
		private const KEY_LEXCION:String = "lexcion";			//更新词典
		private const KEY_START_RECOG:String = "startRecog";	//开始识别
		private const KEY_STOP_RECOG:String = "stopRecog";		//结束识别
		private const KEY_CANCLE_RECOG:String = "cancle";		//取消识别
		
		private var extension:ExtensionContext;
		
		public function Recognizer()
		{
		}
		
		/**
		 * 结果回调
		 * @param func
		 */		
		public function set resultCallback(func:Function):void
		{
			_resultHandler = func;
		}
		private var _resultHandler:Function;
		/**
		 * 词典更新
		 * @param title 词库名称，格式为"<titleName>"
		 * @param words 多个关键词以"_"连接
		 */		
		public function updateLexcion(title:String, words:String):void
		{
			if(extension)
			{
				if(!title)
					throw new Error("词库名称为空");
				if(title.charAt(0) != "<" || title.charAt(title.length-1) != ">")
					throw new Error("词库名称无效，请在词库名称前后分别添加<、>两个符号");
				if(!words)
					throw new Error("无有效关键词");
				extension.call( KEY_LEXCION, title, words );
			}
		}
		/**
		 * 语法构建
		 * @param urlFile 语法文件名称（包含路径）
		 */
		public function initGrammar(urlFile:String):void
		{	
			if(extension)
				extension.call( KEY_INITGRAMMAR, urlFile );
		}
		/**
		 * 开始识别
		 */		
		public function startRecog():void
		{
			if(extension)
				extension.call( KEY_START_RECOG );
		}
		/**
		 * 停止识别，等待结果返回
		 */		
		public function stopRecog():void
		{
			if(extension)
				extension.call( KEY_STOP_RECOG );
		}
		/**
		 * 取消识别
		 */		
		public function cancle():void
		{
			if(extension)
				extension.call( KEY_CANCLE_RECOG );
		}
		
		
		private var initCallback:Function;
		internal function initRecog():void
		{
			extension = IFlyTek.context;
			if(extension)
				extension.call( KEY_INITRECOG );
		}
		private function statusListener(e:StatusEvent):void
		{
		}
	}
}