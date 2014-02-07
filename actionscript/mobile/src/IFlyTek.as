package
{
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;

	public class IFlyTek
	{
		
		private static var _instance:IFlyTek;
		
		internal static var context:ExtensionContext;
		
		private static const EXTENSION_ID:String = "com.pamakids.IFlytek";
		
		private static var APPID:String = "appid=52b7d6b1";
		
		public static function get instance():IFlyTek
		{
			if (!_instance)
			{
				_instance=new IFlyTek();
				context = ExtensionContext.createExtensionContext(EXTENSION_ID, APPID);
				if(!context)
				{
					trace("ERROR - Extension context is null. ");
				}else
				{
					context.addEventListener(StatusEvent.STATUS, onStatus);
				}
			}
			return _instance;
		}
		
		private static function onStatus(e:StatusEvent):void
		{
			switch(e.code)
			{
				case IFlytekEventType.INITRECOG_SUCCESS:
					if(instance.initListener!=null)
						instance.initListener();
					break;
				case IFlytekEventType.INITRECOG_FAILED:
					trace("recognizer 初始化失败，错误码： " + e.level);
					break;
			}
		}
		
		private var recog:Recognizer;
		private var synth:Synthesizer;
		
		/**
		 * 获取语音识别控件
		 * @return 
		 */		
		public function initRecognizer(initListener:Function):Recognizer
		{
			if(!recog)
			{
				recog = new Recognizer();
				this.initListener = initListener;
				recog.initRecog();
			}
			return recog;
		}
		private var initListener:Function;
		
		/**
		 * 获取语音合成控件
		 * @return 
		 */		
		public function initSynthesizer(initListener:Function):Synthesizer
		{
			if( synth )
			{
				synth = new Synthesizer();
			}
			return synth;
		}
		
	}
}