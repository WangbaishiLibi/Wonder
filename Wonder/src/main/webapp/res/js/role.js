/*
 * table.js
 */

var table;				//表格对象

$(function(){
	

	table = new DataTable({
		tbname : "U_ROLE",
		tabledom : $("#table"),
		ajax : {url:web.basePath+"table/entityList/U_ROLE"},
		columns : [
		    {name : "序号", field : "RN"},       
			{name : "ID", field : "ROLE_ID"},
			{name : "角色名", field : "ROLE_NAME"},
			{name : "显示名", field : "DISPLAY_NAME"},
			{name : "备注", field : "MEMO"},
			{name : "操作", field : function(row){
				return '<span rowid="'+row.ROLE_ID+'" class="op_modify glyphicon glyphicon-edit">修改</span>' + 
				'<span rowid="'+row.ROLE_ID+'" class="op_delete glyphicon glyphicon-remove">删除</span>';
			}}
		],
		page : {
			page_index : 1,		
			page_total : 10	//每页记录数
		}
	});

	
	$("#btn_add").click(function(){
		updatePrivileges($("#addModal"));
		document.add['role_id'].value = null;
		document.add.reset();
	})
	
	$(document).on("click", ".op_modify", function(){
		$("#addModal").modal("show");
		var role = table.rows[$(this).parents("tr").eq(0).attr("no")];
		for(var i in role){
			var name = i.toLowerCase();
			if(document.add[name])	document.add[name].value = role[i];
		}
		document.add['role_id'].value = role.ROLE_ID;
		updatePrivileges($("#addModal"), role.ROLE_ID);
	})
	
	$(document).on("click", ".op_delete", function(){
		$("#deleteModal").find("input[name=tbname]").val(table.tbname);
		$("#deleteModal").modal("show");
		
	})
	
	$(".save").click(function(){
		var modalId = $(this).parents(".modal").attr("id");
		modifyEntity($("#"+modalId))
	});
})


function updatePrivileges($dom, roleId){
	$dom.find(".left-part .list-group-item").remove();
	$dom.find(".right-part .list-group-item").remove();
	var url = "";
	if(roleId){
		url = web.basePath + "getRolePrivilegeData?role_id="+roleId;	
	}else{
		url = web.basePath + "getPrivilegeData";
	}
	$.getJSON(url, function(data){
		if(roleId){
			for(var i in data[0]){
				$dom.find(".right-part").append('<li class="list-group-item" value="'+data[0][i].privilege_id+'" >'+data[0][i].display_name+'</li>');
			}
			for(var i in data[1]){
				$dom.find(".left-part").append('<li class="list-group-item" value="'+data[1][i].privilege_id+'" >'+data[1][i].display_name+'</li>');
			}
		}else{
			for(var i in data){
				$dom.find(".left-part").append('<li class="list-group-item" value="'+data[i].privilege_id+'" >'+data[i].display_name+'</li>');
			}
		}
	})
}



/*
 * 新增、修改、删除
 */
function modifyEntity($modal){
	var url = web.basePath;
	url += document.add['role_id'].value?"updateRole":"insertRole";
	
	var params = $modal.find("form").serialize();
	$modal.find(".right-part li").each(function(){
		params += "&privilegeIds="+ $(this).attr("value");
	})
	
	$.ajax({
        url: url,
        type : "POST",
        data : params,
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






