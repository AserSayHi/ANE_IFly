package controllers
{
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	import event.IFlytekRecordEvent;
	
	import utils.Contexts;
	import utils.KeyCode;
	
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
		
		public function dispose():void
		{
			context.removeEventListener(StatusEvent.STATUS, onStatus);
			Contexts.instance().delContext( KeyCode.CONTEXT_RECORDER );
			context = null;
		}
	}
}