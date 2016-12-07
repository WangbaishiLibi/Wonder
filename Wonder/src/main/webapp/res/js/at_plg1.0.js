/*
 * at-plugin
 * version: 1.0
 */
Alert_Info = function(){}



Alert_Info.fn = Alert_Info.prototype = {
	constructor: Alert_Info,
	title : "",
	content : "",
	at_dom : null,
	attibutes : null,
	type : "normal",	//弹出框类型：normal, alert, warn, confirm
	size : "md",		//弹出框大小：sm, md, lg
	enableDrag : true,
	dom : null,
	/*
	 * 参数：
	 * 	title, content, type, size, enableDrag, 
	 * 	width, height, left, top, at_dom, 
	 * 	closeEvent, confirmEvent
	 */
	init: function(opts){
		this.type = opts.type?opts.type:"normal";
		this.size = opts.size?opts.size:"md";
		this.enableDrag = (typeof opts.enableDrag)=="boolean"?opts.enableDrag:true;
		
		var dom = document.createElement("div");
		dom.className = "at";
	
		if(this.size == "sm"){
			dom.style.width = "430px";
			dom.style.height = "200px";
			dom.style.marginLeft = "-200px";
			dom.style.marginTop = "-100px";		
			content_height = 200 - 32;
		}else if(this.size == "md"){
			dom.style.width = "500px";
			dom.style.height = "300px";
			dom.style.marginLeft = "-250px";
			dom.style.marginTop = "-150px";
		}else if(this.size == "lg"){
			dom.style.width = "800px";
			dom.style.height = "600px";
			dom.style.marginLeft = "-400px";
			dom.style.marginTop = "-300px";
		}
		dom.style.left = "50%";
		dom.style.top ="50%";
		
		if(opts.width){dom.style.width = opts.width + "px"; dom.style.marginLeft = (-opts.width/2) + "px";}	
		if(opts.height){dom.style.height = opts.height + "px"; dom.style.marginTop = (-opts.height/2) + "px";}	
		
		if(opts.left){dom.style.left = opts.left; dom.style.marginLeft = "0px";}
		if(opts.top){dom.style.top = opts.top; dom.style.marginTop = "0px";}

		var content_height = parseInt(dom.style.height.replace(/px/, '')) - 32;
		var title_width = parseInt(dom.style.width.replace(/px/, '')) - 32;
		if(this.type == "normal" || this.type == "alert" || this.type == "warn"){
			var title_color = this.type == "warn"?"#E62E2E":"white";
			dom.innerHTML = 
			'<div class="at_title">' +
				'<div class="at_title_label" style="width: '+title_width+'px; color:'+title_color+';"></div>' + 
				'<div class="at_title_btns">' +
					'<button class="at_btn_close">×</button>' +
				'</div>' +
			'</div>' +
			'<div class="at_content" style="height:'+content_height+'px;"><div style="display:table;width:100%; height:100%;"><div class="at_content_label"></div></div></div>';
		}else if(this.type == "confirm"){
			content_height = content_height - 30 ;
			dom.innerHTML = 
			'<div class="at_title">' +
				'<div class="at_title_label" style="width: '+title_width+'px;"></div>' + 
				'<div class="at_title_btns">' +
					'<button class="at_btn_close">×</button>' +
				'</div>' +
			'</div>' +
			'<div class="at_content" style="height:'+content_height+'px;"><div style="display:table;width:100%; height:100%;"><div class="at_content_label"></div></div></div>' +
			'<div class="at_btns"><button class="at_btn_confirm">确认</button> <button class="at_btn_cancle" style="margin-left: 24px;">取消</button> <div>';
		}

		
		opts.title = opts.title?opts.title:"无标题";
		opts.content = opts.content?opts.content:"";
		
		dom = opts.at_dom?opts.at_dom:dom;
		var index = "id_" + parseInt(new Date().getTime() / 1000);
		dom.setAttribute("name", index);
		this.dom = dom;
		return index;
	},
	
	show : function(opts){
		this.remove();
		opts = opts?opts:{};
		this.init(opts);
		var dom = this.dom;
		document.getElementsByTagName("body")[0].appendChild(dom);
		
		if(opts.title.length > 20)	opts.title = opts.title.substr(0, 20) + "..";
		dom.getElementsByClassName("at_title_label")[0].innerText = opts.title;
		dom.getElementsByClassName("at_content_label")[0].innerHTML = opts.content;
		if(opts.scrollable)	dom.getElementsByClassName("at_content")[0].style.overflowY = "scroll";
		
		
		
		/*
		 * 事件处理
		 */
		var self = this;
		if(self.enableDrag){
			var title_dom = dom.getElementsByClassName("at_title")[0];
			title_dom.onmousedown = function(event){
				title_dom.style.cursor = "move";
				var orign_x = dom.offsetLeft, orign_y = dom.offsetTop;
				window.onmousemove = function(evt){		
					if(web.browser == "chrome"){
						self.locate(dom, orign_x + evt.x - event.x, orign_y + evt.y - event.y);
					}else{
						orign_x += evt.x - event.x;
						orign_y += evt.y - event.y;
						self.locate(dom, orign_x, orign_y);
					}
				}
			}
			title_dom.onmouseup = function(event){
				window.onmousemove = null;
				title_dom.style.cursor = "auto";
			}
		}
		
		dom.getElementsByClassName("at_btn_close")[0].onclick = function(evt){			
			if(opts.closeEvent) opts.closeEvent(evt);
			self.remove();
		}
		var btn_cancle = dom.getElementsByClassName("at_btn_cancle");
		if(btn_cancle.length){
			btn_cancle[0].onclick = function(evt){
				if(opts.closeEvent) opts.closeEvent(evt);
				self.remove();
			}
		}
		var btn_confirm = dom.getElementsByClassName("at_btn_confirm");
		if(btn_confirm.length){
			btn_confirm[0].onclick = function(evt){
				self.hide(dom_id);
				if(opts.confirmEvent) opts.confirmEvent(evt);
			}
		}
		this.dom = dom;
		return dom;
	},
	hide : function(){
		this.dom.style.display = "none";
	},
	remove : function(){
		if(this.dom){
			this.dom.remove();
		}
	},
	locate : function(dom, x, y){
		if(typeof dom == "string"){
			dom = this.at_dom.get(dom);
		}
		if(typeof dom == "object"){
			dom.style.left = x + "px";
			dom.style.top = y + "px";
			dom.style.marginLeft = "0px";
			dom.style.marginTop = "0px";
		} 
	},
	alert : function(_content){
		var opts = {
			"title" : "提示",
			"content" : _content,
			"width" : 250,
			"height" : 150,
			"type" : "alert"
		}
		return this.show(opts);
	},
	warn : function(_content){
		var opts = {
			"title" : "警告",
			"content" : _content,
			"width" : 250,
			"height" : 150,
			"type" : "warn"
		}
		return this.show(opts);
	},
	confirm : function(_title, _content, confirmEvent, _opts){
		_opts = _opts?_opts:{};
		_opts.title = _title;
		_opts.content = _content;
		_opts.type = _opts.type?_opts.type:"confirm";
		_opts.size = _opts.size?_opts.size:"sm";
		if(confirmEvent)	_opts.confirmEvent = confirmEvent;
		return this.show(_opts);
	}
}

var AT = new Alert_Info();