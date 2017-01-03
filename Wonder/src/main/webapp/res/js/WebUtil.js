/*
 * WebUtil.js 1.0
 * js配置常量、常用函数
 */

WebUtil = function(){
	this.init();
}

WebUtil.fn = WebUtil.prototype = {
	constructor: WebUtil,
	basePath: "/Wonder/",		//项目根目录
	resPath: "/Wonder/res/",	//静态资源根目录
	browser: "",
	getHttpRequest: function(){
	    if ( window.XMLHttpRequest ) 		// Gecko 
	        return new XMLHttpRequest() ; 
	    else if ( window.ActiveXObject ) 	// IE 
	        return new ActiveXObject("MsXml2.XmlHttp") ; 
	},
	loadSource: function(type, url, asyn){
		asyn = asyn?true:false;
		if(type == "script"){
			var oXmlHttp = this.getHttpRequest();
		    oXmlHttp.onreadystatechange = function(){
		        if (oXmlHttp.readyState == 4){
		            if ( oXmlHttp.status == 200 || oXmlHttp.status == 304 ) {
	                    var oScript = document.createElement("script"); 
	                    oScript.language = "javascript"; 
	                    oScript.type = "text/javascript"; 
	                    oScript.defer = true; 
	                    oScript.text = oXmlHttp.responseText; 
	                    document.head.appendChild(oScript); 
		            } else {
		            	alert( 'XML request error: ' + oXmlHttp.statusText + ' (' + oXmlHttp.status + ')' ) ; 
		            }

		        } 
		    };
		    oXmlHttp.open('GET', url, true);
		    oXmlHttp.send(null);
		}else if(type == "css"){
			var link = document.createElement("link");
			link.href = url;
			link.type = "text/css";
			link.rel = "stylesheet";
			document.head.appendChild(link);
		}

	}
}

/*
 * 项目初始化时加载资源
 */
WebUtil.fn.init = function(){
	//jquery2.1
//	this.loadSource("script", this.resPath+"js/jquery.min.js");
	//bootstrap3.3.5
	this.loadSource("css", this.resPath+"dist/bootstrap/css/bootstrap.min.css");
	this.loadSource("css", this.resPath+"dist/bootstrap/css/bootstrap-theme.min.css");
	this.loadSource("script", this.resPath+"dist/bootstrap/js/bootstrap.min.js");
	this.loadSource("script", this.resPath+"js/at_plg1.0.js");
	
	if (navigator.userAgent.indexOf("Chrome") > 1)
    	this.browser = "chrome";
 //   else if (navigator.userAgent.indexOf("MSIE") > 1)
    else this.browser = "ie";
}


/*
 * 跳转时新建标签页
 */
WebUtil.fn.goto_blank = function(url){
	var link = document.createElement("a");
	link.href = url;
	link.target = "_blank";
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
}

/*
 * 初始化拖拽操作
 */
WebUtil.fn.initDragOperation = function (dom){
	dom.onmousedown = function(event){
		dom.style.cursor = "move";
		var orign_x = dom.offsetLeft, orign_y = dom.offsetTop;
		window.onmousemove = function(evt){		
			var x = orign_x + evt.x - event.x;
			var y = orign_y + evt.y - event.y;
			dom.style.left = x + "px";
			dom.style.top = y + "px";
		}
	}
	dom.onmouseup = function(event){
		window.onmousemove = null;
		dom.style.cursor = "auto";
	}
}

/*
 * 获取url参数
 */
WebUtil.fn.getUrlParam = function(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) return decodeURI(r[2]); return null;
}


/*
 * 初始化部门树
 */
WebUtil.fn.initTree = function($dom){
	return $dom.jstree({
 		"plugins" : [ "contextmenu", "state" ],
 		"core" : {
 			"check_callback" : true,
 		      "data" :  {
 		    	 "url" : web.basePath+"getTreeData.do",
 		    	 "dataType" : "JSON",
 		    	 "type" : "GET"
 		      }
 		},
 		"contextmenu" : {
			 "items":{
				 "create":{
					 "label":"创建",
					 "action":function(obj){
						 console.log("create--->"+$(this));
						 window.location='adduser.html?did='+$('#jstree_div').jstree(true).get_selected(true)[0].id;
					 }
				 },
				 "remove":{
					 "label":"删除",
					 "action":function(obj){
						 console.log("delete--->"+obj);
					 }
				 }
			 }
	 	 }    
	 });
}

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
 var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
}




var web = new WebUtil();




/*
 * ajax设置
 */
$(function(){

	$.ajaxSetup({
		async : false,	//同步
		beforeSend : function(xhr){
			//设置ajax安全验证参数
			if(this.url.indexOf("?")>0){	
				this.url += "&ajaxStatus=1";	
			}else{
				this.url += "?ajaxStatus=1";
			}
		},
		error : function(response){
			return;
			//用户未登录时的ajax请求状态
			if(response.status == 701)
				window.location = web.basePath + "html/login.html";
			if(response.status == 500){}
				
		}
	
	});		
})
