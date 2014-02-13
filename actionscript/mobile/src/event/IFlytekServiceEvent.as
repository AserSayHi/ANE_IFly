package event
{
	import flash.events.Event;
	
	public class IFlytekServiceEvent extends Event
	{
		public static const INSTALL_SERVICE_FAILED:String = "install_service_failed";
		public static const INSTALL_SERVICE_SUCCESS:String = "install_service_success";
		
		public function IFlytekServiceEvent(type:String, message:String=null, bubbles:Boolean=false, cancelable:Boolean=false)
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