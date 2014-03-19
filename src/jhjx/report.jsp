<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String code = request.getParameter("code");
	com.powernt.info.UserInfo userInfo = (com.powernt.info.UserInfo) session
			.getAttribute("UserInfo");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!--框架必需start-->
<script type="text/javascript" src="<%=path%>/libs/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/libs/js/framework.js"></script>
<link href="<%=path%>/libs/css/import_basic.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" type="text/css" id="skin" prePath="<%=path%>/" />
<link rel="stylesheet" type="text/css" id="customSkin" />
<!--框架必需end-->
<!-- 日期start -->
<script type="text/javascript" src="<%=path%>/libs/js/form/datePicker/WdatePicker.js"></script>
<!-- 日期end -->
<!--数据表格start-->
<script src="<%=path%>/libs/js/form/form.js" type="text/javascript"></script>
<script src="<%=path%>/libs/js/table/quiGrid.js" type="text/javascript"></script>
<script src="<%=path%>/libs/js/form/stepper.js" type="text/javascript"></script>
<!-- 遮罩提示start -->
<script src="<%=path%>/scripts/jGrowl-master/jquery.jgrowl.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/scripts/jGrowl-master/jquery.jgrowl.css" />
<!-- 遮罩提示end -->

<!--进度条start-->
<script type="text/javascript" src="<%=path%>/libs/js/other/progressbar.js"></script>
<!--进度条end-->

<!--引入弹窗组件start-->
<script type="text/javascript" src="<%=path%>/libs/js/popup/drag.js"></script>
<script type="text/javascript" src="<%=path%>/libs/js/popup/dialog.js"></script>
<!--引入弹窗组件end-->

<script type="text/javascript"
	src="<%=path%>/iproject/basedata/report/js/report.js"></script>
<script type="text/javascript"
	src="<%=path%>/iproject/basedata/adversePersonal/js/List.js"></script>
<script>
var path = "<%=path%>";
</script>
</head>
<body>

<input type="hidden" name="tableid" id="tableid" value="${tableid }" />
<input type="hidden" name="code" id="code" value="${code }" />
	<div>
		<div id="TitMenu">
			<div id="thisAddressDiv" class="position"
				style="margin: 0px 0px 0px 0;">
				<div class="center">
					<div class="left">
						<div class="right">
							<span>当前位置：报表管理 >> ${tablename }</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
	<div id="paddingSelectDiv" class="box_tool_mid">
		<div class="center">
			<div class="left">
				<div class="right">
					<div class="padding_top8 padding_left10">
						<form id="form1">
							<table>
								<tr>
									<td>部门名称：</td>
									<td><input id="orgnamesearch" class="listener" type="text" name="orgnamesearch" style="width:160px;" /></td>
									<td>日期：</td>
									<td><input id="datesearch" type="text" name="datesearch"  class="date listener" datefmt="yyyy-MM" style="width:160px;" /></td>
									<td><input type="button" onclick="searchHandler()" id="btn_SelectFiledSQL" value="查询" /></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br />
	<div id="scrollContent">
		<div class="padding_right5">
			<div id="dataBasic"></div>
		</div>
	</div>

</body>
<script>
var code = "${code}";
var grid;
var column = new Array();
column.push({
	display: "网点",
	name: 'orgname',
	align: "center",
	width: 150,
	frozen: true
});
if("${isshowtype}" == "1"){
	column.push({
		display: "部门类型",
		name: 'type_name',
		align: "center",
		width: 150,
		frozen: true
	});
}
if("${isshowleader}" == "1"){
	column.push({
		display: "部门主管",
		name: 'empname',
		align: "center",
		width: 150,
		frozen: true
	});
}
var parentid ="";
var head={};
var parentarr={};
var validateColumn={};

var editip="请输入原始数据";

