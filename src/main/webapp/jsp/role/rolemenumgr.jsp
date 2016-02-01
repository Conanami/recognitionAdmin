<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>角色菜单管理</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />  
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />  
	<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>" ></script>  
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>" ></script> 
    <script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>" ></script> 
    <%
		String roleid = request.getParameter("roleid")==null?"":(String)request.getParameter("roleid");
		String rolename = request.getParameter("rolename")==null?"":(String)request.getParameter("rolename");
	%>
    <script type="text/javascript">
    
  		//采用jquery easyui loading css效果 
	    function ajaxLoading(){ 
	        $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
	        $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
	    } 
	    function ajaxLoadingWith(msg){ 
	        $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
	        $("<div class=\"datagrid-mask-msg\"></div>").html(msg).appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
	    } 
		function ajaxLoadEnd(){ 
			$(".datagrid-mask").remove(); 
			$(".datagrid-mask-msg").remove();             
		}
	
		$.extend($.fn.datagrid.methods, {
			editCell: function(jq,param){
				return jq.each(function(){
					var opts = $(this).datagrid('options');
					var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
						}
					}
					$(this).datagrid('beginEdit', param.index);
                    var ed = $(this).datagrid('getEditor', param);
                    if (ed){
                        if ($(ed.target).hasClass('textbox-f')){
                            $(ed.target).textbox('textbox').focus();
                        } else {
                            $(ed.target).focus();
                        }
                    }
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor = col.editor1;
					}
				});
			},
            enableCellEditing: function(jq){
                return jq.each(function(){
                    var dg = $(this);
                    var opts = dg.datagrid('options');
                    opts.oldOnClickCell = opts.onClickCell;
                    opts.onClickCell = function(index, field){
                        if (opts.editIndex != undefined){
                            if (dg.datagrid('validateRow', opts.editIndex)){
                                dg.datagrid('endEdit', opts.editIndex);
                                opts.editIndex = undefined;
                            } else {
                                return;
                            }
                        }
                        dg.datagrid('selectRow', index).datagrid('editCell', {
                            index: index,
                            field: field
                        });
                        opts.editIndex = index;
                        opts.oldOnClickCell.call(this, index, field);
                    }
                });
            }
		});
		
        var grid = null;
		var _pageSize = 50;
		var _page = 1;
		
        $(function () {
        	requestGridData();
        });
        
        function requestGridData(){
        	var url = "<c:url value='/admin.rolemenu.query'/>";
        	$('#maingrid4').datagrid({
        		url: url,
        		queryParams: {
        	    	roleid: '<%=roleid%>'
        		}, 
        	}).datagrid('enableCellEditing');
        }
        
        function dgloadSuccess(data){
        	if(data){
				$.each(data.rows, function(index, item){
					if(item.checked==1){
						$('#maingrid4').datagrid('checkRow', index);
					}
				});
			}
        }
        
        function f_doSearch(){
        	requestGridData();
        }
        
        function saveRoleMenu(){
    		var obj = $('#maingrid4').datagrid('getChecked');
        	var liststr = JSON.stringify(obj);
    		
    		//$.messager.alert('提示', liststr,'error');
    		
        	ajaxLoading();
        	var url = "<c:url value='/admin.rolemenu.update'/>";
        	$.post(url, {
				roleid: '<%=roleid%>',
				menuidlist: liststr
			}, function(result) {	
				ajaxLoadEnd();
				if(result.respCode!=0){
					$.messager.alert('提示', result.respDescription,'error');
				}else{
					 $.messager.alert('提示', result.respDescription,'info',function(r){
						 requestGridData();
                    });
				}
			});
        }
        
        function exportdata(){
        	
        }
        
        function f_back()
        {
        	var url = "rolemgr.jsp";
        	window.location = url;
        }
        
    </script>
</head>
<style type="text/css">
        #fm{
            margin:0;
            padding:10px 30px;
        }
        .ftitle{
            font-size:14px;
            font-weight:bold;
            padding:5px 0;
            margin-bottom:10px;
            border-bottom:1px solid #ccc;
        }
        .fitem{
            margin-bottom:5px;
        }
        .fitem label{
            display:inline-block;
            width:80px;
        }
        .fitem input{
            width:80px;
        }
    </style>
<body style="padding:6px; overflow:hidden;">
	<input style="position:absolute;left:10px;margin-top:0% ; " id="btnOK" type="button" value="返回" onclick="f_back()" />
 
		<div style="margin-top:30px ;">
		
		<table id="maingrid4" title="角色 《<%=rolename%> 》的权限菜单详情" style="width:700px;height:auto"
				data-options="
					singleSelect: false,
					method:'post',
					checkOnSelect: false,
					toolbar: '#toolbar',
					onLoadSuccess: dgloadSuccess
				">
			<thead>
				<tr>
					<th data-options="field: 'checked', checkbox:true"></th>
					<th data-options="field:'parentmenuname',width:100">一级菜单</th>
					<th data-options="field:'menuname',width:100">二级菜单</th>
					<th data-options="field:'position',width:80,align:'right',editor:{type:'numberbox',options:{precision:0}}">排序位置</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveRoleMenu()">保存</a>
    </div>
	</div>
  	<div style="display:none;" >
	</div>
</body>

</html>