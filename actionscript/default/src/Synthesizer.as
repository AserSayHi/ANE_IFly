package
{
	import flash.events.EventDispatcher;
	import flash.external.ExtensionContext;

	public class Synthesizer extends EventDispatcher
	{
		private var extension:ExtensionContext;
		
		public function Synthesizer()
		{
			extension = IFlyTek.context;
		}
		
		public function dispose():void
		{
			extension = null;
		}
	}
}