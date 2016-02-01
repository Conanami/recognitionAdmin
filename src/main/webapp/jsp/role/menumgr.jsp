<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>菜单管理</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />  
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />  
	<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>" ></script>  
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>" ></script> 
    <script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>" ></script> 
    
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
	
        var grid = null;
		var _pageSize = 50;
		var _page = 1;
		
        $(function () {
        	requestGridData();
        	
        	$('#type').combobox({
                data: [{'type':'1', 'title':'一级菜单'}, {'type':'2', 'title':'二级菜单'}],
                textField: 'title',
                valueField: 'type'
            });
        });
        
        function requestGridData(){
        	var url = "<c:url value='/admin.menu.query'/>";
        	$('#maingrid4').datagrid({
        	    url: url,
        	    
        	    pagination: false,
        	    rownumbers: false,
        	    singleSelect: true,
        	    
        	    toolbar: [{
                    text:'新增菜单项',
                    iconCls:'icon-add',
                    handler:function(){showAddMenu();}
                },'-',{
                    text:'编辑',
                    iconCls:'icon-edit',
                    handler:function(){showEditMenu();}
                },'-',{
                    text:'删除',
                    iconCls:'icon-remove',
                    handler:function(){removeMenu();}
                }],
        	
        	    queryParams: {
        	    	page: _page,  		//服务器入参
        	    	pagesize: _pageSize //服务器入参
        		}, 
        	    
        	    columns:[[
							{ title: '菜单名称', field: 'menuname', width:150},
							{ title: '菜单地址', field: 'url', width:200}, 
							{ title: '菜单等级', field: 'type', width:80 ,formatter: fmt_type},
							{ title: '父菜单名称', field: 'parentmenuid', width:150, formatter: fmt_menuid}
		        	    ]]
        	});  
        	
       	    //设置分页控件       
            var p = $('#maingrid4').datagrid('getPager');       
            $(p).pagination({  
            	pageNumber: _page,
            	pageSize: _pageSize,//每页显示的记录条数，默认为10 
            	pageList: [_pageSize],//可以设置每页记录条数的列表      
                beforePageText: '第',//页数文本框前显示的汉字           
                afterPageText: '页    共 {pages} 页',           
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'    ,
                onBeforeRefresh:function(){
                    $(this).pagination('loading');
                    $(this).pagination('loaded');
                }
            });
        }
         
        function fmt_date(value, row, index){
        	if(value!=undefined && value.length>10){
	        	return value.substring(0,10);        		
        	}
        	return value;
        }
        
        function fmt_menuid(value, row, index){
        	var rows = $('#maingrid4').datagrid('getData').rows;
        	for(var i=0;i<rows.length;i++){
        		var item = rows[i];
        		if (item.menuid==value) {
        			return item.menuname;
				}
        	}
        	return '';
        }
        
        function fmt_type(value, row, index){
        	if (value==1) {
				return '一级';
			}else if (value==2) {
				return '二级';
			}
        	return '';
        }
        
        function f_doSearch(){
        	requestGridData();
        }
        
        function refresh_parentmenu(){
        	var result = [];
        	result.push({'menuname':'无', 'menuid':'0'});
        	var rows = $('#maingrid4').datagrid('getData').rows;
        	
        	for(var i=0;i<rows.length;i++){
        		var item = rows[i];
        		if (item.type=='1') {
        			result.push(item);
				}
        	}
        	$('#parentmenuid').combobox({
                data: result,
                textField: 'menuname',
                valueField: 'menuid'
            });
        }
        
        function showAddMenu(){
        	$('#fm-menu').form('clear');
        	$('#dlg-menu').dialog('open');
        	
        	refresh_parentmenu();
        }
        
        function showEditMenu(){

        	refresh_parentmenu();
        	
        	$('#dlg-menu').dialog('open');
        	var row = $('#maingrid4').datagrid('getSelected');
        	$('#fm-menu').form('load',row);
        }
        
        function removeMenu(){
        	var row = $('#maingrid4').datagrid('getSelected');
            if (row){
	        	$.messager.confirm('Confirm','确认要删除菜单'+row.menuname+' 吗?',function(r){
	                if (r){
	                	ajaxLoading();
	                	var url = "<c:url value='/admin.menu.remove'/>";
	        			$.post(url, {
	        				menuid : row.menuid
	        			}, function(result) {	
	        				ajaxLoadEnd();
	        				if(result.respCode!=0){
	        					$.messager.alert('提示', result.respDescription,'error');
	        				}else{
	        					$('#maingrid4').datagrid('reload');
	        				}
	        			}); 
	                }
	            });
            }
        }
        
        function saveMenu(){
        	ajaxLoading();
        	$("#fm-menu").ajaxSubmit({
				success:function(result){
					ajaxLoadEnd();
					if(result.respCode!=0){
						$.messager.alert('提示', result.respDescription,'error');
					}else{
						$('#dlg-menu').dialog('close');        // close the dialog
						$('#maingrid4').datagrid('reload');
					}
    			}
        	}); 
        }
        
        function exportdata(){
        	
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
            width:250px;
        }
    </style>
<body style="padding:6px; overflow:hidden;">
 
	<div style="margin-top:10px ;">
		<table id="maingrid4" style="width:100%;height:auto">
		</table>
	</div>
  	<div style="display:none;" >
	</div>

	<div id="dlg-menu" title="菜单" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons-menu">
        <form id="fm-menu" method="post" action="<c:url value='/admin.menu.update'/>">
        	<input type="hidden" id="menuid" name="menuid" value='0'/>
        	<div class="fitem">
                <label>菜单名称:</label>
                <input name="menuname" id="menuname" class="easyui-textbox" >
            </div>
            <div class="fitem">
                <label>菜单地址:</label>
                <input name="url" id="url" class="easyui-textbox" >
            </div>
            <div class="fitem">
                <label>菜单等级:</label>
                <input name="type" id="type" class="easyui-combobox" editable="false">
            </div>
            <div class="fitem">
                <label>父菜单名称:</label>
                <input name="parentmenuid" id="parentmenuid" class="easyui-combobox" editable="false">
            </div>
        </form>
    </div>
    <div id="dlg-buttons-menu">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveMenu()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-menu').dialog('close')" style="width:90px">Cancel</a>
    </div>
</body>

</html>