var level1His = {};
var level2His = {};
var level3His = {};
//是否编辑公式计算的结果
var editcode =1;
var datafromserver;
//通过得到的数据初始化列表
function intitablek(data){
	// 如果返回结果不为空，则循环
		columnupdata();
	grid= $("#dataBasic").quiGrid({
		columns: column,
		url:'<%=path%>/report.do?action=getReport&table_id='+$("#tableid").val() + "&code="+code,
		sortName: 'id',
		rownumbers: true,
		checkbox: false,
		pageSize: 10,
		dataAction: "server",
		usePager: false,
		height: "100%",
		width: "100%",
		enabledEdit: true,
		clickToEdit: false,
		multihead:true,
		onDblClickRow:function(rowdata, rowindex){
			if(rowdata.status ==1 && rowdata.ismonthly != 2){
				grid.beginEdit(rowindex);
			};
        },
        //如果在rowAttrRender中更改行的背景色的话，要将alternatingRow设置为false，以免自己设的背景色与奇偶行的颜色设置冲突。
        alternatingRow: false,
      	//行渲染 实质是对该行的tr标签进行属性的设置
        rowAttrRender: function(rowdata, rowid){
            return 2 == rowdata.ismonthly ? "style=\"background-color:#B7FCB7;\"" : '';
        },
        onBeforeEdit: onBeforeEdit, 
        onBeforeSubmitEdit: onBeforeSubmitEdit,
        onAfterSubmitEdit: onAfterSubmitEdit,
		toolbar:{ 
			items: [
				<% if (userInfo.validateOperation(code, "Modify")) { %>
                { text: '全部确认修改', click: endAllEdit, iconClass: 'icon_ok'},
                { line: true },
                { text: '全部取消修改', click: cancelAllEdit, iconClass: 'icon_no'},
                { line: true },
                { text: '同步数据', click: updateAll, iconClass: 'icon_reload'},
                { line: true },
                { text: '调整结果', click: modifyvalue, iconClass: 'icon_edit'},
                { line: true }
                <% } %>
                <%if (userInfo.validateOperation(code, "Monthly")) {%>
                ,{ text: '全部月结', click: monthly, iconClass: 'icon_save'},
                { line: true }
                <%}%>
                <%if (userInfo.validateOperation(code, "Execute")) {%>
                ,{ text: '导出', click: exportExcel, iconClass: 'icon_export'}
                <%}%>
            ]
        }            

	});

}
// 初始化列表
function initComplete() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if(month <10){
		month = "0" + month;
	}
	$("#datesearch").val(year + "-" + month);
	$.post("parameter.do?action=getParameterHead", {table_id:$("#tableid").val(),code:code},
	function(data) {
		datafromserver=data;
		editcode=1;
		 intitablek(data);
	});

}
$.quiDefaults.Grid.formatters['percent'] = function (value, column) {
	return value*100+"%";
}

//全部月结
function monthly(){
	beginMonthly("-1");
}
//对运算结果微调
function modifyvalue(){
	alert("can edit!");
	editip="请填入需要增加的值,如需减少,填入负值"
	editcode=2;
	columnupdata();
	grid.options.data=datafromserver;   
	grid.set('columns', column); 
	grid.loadData();
// 	document.getElementById('dataBasic').innerHTML = '';
// 	var gridData=$.parseJSON('{\"form.paginate.pageNo\":1,\"form.paginate.totalRows\":0,\"rows\":[]}');
// 	grid.loadData(gridData);
// 	intitablek(datafromserver);
}
// 单行月结
function beginMonthly(deptid){
	top.Dialog.confirm("月结后数据将不能进行修改，确认继续该操作吗？",function(){
		var date = $("#datesearch").val();
		var tableid = $("#tableid").val();
		if(date == null || date == ""){
			tip("请选择日期");
		}else if(tableid == null || tableid == "" || deptid == null || deptid == ""){
			tip("系统异常，请刷新页面重试！");
		}else{
			$.post("report.do?action=beginDeptReportMonthly",{tableid:tableid,code:code,deptid:deptid,date:date},function(data){
				if(data){
					if(data == 1){
						tip("操作成功");
						DataLoad();
					}else{
						tip("操作失败");
					};
				};
			});
		};
	});
}


