package
{
	import flash.external.ExtensionContext;

	/**
	 * 识别控件
	 * @author Administrator
	 */	
	public class Recognizer
	{
		private var extension:ExtensionContext;
		
		public function Recognizer()
		{
		}
		
		internal function set context(_context:ExtensionContext):void
		{
			if(!_context)
			{
				trace("Error: _context不存在， 所在位置：Recognizer 第21行！");
				return;
			}
			extension = _context;
		}
		
		
		public function startRecog():void
		{
		}
		
	}
}