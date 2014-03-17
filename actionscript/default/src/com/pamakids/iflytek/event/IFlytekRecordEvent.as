package com.pamakids.iflytek.event
{
	import flash.events.Event;
	
	public class IFlytekRecordEvent extends Event
	{
		//public static const INITIALIZE_SUCCESS:String = "initRecord_success";
		
		public static const RECORD_BEGIN:String = "record_begin";
		
		public static const RECORD_END:String = "record_end";
		
		public function IFlytekRecordEvent(type:String, message:String=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_message = message;
		}
		
		public function get message():String
		{
			return _message;
		}
		private var _message:String;
	}
}