//编辑后事件 
function onAfterSubmitEdit(e)
{
	//在这里一律作修改处理
	//var rowData = jQuery.makeArray(e.newdata);
	var object = e.newdata;
	var keys = "";
	for (var property in object){
		keys += property + ",";
	}
	var values = [];
	for (var property in object){
		var v = object[property];
		if(v == ""){
			v = "0";
		}
		values += v + ",";  
	}
	var tableid = $("#tableid").val();
	var date = $("#datesearch").val();
	if(tableid == null || tableid == ""){
		top.Dialog.alert("系统异常，请刷新页面重试！");
	} 
	if(date == null || date == ""){
		tip("请选择日期");
	} else if(keys == "" || keys == null || values == "" || values == null){
		DataLoad();
	}else{
		$("body").mask();
			//ajax方式提交数据到数据库
			$.post("<%=path%>/report.do?action=editReportData",{columns:keys,values:values,departmentid:e.record.department_id,tableid:tableid,code:code,date:date},
			    	function(result){
			    		if(result == 1){
			    			tip("操作成功");
			    			DataLoad();
			    		}else if(result == 2){
			    			tip("操作失败");
			    		}else{
			    			top.Dialog.alert("系统异常，请刷新页面重试！");
			    		};
			    		$("body").unmask();
			    });
	};
}

