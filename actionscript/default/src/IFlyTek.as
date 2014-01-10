package
{
	import flash.external.ExtensionContext;

	public class IFlyTek
	{
		
		private static var _instance:IFlyTek;
		
		private static var context:ExtensionContext;
		
		private static const EXTENSION_ID:String = "com.pamakids.IFlytek";
		
		public static function get instance():IFlyTek
		{
			if (!_instance)
			{
				_instance=new IFlyTek();
				context = ExtensionContext.createExtensionContext(EXTENSION_ID, null);
				if(!context)
					trace("ERROR - Extension context is null. Please check if extension.xml is setup correctly.");
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
		
		
		public function get synthesizer():Synthesizer
		{
			if(synth)
			{
				synth = new Synthesizer();
				synth.context = context;
			}
			return synth;
		}
		
	}
}