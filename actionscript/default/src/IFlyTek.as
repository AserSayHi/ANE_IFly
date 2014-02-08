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
					throw new Error("ERROR - Extension context is null. ");
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
				case IFlytekEventCode.INITRECOG_SUCCESS:
					_instance.initRecogGrammer();
					break;
				case IFlytekEventCode.INITRECOG_FAILED:
					throw new Error("recognizer 初始化失败，错误码： " + e.level);
					break;
				case IFlytekEventCode.INITRECOG_GRAMMER_SUCCESS:
					_instance.updateLexcion();
					break;
				case IFlytekEventCode.INITRECOG_GRAMMER_FAILED:
					throw new Error("recognizer 语法构建失败，错误码： " + e.level);
					break;
				case IFlytekEventCode.RECOG_UPDATE_LEXCION_SUCCESS:
					if(_instance.initRecogCallback != null)
						_instance.initRecogCallback();
					break;
				case IFlytekEventCode.RECOG_UPDATE_LEXCION_FAILED:
					throw new Error("recognizer 词典更新失败，错误码： " + e.level);
					break;
			}
		}
		
		private var recog:Recognizer;
		private var synth:Synthesizer;
		
		/**
		 * 获取语音识别控件，同时构建语法，语法构建完成后调用onCompleted方法
		 * @param file			语法文件路径
		 * @param onCompleted	回调函数
		 * @return 
		 */		
		public function initRecognizer(file:String, key:String, onCompleted:Function=null):Recognizer
		{
			if(!recog)
			{
				recog = new Recognizer();
				this.grammerFile = file;
				this.initRecogCallback = onCompleted;
				recog.initRecog();
			}
			return recog;
		}
		private var initRecogCallback:Function;
		private var grammerFile:String;
		private var grammerKey:String;
		/**
		 * 语法构建
		 */		
		private function initRecogGrammer():void
		{
			recog.initGrammar( grammerFile );
		}
		private function updateLexcion():void
		{
			recog.updateLexcion(grammerFile, grammerKey);
		}
		
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