//导出
function exportExcel(){
	var date = $("#datesearch").val();
	var tableid = $("#tableid").val();
	var orgnamesearch = $("#orgnamesearch").val();
	if(date == null || date == ""){
		tip("请选择日期");
	}else if(tableid == null || tableid == "" || code == null || code == ""){
		tip("系统异常，请刷新页面重试！");
	}else{
		var url = "<%=path%>/report.do?action=exportDepartmentReportData";
		url += "&tableid=" + tableid;
		url += "&code=" + code;
		url += "&date=" + date;
		url += "&orgnamesearch=" + orgnamesearch;
		url += "&type=excel";
		window.location = url;
	}
}
//初始化表头
function columnupdata(){
	data= datafromserver;
	column = new Array();
	level1His = {};
	level2His = {};
	level3His = {};
	if(data){
	for(var i = 0 ; i < data.length ; i++ ){
		
		var level1 = {};
		level1.columns=new Array();
		var level2 = {};
		level2.columns=new Array();
		var level3 = {};
		level3.columns=new Array();
		
		// 三级表头
		if(data[i].id3 !=""){
			// 显示三级表头下面的二级表头
			level2.display = data[i].name2;
			level2.columnid = data[i].id2;
			level2.name = data[i].columnname2;
			level2.align = "center";
			level2.width = 150;
			level2.columns=[];
			
			// 显示三级表头下面的二级表头对应的三级表头
			level3.display =  data[i].name3;
			level3.columnid =  data[i].id3;
			level3.name =  data[i].columnname3;
			level3.align =  "center";
			level3.width =  150;
			// 判断数值类型
			if(data[i].valuetype == 2){
				level3.type="percent";
			}
			// 判断是否可以编辑
			if(data[i].isedit == editcode){
				level3.editor = {
					type: 'text',
					inputMode:"numberOnly",
					tip: editip	
				};
			}
			// 判断该一级菜单是否已经出现过，有则跳过，无则显示
			if(!level1His[data[i].id1]){
				level1.display =  data[i].name1;
				level1.columnid =  data[i].id1;
				level1.name =  data[i].columnname1;
				level1.align =  "center";
				level1.width =  150;
				
				column.push(level1);
				level1His[data[i].id1] = data[i].id1;
				
			}
			if(data[i].id2 =="" && data[i].id3 !=""){
				if(!level3His[data[i].id3]){
					column[column.length-1].columns.push(level3);
					level3His[data[i].id3] = data[i].id3;
				}
			}else{
				if(!level2His[data[i].id2]){
					column[column.length-1].columns.push(level2);
					column[column.length-1].columns[column[column.length-1].columns.length-1].columns.push(level3);
					level2His[data[i].id2] = data[i].id2;
					level3His[data[i].id3] = data[i].id3;
				}
				if(!level3His[data[i].id3]){
					column[column.length-1].columns[column[column.length-1].columns.length-1].columns.push(level3);
					level3His[data[i].id3] = data[i].id3;
				}
			}
			
			
		}
		// 二级表头
		else if(data[i].id2 !=""){
			level2 = {
				display: data[i].name2,
				columnid: data[i].id2,
				name: data[i].columnname2,
				align: "center",
				width: 150
			};
			// 判断数值类型
			if(data[i].valuetype == 2){
				level2.type="percent";
			}
			// 判断是否可以编辑
			if(data[i].isedit == editcode){
				level2.editor = {
					type: "text",
					inputMode:"numberOnly",
					tip: editip
				};
			}
			if(!level1His[data[i].id1]){
				level1.display =  data[i].name1;
				level1.columnid =  data[i].id1;
				level1.name =  data[i].columnname1;
				level1.align =  "center";
				level1.width =  150;
				level1.columns.push(level2);
				
				column.push(level1);
				level1His[data[i].id1] = data[i].name1;
			}else{
				column[column.length-1].columns.push(level2);
			}
		}
		// 一级表头
		else if(data[i].id1 !=""){
			level3.display = data[i].name1;
			level3.columnid = data[i].id1;
			level3.name = data[i].columnname1;
			level3.align = "center";
			level3.width = 150;
			// 判断数值类型
			if(data[i].valuetype == 2){
				level3.type="percent";
			}
			// 判断是否可以编辑
			if(data[i].isedit == editcode){
				level3.editor = {
					type: "text",
					inputMode:"numberOnly",
					tip: editip
				};
			}
			column.push(level3);
		}
	}
	
	}else{
		tip("装载表格出现错误");
	}
	column.push({
		display: "操作",
		name: "op",
		align: "center",
		width: 200,
		render: function (rowdata, rowindex, value){
             var h = "";
             if(rowdata.status ==1 ){
            	 if(rowdata.ismonthly != 2){
                	 <%if (userInfo.validateOperation(code, "Modify")) {%>
	                 if (!rowdata._editing){
	                	 h += "<div class=\"box_tool_line\" style='background-image:none;'></div>";
	                     h += "<a onclick='beginEdit(" + rowindex + ")'><span class='icon_edit'>修改</span></a> ";
	                   
	                 }
	                 else{
	                	 h += "<div class=\"box_tool_line\" style='background-image:none;'></div>";
	                     h += "<a onclick='endEdit(" + rowindex + ")'><span class='icon_ok'>提交</span></a> ";
	                     h += "<div class=\"box_tool_line\"></div>";
	                     h += "<a onclick='cancelEdit(" + rowindex + ")'><span class='icon_no'>取消</span></a> "; 
	                 }
                 	<%}%>
                 	<%if (userInfo.validateOperation(code, "Monthly")) {%>
		                 h += "<div class=\"box_tool_line\" style='background-image:none;'></div>";
		                 h += "<a onclick='beginMonthly(\"" + rowdata.department_id + "\")'><span class='icon_save'>月结</span></a> ";
             		 <%}%>
                 }else{
                	 h += "<div class=\"box_tool_line\" style='background-image:none;'></div>";
                     h += "<font color='green'>已月结</font> ";
                 }
             }else if(rowdata.status ==2){
            	 h += "<div class=\"box_tool_line\" style='background-image:none;'></div>";
                 h += "<font color='red'>该部门已被禁用</font> ";
             }
             return h;
         }

	});

}
</script>
</html>