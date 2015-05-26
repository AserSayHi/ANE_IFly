package
{
	import com.pamakids.iflytek.controllers.Recognizer;
	import com.pamakids.iflytek.controllers.Recorder;
	import com.pamakids.iflytek.event.IFlytekRecogEvent;
	import com.pamakids.iflytek.event.IFlytekServiceEvent;
	import com.pamakids.iflytek.utils.ApkInstall;
	
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	
	public class ANE_IFlyTekTest extends Sprite
	{
		private var recog:Recognizer;
		private var record:Recorder;
		
		public function ANE_IFlyTekTest()
		{
			super();
			
			// 支持 autoOrient
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			
			var text:TextField = new TextField();
			text.text = "TEST";
			this.addChild( text );
			text.textColor = 0x333333;
			
			init();
		}
		
		private function onInstallFailed(e:IFlytekServiceEvent):void
		{
			trace("讯飞语音+ 安装失败！");
		}
		
		private function init(e:Event=null):void
		{
			var apkInstall:ApkInstall = ApkInstall.instance();
			trace(apkInstall);
			//是否安装了讯飞语音+
			if(!apkInstall.checkServiceInstall())
			{
				apkInstall.addEventListener(Event.ACTIVATE, init);
				apkInstall.addEventListener(IFlytekServiceEvent.INSTALL_SERVICE_FAILED, onInstallFailed);
				apkInstall.installService("assets/SpeechService_1.0.1063.mp3");
			}
			else
			{
				if(apkInstall.hasEventListener(IFlytekServiceEvent.INSTALL_SERVICE_FAILED))
					apkInstall.removeEventListener(IFlytekServiceEvent.INSTALL_SERVICE_FAILED, onInstallFailed);
				if(apkInstall.hasEventListener(Event.ACTIVATE))
					apkInstall.removeEventListener(Event.ACTIVATE, init);
				initRecog();
			}
		}
		
		private function initRecog():void
		{
			recog = new Recognizer();
			recog.addEventListener(IFlytekRecogEvent.INITIALIZE_SUCCESS, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.INITIALIZE_FAILED, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.INITGRAMMER_SUCCESS, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.INITGRAMMER_FAILED, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.UPDATE_LEXCION_FAILED, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.RECOG_BEGIN, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.RECOG_END, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.RECOG_ERROR, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.RECOG_RESULT, recogHandler);
			recog.addEventListener(IFlytekRecogEvent.VOLUME_CHANGED, recogHandler);
			recog.initialize();
			
			stage.addEventListener(MouseEvent.MOUSE_DOWN, onClick);
			stage.addEventListener(MouseEvent.MOUSE_UP, onClick);
		}
		
		private function recogHandler(e:IFlytekRecogEvent):void
		{
			switch(e.type)
			{
				case IFlytekRecogEvent.INITIALIZE_SUCCESS:
					trace("recog初始化完成");
					recog.initGrammar("assets/call.bnf");
					break;
				case IFlytekRecogEvent.INITIALIZE_FAILED:
					trace("recog初始化失败，错误码： " + e.message);
					break;
				case IFlytekRecogEvent.INITGRAMMER_SUCCESS:
					trace("recog语法构建成功");
					recog.updateLexcion("content", "run_stop");
					break;
				case IFlytekRecogEvent.INITGRAMMER_FAILED:
					trace("recog语法构建失败，错误码： " +　e.message);
					break;
				case IFlytekRecogEvent.UPDATE_LEXCION_SUCCESS:
					trace("recog词典更新成功");
					break;
				case IFlytekRecogEvent.UPDATE_LEXCION_FAILED:
					trace("词典更新失败，错误码： " + e.message);
					break;
				case IFlytekRecogEvent.RECOG_BEGIN:
					trace("语音识别开始！");
					break;
				case IFlytekRecogEvent.RECOG_END:
					trace("语音识别结束！");
					break;
				case IFlytekRecogEvent.RECOG_RESULT:
					trace("获取识别结果，语音内容为：" + e.message);
					break;
				case IFlytekRecogEvent.RECOG_ERROR:
					trace("识别出错，错误码： " + e.message);
					break;
				case IFlytekRecogEvent.VOLUME_CHANGED:
					trace("语音音量变化，当前音量值为：  " + e.message);
					break;
			}
		}
		
		private function onClick(e:MouseEvent):void
		{
			switch(e.type)
			{
				case MouseEvent.MOUSE_DOWN:
					recog.startRecog();
					break;
				case MouseEvent.MOUSE_UP:
					recog.stopRecog();
					break;
			}
		}
	}
}