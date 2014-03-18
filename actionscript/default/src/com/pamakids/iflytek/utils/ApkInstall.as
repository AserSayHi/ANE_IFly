package com.pamakids.iflytek.utils
{
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	import com.pamakids.iflytek.event.IFlytekServiceEvent;
	
	public class ApkInstall extends EventDispatcher
	{
		
		private static var _instance:ApkInstall;
		
		public static function instance():ApkInstall
		{
			if(!_instance)
			{
				_instance = new ApkInstall();
			}
			return _instance;
		}
		public static function dispose():void
		{
			if(_instance)
			{
				_instance.dispose();
				_instance = null;
			}
		}
		
		private var context:ExtensionContext;
		public function ApkInstall()
		{
			context = Contexts.instance().getContext( KeyCode.CONTEXT_APKINSTALL );
			context.addEventListener(StatusEvent.STATUS, onStatus);
		}
		
		protected function onStatus(e:StatusEvent):void
		{
			if(e.code == IFlytekServiceEvent.INSTALL_SERVICE_FAILED)
			{
				dispatchEvent(new IFlytekServiceEvent( IFlytekServiceEvent.INSTALL_SERVICE_FAILED ));
			}
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
		
		private var called:Boolean = false;
		
		/**
		 * 为设备安装讯飞语音+
		 * @param apkUrl 语音服务apk相对路径
		 */	
		public function installService(apkUrl:String):void
		{
			if(context)
			{
				if(called)
					return;
				context.call( KeyCode.KEY_SERVICE_INSTALL, apkUrl );
				called = true;
			}
		}
		
		private function dispose():void
		{
			if(context)
			{
				context.removeEventListener(StatusEvent.STATUS, onStatus);
				Contexts.instance().delContext( KeyCode.CONTEXT_APKINSTALL );
				context = null;
			}
		}
	}
}

