package
{
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	import event.IFlytekRecogEvent;
	import event.IFlytekServiceEvent;
	import event.IFlytekSynthEvent;

	/**
	 * v1
	 * @author kc2ong
	 */	
	public class IFlyTek extends EventDispatcher
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
					throw new Error("ERROR - Extension context is null.");
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
				case IFlytekRecogEvent.INITIALIZE_SUCCESS:
					dispatchRecogEvent( IFlytekRecogEvent.INITIALIZE_SUCCESS );
					break;
				case IFlytekRecogEvent.INITIALIZE_FAILED:
					dispatchRecogEvent( IFlytekRecogEvent.INITIALIZE_FAILED, e.level );
					break;
				case IFlytekRecogEvent.INITGRAMMER_SUCCESS:
					dispatchRecogEvent( IFlytekRecogEvent.INITGRAMMER_SUCCESS );
					break;
				case IFlytekRecogEvent.INITGRAMMER_FAILED:
					dispatchRecogEvent( IFlytekRecogEvent.INITGRAMMER_FAILED, e.level );
					break;
				case IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS:
					dispatchRecogEvent( IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS );
					break;
				case IFlytekRecogEvent.UPDATE_LEXCION_FAILED:
					dispatchRecogEvent( IFlytekRecogEvent.UPDATE_LEXCION_FAILED, e.level );
					break;
				case IFlytekRecogEvent.RECOG_BEGIN:
					dispatchRecogEvent( IFlytekRecogEvent.RECOG_BEGIN );
					break;
				case IFlytekRecogEvent.RECOG_END:
					dispatchRecogEvent( IFlytekRecogEvent.RECOG_END );
					break;
				case IFlytekRecogEvent.RECOG_RESULT:
					dispatchRecogEvent( IFlytekRecogEvent.RECOG_RESULT, e.level );
					break;
				case IFlytekRecogEvent.RECOG_ERROR:
					dispatchRecogEvent( IFlytekRecogEvent.RECOG_ERROR, e.level );
					break;
				case IFlytekRecogEvent.VOLUME_CHANGED:
					dispatchRecogEvent( IFlytekRecogEvent.VOLUME_CHANGED, e.level );
					break;
				case IFlytekServiceEvent.INSTALL_SERVICE_FAILED:
					_instance.dispatchEvent(new IFlytekServiceEvent( IFlytekServiceEvent.INSTALL_SERVICE_FAILED ));
					break;
				case IFlytekServiceEvent.INSTALL_SERVICE_SUCCESS:
					_instance.dispatchEvent(new IFlytekServiceEvent( IFlytekServiceEvent.INSTALL_SERVICE_SUCCESS ));
					break;
			}
		}
		
		private static function dispatchRecogEvent(type:String, message:String=null):void
		{
			_instance.recog.dispatchEvent( new IFlytekRecogEvent( type, message ) );
		}
		private static function dispatchSynthEvent(type:String, message:String=null):void
		{
			_instance.synth.dispatchEvent( new IFlytekSynthEvent( type, message ) );
		}
		
		private var recog:Recognizer;
		private var synth:Synthesizer;
		
		/**
		 * 获取语音识别控件
		 * @return 
		 */		
		public function initRecognizer():Recognizer
		{
			if(!recog)
				recog = new Recognizer();
			return recog;
		}
		
		/**
		 * 获取语音合成控件
		 * @return 
		 */		
		public function initSynthesizer(initListener:Function):Synthesizer
		{
			if( synth )
				synth = new Synthesizer();
			return synth;
		}
		
		/**
		 * 检测设备是否已安装 讯飞语音+ 
		 */		
		public function checkServiceInstall():Boolean
		{
			if(context)
				return context.call( KeyCode.KEY_CHECK_SERVICE_INSTALL ) as Boolean;
			return false;
		}
		
		/**
		 * 安装 讯飞语音+
		 */		
		public function installService(apkUrl:String):void
		{
			if(context)
				context.call( KeyCode.KEY_SERVICE_INSTALL, apkUrl );
		}
	}
}