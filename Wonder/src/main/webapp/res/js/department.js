/*
 * table.js
 */

var table;				//表格对象
var tree;
$(function(){
	

	table = new DataTable({
		cache : true,
		tbname : "U_DEPARTMENT",
		tabledom : $("#table"),
		columns : [
		    {name : "部门编号", field : "department_code"},     
			{name : "部门名称", field : "department_name"},
			{name : "上级部门", field : "superior_department_name"},
			{name : "角色", field : function(row){
				var roles = [];
				for(var i in row.roles)	roles.push(row.roles[i].display_name);
				return roles.toString();
			}},
			{name : "类别", field : function(row){
//				var types = new Array("设备使用部门","设备管理部门", "调度室", "段级部门");
				var types = new Array("设备使用部门","设备管理部门");
				return types[parseInt(row.category)];
			}},
			{name : "操作", field : function(row){
				return '<span rowid="'+row.ROLE_ID+'" class="op_modify glyphicon glyphicon-edit">修改</span>' + 
				'<span rowid="'+row.ROLE_ID+'" class="op_delete glyphicon glyphicon-remove">删除</span>'; 
			//	'<span rowid="'+row.ROLE_ID+'" class="op_detail glyphicon glyphicon-th-list">权限详情</span>';
			}}
		],
		page : {
			page_index : 1,		
			page_total : 10	//每页记录数
		}
	});

	
	web.initTree($("#depart_tree")).on("select_node.jstree", function (e, data) {
		table.reloadAjax(web.basePath+"getChildDepartment.do", {department_id:data.node.id})
	});
	
	$("#query").find("button").eq(0).click(function(){
		table.reloadAjax(web.basePath+"getDepartmentBykey.do", {key:$("#query").find("input").eq(0).val()})
	})
	$("#query").find("input").eq(0).keyup(function(evt){
		if(evt.keyCode == 13)	$("#query").find("button").eq(0).click();
	})
	
	$("#btn_add").click(function(){
		updatePrivileges($("#privi_selector"));
		updateRoles($("#role_selector"));
		document.add['user_id'].value = null;
		document.add.reset();
	})
	
	$(document).on("click", ".op_modify", function(){
		$("#addModal").modal("show");
		var department = table.rows[$(this).parents("tr").eq(0).attr("no")];
		for(var i in department){
			var name = i.toLowerCase();
			if(document.add[name])	document.add[name].value = department[i];
		}
		document.add['department_id'].value = department.department_id;
		updatePrivileges($("#privi_selector"), department.department_id);
		updateRoles($("#role_selector"), department.department_id);
	})
	
	$(document).on("click", ".op_delete", function(){
		$("#deleteModal").find("input[name=tbname]").val(table.tbname);
		$("#deleteModal").modal("show");
		
	})
	
	$(document).on("click", ".op_detail", function(){
		var user = table.rows[$(this).parents("tr").eq(0).attr("no")];
		$("#detailModal").modal("show");
		loadPrivilegeDetail(user.user_id);
	})
	
	
	
	$(".save").click(function(){
		var modalId = $(this).parents(".modal").attr("id");
		modifyEntity($("#"+modalId))
	});
	
	
})


function updateRoles($dom, departId){
	$dom.find(".left-part .list-group-item").remove();
	$dom.find(".right-part .list-group-item").remove();
	var url = "";
	if(departId){
		url = web.basePath + "getDepartmentRoleData?department_id="+departId;	
	}else{
		url = web.basePath + "getRoleData";
	}
	$.getJSON(url, function(data){
		if(departId){
			for(var i in data[0]){
				$dom.find(".right-part").append('<li class="list-group-item" value="'+data[0][i].role_id+'" >'+data[0][i].display_name+'</li>');
			}
			for(var i in data[1]){
				$dom.find(".left-part").append('<li class="list-group-item" value="'+data[1][i].role_id+'" >'+data[1][i].display_name+'</li>');
			}
		}else{
			for(var i in data){
				$dom.find(".left-part").append('<li class="list-group-item" value="'+data[i].role_id+'" >'+data[i].display_name+'</li>');
			}
		}
	})
}


function updatePrivileges($dom, departId){
	$dom.find(".left-part .list-group-item").remove();
	$dom.find(".right-part .list-group-item").remove();
	var url = "";
	if(departId){
		url = web.basePath + "getDepartmentPrivilegeData?department_id="+departId;	
	}else{
		url = web.basePath + "getPrivilegeData";
	}
	$.getJSON(url, function(data){
		if(departId){
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
	url += document.add['user_id'].value?"updateUser":"insertUser";
	
	var params = $modal.find("form").serialize();
	$("#role_selector").find(".right-part li").each(function(){
		params += "&roleIds="+ $(this).attr("value");
	})
	$("#privi_selector").find(".right-part li").each(function(){
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



function loadPrivilegeDetail(user_id){
	$.getJSON(web.basePath+"getUserPrivilege.do?user_id="+user_id, function(data){
		$("#detail_table tbody").find("tr").remove();
		for(var i in data){
			var row = "<tr>";
			row += "<td>" + data[i][0] + "</td>";
			row += "<td>" + data[i][1] + "</td>";
			row += "<td>" + data[i][2] + "</td>";
			row += "<td>" + data[i][3] + "</td>";
			row += "</tr>";
			$("#detail_table tbody").append(row);
		}

	});
}


