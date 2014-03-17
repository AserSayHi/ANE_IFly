package com.pamakids.iflytek.controllers
{
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	import com.pamakids.iflytek.event.IFlytekRecogEvent;
	
	import com.pamakids.iflytek.utils.Contexts;
	import com.pamakids.iflytek.utils.KeyCode;

	/**
	 * 识别控件
	 * @author Administrator
	 */	
	public class Recognizer extends EventDispatcher
	{
		private var context:ExtensionContext;
		
		public function Recognizer()
		{
			context = Contexts.instance().getContext( KeyCode.CONTEXT_RECOGNIZER);
			context.addEventListener(StatusEvent.STATUS, onStatus);
		}
		
		/**
		 * 控件初始化，初始化完成后派发  IFlytekRecogEvent.INITRECOG_SUCCESS 事件
		 */		
		public function initialize():void
		{
			if(context)
				context.call( KeyCode.KEY_INITRECOG );
		}
		
		protected function onStatus(e:StatusEvent):void
		{
			switch(e.code)
			{
				case IFlytekRecogEvent.INITIALIZE_SUCCESS:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.INITIALIZE_SUCCESS ) );
					break;
				case IFlytekRecogEvent.INITIALIZE_FAILED:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.INITIALIZE_FAILED, e.level ));
					break;
				case IFlytekRecogEvent.INITGRAMMER_SUCCESS:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.INITGRAMMER_SUCCESS ));
					break;
				case IFlytekRecogEvent.INITGRAMMER_FAILED:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.INITGRAMMER_FAILED, e.level ));
					break;
				case IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS ));
					break;
				case IFlytekRecogEvent.UPDATE_LEXCION_FAILED:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.UPDATE_LEXCION_FAILED, e.level ));
					break;
				case IFlytekRecogEvent.RECOG_BEGIN:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.RECOG_BEGIN ));
					break;
				case IFlytekRecogEvent.RECOG_END:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.RECOG_END ));
					break;
				case IFlytekRecogEvent.RECOG_RESULT:
					var str:String = e.level;
					var xml:XML = new XML(str);
					if(xml.rawtext)
						str = xml.rawtext.toString();
					else
						str = null;
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.RECOG_RESULT, str ));
					break;
				case IFlytekRecogEvent.RECOG_ERROR:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.RECOG_ERROR, e.level ));
					break;
				case IFlytekRecogEvent.VOLUME_CHANGED:
					dispatchEvent( new IFlytekRecogEvent( IFlytekRecogEvent.VOLUME_CHANGED, e.level ));
					break;
			}
		}
		
		/**
		 * 词典更新
		 * @param key 语法文件中定义的槽名，槽名前后不需要加<>符号
		 * @param words 多个关键词以"_"连接
		 * @param cover 是否覆盖原词库
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
	}
}