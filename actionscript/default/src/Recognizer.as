package
{
	import flash.events.Event;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;

	/**
	 * 识别控件
	 * @author Administrator
	 */	
	public class Recognizer
	{
		private var extension:ExtensionContext;
		
		public function Recognizer()
		{
		}
		
		protected function statusHandler(event:Event):void
		{
			trace(event.target);
			trace(event.type);
			if( event.type == "result" )
			{
				if( _resultHandler != null )
					_resultHandler( event );
			}
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
		public function updateDictionary(title:String, words:String):void
		{
			if(extension)
				extension.call( "lexcion", words );
		}
		/**
		 * 语法构建
		 * @param urlFile 语法文件名称（包含路径）
		 */
		public function initGrammar(urlFile:String):void
		{	
			if(extension)
				extension.call( "initGrammar", urlFile );
		}
		/**
		 * 开始识别
		 */		
		public function startRecog():void
		{
			if(extension)
				extension.call( "startRecog" );
		}
		/**
		 * 停止识别，等待结果返回
		 */		
		public function stopRecog():void
		{
			if(extension)
				extension.call( "stopRecog" );
		}
		/**
		 * 取消识别
		 */		
		public function cancle():void
		{
			if(extension)
				extension.call( "cancle" );
		}
		
		
		private var initCallback:Function;
		internal function initRecog():void
		{
			extension = IFlyTek.context;
			if(extension)
				extension.call( "initRecog" );
		}
		private function statusListener(e:StatusEvent):void
		{
		}
	}
}