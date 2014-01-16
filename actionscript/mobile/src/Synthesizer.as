package
{
	import flash.external.ExtensionContext;

	public class Synthesizer
	{
		private var extension:ExtensionContext;
		
		public function Synthesizer()
		{
			extension = IFlyTek.context;
		}
	}
}