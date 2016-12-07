/*
 * table.js
 */

var table;				//表格对象

$(function(){
	

	table = new DataTable({
		tbname : "B_SCHEMA_INFO",
		tabledom : $("#table"),
		ajax : {url:web.basePath+"table/entityList/B_SCHEMA_INFO"},
		columns : [
		    {name : "序号", field : "RN"},       
			{name : "中文表名", field : "CN_TABLENAME"},
			{name : "英文表名", field : "EN_TABLENAME"},
			{name : "中文字段名", field : "CN_FIELDNAME"},
			{name : "英文字段名", field : "EN_FIELDNAME"},
			{name : "排序号", field : "SEQ"},
			{name : "操作", field : function(row){
				return '<span rowid="'+row.TID+'" class="op_modify glyphicon glyphicon-edit">修改</span>' + 
				'<span rowid="'+row.TID+'" class="op_delete glyphicon glyphicon-remove">删除</span>';
			}}
		],
		page : {
			page_index : 1,		
			page_total : 10	//每页记录数
		}
	});
	updateTableInfo(table.tbname)
	
	$("#btn_add").click(function(){
		$("#addModal").find("input[name=tbname]").val(table.tbname);
		updateColumnInfo($("#addModal"));
	})
	
	$(document).on("click", ".op_modify", function(){
		$("#modifyModal").find("input[name=tbname]").val(table.tbname);
		$("#modifyModal").modal("show");
		updateColumnInfo($("#modifyModal"), $(this).attr("rowid"));
	})
	
	$(document).on("click", ".op_delete", function(){
		$("#deleteModal").find("input[name=tbname]").val(table.tbname);
		$("#deleteModal").modal("show");
		updateColumnInfo($("#deleteModal"), $(this).attr("rowid"));
	})
	
	$(".save").click(function(){
		var modalId = $(this).parents(".modal").attr("id");
		modifyEntity($("#"+modalId))
	});
})

/*
 * 更新数据表信息
 */
function updateTableInfo(tbname){
	table.tbname = tbname;
	$.getJSON(web.basePath + "table/entityInfo/"+tbname, function(data){
		table.col_info = data;
	});
}


/*
 * 更新弹出框字段信息
 */
function updateColumnInfo($modal, rowid){
	var tbname = $modal.find("input[name=tbname]").val();
	if(!tbname)	return;
	
	if(table.col_info){
		var data = table.col_info;
		var parms = '<input type="hidden"  name="tbname" value="'+tbname+'">';
		$modal.find(".modal-body").html(parms);
		
				//删除时，只需加载关键字段
		if($modal.find("input[name=_method]").val() == "DELETE"){
			for(var i in data){
				if(data[i].CONSTRAINT_TYPE){
					var tmp = '<input type="hidden"  name="'+data[i].COLUMN_NAME+'" value="'+rowid+'">';
					$modal.find(".modal-body").append(tmp);
					break;
				}
			}
			$modal.find(".modal-body").append("确认删除记录"+rowid);
		}else{	//新增和修改时，需要加载所有字段
			for(var i in data){
				var tmp = '<div class="form-group">' +
				'<div class="col-sm-2 control-label">'+data[i].COMMENTS+'</div>' +
				'<div class="col-sm-10">' +
					'<input type="text" class="form-control" dataType="'+data[i].DATA_TYPE+'" name="'+data[i].COLUMN_NAME+'" required>' +
				'</div></div>';
				$modal.find(".modal-body").append(tmp);
			}
		}
		
		
		//修改时，需要加载所有字段值
		if($modal.find("input[name=_method]").val() == "PUT"){
			var url = "table/entity?tbname="+tbname+"&";
			for(var i in data){
				if(data[i].CONSTRAINT_TYPE){
					url += data[i].COLUMN_NAME+"="+rowid;
					break;
				}
			}
			$.getJSON(web.basePath + url, function(data){
				for(var i in data){
					$modal.find("input[name="+i+"]").val(data[i]);
				}
			});
		}
	}
	
}



/*
 * 新增、修改、删除
 */
function modifyEntity($modal){
	var url = web.basePath + "table/entity/";
	var type = $modal.find("input[name=_method]").val();
	if(type == "DELETE"){	//不知道为什么restful不支持DELETE，真是郁闷
		url = web.basePath + "table/entity/delete";
		type = "POST";
	}
	$.ajax({
        url: url,
        type : type,
        data : $modal.find("form").serialize(),
        success : function(data){
            if(data.res){
            	table.reloadAjax();
            	$modal.modal('hide');
            	AT.alert("提交成功！");
            }
            else AT.warn("提交失败！");
        },
        dataType : "json",
        error : function(data){
        	AT.warn("提交出错！");
        }
    })
}






