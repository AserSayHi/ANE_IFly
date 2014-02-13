package event
{
	import flash.events.Event;
	
	public class IFlytekRecogEvent extends Event
	{
		public static const INITIALIZE_SUCCESS:String = "initRecog_success";
		public static const INITIALIZE_FAILED:String = "initRecog_failed";
		
		public static const INITGRAMMER_SUCCESS:String = "initRecog_grammer_success";
		public static const INITGRAMMER_FAILED:String = "initRecog_grammer_failed";
		
		public static const UPDATE_LEXCION_SUCCESS:String = "update_lexcion_success";
		public static const UPDATE_LEXCION_FAILED:String = "update_lexcion_failed";
		
		public static const RECOG_BEGIN:String = "recog_begin";
		public static const RECOG_END:String = "recog_end";
		public static const RECOG_RESULT:String = "recog_result";
		public static const RECOG_ERROR:String = "recog_error";
		public static const VOLUME_CHANGED:String = "volume_changed";
		
		public static const INSTALL_SERVICE_SUCCESS:String = "install_service_success";
		public static const INSTALL_SERVICE_FAILED:String = "install_service_failed";
		
		public function IFlytekRecogEvent(type:String, message:String=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this._message = message;
		}
		
		public function get message():String
		{
			return _message;
		}
		private var _message:String;
	}
}