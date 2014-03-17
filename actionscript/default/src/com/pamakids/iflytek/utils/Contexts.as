package com.pamakids.iflytek.utils
{
	import flash.external.ExtensionContext;
	import flash.utils.Dictionary;
	
	public class Contexts
	{
		
		private static var _instance:Contexts;
		public static function instance():Contexts
		{
			if(!_instance)
				_instance = new Contexts(new MyClass());
			return _instance;
		}
		
		public function Contexts(myClass:MyClass)
		{
			if(!myClass)
				throw new Error("初始化失败，请使用公开的类静态方法获取单例对象!");
			dic = new Dictionary();
		}
		
		private var dic:Dictionary;
		
		public function getContext(tag:String):ExtensionContext
		{
			var context:ExtensionContext = dic[tag];
			if(!context)
			{
				context = ExtensionContext.createExtensionContext(KeyCode.EXTENSION_ID, tag);
				if(!context)
					throw new Error( "Error: Extension context is null! TAG = " + tag);
				dic[tag] = context;
			}
			return context;
		}
		
		public function delContext(tag:String):void
		{
			var context:ExtensionContext = dic[tag];
			if(context)
			{
				context.dispose();
				delete dic[tag];
			}
		}
		
		public function deleteAll():void
		{
			for(var tag:String in dic)
			{
				delContext(tag);
			}
			dic = null;
		}
	}
}

class MyClass{}