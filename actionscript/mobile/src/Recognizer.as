package
{
	import flash.events.Event;
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
			extension.addEventListener( "start", statusHandler );
			extension.addEventListener( "result", statusHandler );
		}
		
		protected function statusHandler(event:Event):void
		{
			trace(event.target);
			trace(event.type);
			if( event.type == "result" )
			{
				if( _resultHandler != null )
					_resultHandler( event );
			}
		}		
		
		public function startRecog( startCallback:Function=null ):void
		{
			extension.call( "start" );
		}
		
		/**
		 * 结果回调
		 * @param func
		 */		
		public function set resultCallback(func:Function):void
		{
			_resultHandler = func;
		}
		private var _resultHandler:Function;
		
	}
}