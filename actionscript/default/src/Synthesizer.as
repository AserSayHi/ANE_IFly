package
{
	import flash.external.ExtensionContext;

	public class Synthesizer
	{
		private var extension:ExtensionContext;
		
		public function Synthesizer()
		{
		}
		
		internal function set context(_context:ExtensionContext):void
		{
			if(!_context)
			{
				trace("Error: _context不存在， 所在位置：Synthesizer 第18行！");
				return;
			}
			extension = _context;
		}
	}
}