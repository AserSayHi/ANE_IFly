package com.pamakids.iflytek.controllers
{
	import com.pamakids.iflytek.event.IFlytekRecordEvent;
	import com.pamakids.iflytek.utils.Contexts;
	import com.pamakids.iflytek.utils.KeyCode;
	
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	public class Recorder extends EventDispatcher
	{
		
		private var context:ExtensionContext;
		
		public function Recorder()
		{
			context = Contexts.instance().getContext( KeyCode.CONTEXT_RECORDER );
			context.addEventListener(StatusEvent.STATUS, onStatus);
		}
		
		protected function onStatus(e:StatusEvent):void
		{
			switch(e.code)
			{
				case IFlytekRecordEvent.RECORD_BEGIN:
					dispatchEvent( new IFlytekRecordEvent( IFlytekRecordEvent.RECORD_BEGIN ));
					break;
				case IFlytekRecordEvent.RECORD_END:
					dispatchEvent( new IFlytekRecordEvent( IFlytekRecordEvent.RECORD_END ));
					break;
			}
		}
		
		 /**
		  * 初始化
		  */		
		public function initialize():void
		{
			if(context)
				context.call(KeyCode.KEY_INIT_RECORD);
		}
		/**
		 * @param fileName	录音存储文件名（不包含后缀，默认为amr格式存储）
		 */		
		public function startRecord(fileName:String):void
		{
			if(context)
				context.call( KeyCode.KEY_START_RECORD, fileName );
		}
		
		public function stopRecord():void
		{
			if(context)
				context.call( KeyCode.KEY_STOP_RECORD );
		}
		
		public function play(fileName:String):void
		{
			if(context)
				context.call( KeyCode.KEY_AUDIO_PLAY, fileName );
		}
		
		public function dispose():void
		{
			context.removeEventListener(StatusEvent.STATUS, onStatus);
			Contexts.instance().delContext( KeyCode.CONTEXT_RECORDER );
			context = null;
		}
	}
}