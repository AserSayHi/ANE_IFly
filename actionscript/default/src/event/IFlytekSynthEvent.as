package event
{
	import flash.events.Event;
	
	public class IFlytekSynthEvent extends Event
	{
		public function IFlytekSynthEvent(type:String, message:String=null, bubbles:Boolean=false, cancelable:Boolean=false)
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