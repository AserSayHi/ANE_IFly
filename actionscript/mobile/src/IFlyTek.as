package
{
	import flash.external.ExtensionContext;

	public class IFlyTek
	{
		
		private static var _instance:IFlyTek;
		
		private static var context:ExtensionContext;
		
		private static const EXTENSION_ID:String = "com.pamakids.IFlytek";
		
		public static var APPID:String = "appid=52b7d6b1";
		
		public static function get instance():IFlyTek
		{
			if (!_instance)
			{
				_instance=new IFlyTek();
				context = ExtensionContext.createExtensionContext(EXTENSION_ID, APPID);
				if(!context)
					trace("ERROR - Extension context is null. Please check if extension.xml is setup correctly.");
				else
					context.call( "init" );
			}
			return _instance;
		}
		
		private var recog:Recognizer;
		private var synth:Synthesizer;
		
		/**
		 * 获取语音识别控件
		 * @return 
		 */		
		public function get recognizer():Recognizer
		{
			if(!recog)
			{
				recog = new Recognizer();
				recog.context = context;
			}
			return recog;
		}
		
		/**
		 * 获取语音合成控件
		 * @return 
		 */		
		public function get synthesizer():Synthesizer
		{
			if( synth )
			{
				synth = new Synthesizer();
				synth.context = context;
			}
			return synth;
		}
		
	}
}