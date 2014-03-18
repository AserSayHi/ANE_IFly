package com.pamakids.iflytek.controllers
{
	import com.pamakids.iflytek.event.IFlytekRecogEvent;
	import com.pamakids.iflytek.utils.Contexts;
	import com.pamakids.iflytek.utils.KeyCode;
	
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;

	/**
	 * 识别控件
	 * @author Administrator
	 */	
	public class Recognizer extends EventDispatcher
	{
		private var context:ExtensionContext;
		
		public function Recognizer()
		{
		}
		
		private var initilalized:Boolean = false;
		public function checkInitialized():Boolean
		{
			return initilalized;
		}
		
		/**
		 * 控件初始化，初始化完成后派发  IFlytekRecogEvent.INITRECOG_SUCCESS 事件
		 */		
		public function initialize():void
		{
			if(initilalized)
				return;
			listeners = new Vector.<Function>();
			context = Contexts.instance().getContext( KeyCode.CONTEXT_RECOGNIZER);
			if(context)
			{
				context.addEventListener(StatusEvent.STATUS, onStatus);
				context.call( KeyCode.KEY_INITRECOG );
			}
		}
		
		protected function onStatus(e:StatusEvent):void
		{
			var event:IFlytekRecogEvent;
			switch(e.code)
			{
				case IFlytekRecogEvent.INITIALIZE_SUCCESS:
					initilalized = true;
				case IFlytekRecogEvent.INITGRAMMER_SUCCESS:
				case IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS:
				case IFlytekRecogEvent.RECOG_BEGIN:
				case IFlytekRecogEvent.RECOG_END:
					event = new IFlytekRecogEvent( e.code );
					break;
				case IFlytekRecogEvent.RECOG_RESULT:
					var str:String = e.level;
					var xml:XML = new XML(str);
					if(xml.rawtext)
						str = xml.rawtext.toString();
					else
						str = null;
					event = new IFlytekRecogEvent( e.code, str );
					break;
				case IFlytekRecogEvent.INITIALIZE_FAILED:
				case IFlytekRecogEvent.INITGRAMMER_FAILED:
				case IFlytekRecogEvent.UPDATE_LEXCION_FAILED:
				case IFlytekRecogEvent.RECOG_ERROR:
				case IFlytekRecogEvent.VOLUME_CHANGED:
					event = new IFlytekRecogEvent( e.code, e.level );
					break;
			}
			for each(var func:Function in listeners)
			{
				func(event);
			}
		}
		
		/**
		 * 词典更新
		 * @param key 语法文件中定义的槽名，槽名前后不需要加<>符号
		 * @param words 多个关键词以"_"连接
		 */		
		public function updateLexcion(key:String, words:String):void
		{
			if(context)
			{
				if(!key)
					throw new Error("无效槽名");
				if(!words)
					throw new Error("无有效关键词");
				key = "<" + key + ">";
				context.call( KeyCode.KEY_LEXCION, key, words);
			}
		}
		/**
		 * 语法构建
		 * @param urlFile 语法文件名称（包含路径）
		 */
		public function initGrammar(urlFile:String):void
		{	
			if(context)
				context.call( KeyCode.KEY_INITGRAMMAR, urlFile );
		}
		/**
		 * 开始识别
		 */		
		public function startRecog():void
		{
			if(context)
				context.call( KeyCode.KEY_START_RECOG );
		}
		/**
		 * 停止识别，等待结果返回
		 */		
		public function stopRecog():void
		{
			if(context)
				context.call( KeyCode.KEY_STOP_RECOG );
		}
		
		/**
		 * 取消识别
		 */		
		public function cancle():void
		{
			if(context)
				context.call( KeyCode.KEY_CANCLE_RECOG );
		}
		
		public function dispose():void
		{
			initilalized = false;
			listeners = null;
			if(context)
			{
				context.removeEventListener(StatusEvent.STATUS, onStatus);
				Contexts.instance().delContext( KeyCode.CONTEXT_RECOGNIZER );
				context = null;
			}
		}
		
		public function writeAudio(fileName:String):void
		{
			if(context)
				context.call( KeyCode.KEY_RECOG_AUDIO, fileName);
		}
		
		private var listeners:Vector.<Function>;
		public function addListener( listener:Function ):void
		{
			listeners.push( listener );
		}
		public function delListener( listener:Function ):void
		{
			var i:int = listeners.indexOf( listener );
			if(i != -1)
			{
				listeners.splice(i, 1);
			}
		}
	}
}