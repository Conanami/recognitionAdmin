<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>角色管理</title>
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
        });
        
        function requestGridData(){
        	var url = "<c:url value='/admin.role.query'/>";
        	$('#maingrid4').datagrid({
        	    url: url,
        	    
        	    pagination: false,
        	    rownumbers: false,
        	    singleSelect: true,
        	    
        	    toolbar: [{
                    text:'新增',
                    iconCls:'icon-add',
                    handler:function(){showAddRole();}
                },'-',{
                    text:'编辑',
                    iconCls:'icon-edit',
                    handler:function(){showEditRole();}
                },'-',{
                    text:'编辑角色的菜单权限',
                    iconCls:'icon-edit',
                    handler:function(){showEditRoleMenu();}
                },'-',{
                    text:'删除',
                    iconCls:'icon-remove',
                    handler:function(){removeRole();}
                }],
        	
        	    queryParams: {
        	    	page: _page,  		//服务器入参
        	    	pagesize: _pageSize //服务器入参
        		}, 
        	    
        	    columns:[[
							{ title: '角色名称', field: 'rolename', width:150},
							{ title: '角色描述', field: 'roledesc', width:200}
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
        
        function f_doSearch(){
        	requestGridData();
        }
        
        function showAddRole(){
        	$('#fm-role').form('clear');
        	$('#dlg-role').dialog('open');
        }
        
        function showEditRole(){
        	$('#dlg-role').dialog('open');
        	var row = $('#maingrid4').datagrid('getSelected');
        	$('#fm-role').form('load',row);
        }
        
        function removeRole(){
        	var row = $('#maingrid4').datagrid('getSelected');
            if (row){
	        	$.messager.confirm('Confirm','确认要删除角色'+row.rolename+' 吗?',function(r){
	                if (r){
	                	ajaxLoading();
	                	var url = "<c:url value='/admin.role.remove'/>";
	        			$.post(url, {
	        				roleid : row.roleid
	        			}, function(result) {	
	        				ajaxLoadEnd();
	        				if(result.respCode!=0){
	        					$.messager.alert('提示', result.respDescription,'error');
	        				}else{
	                             $.messager.alert('提示', result.respDescription,'info',function(r){
	                             	$('#maingrid4').datagrid('reload');
	                             });
	        				}
	        			}); 
	                }
	            });
            }
        }
        
        function saveRole(){
        	ajaxLoading();
        	$("#fm-role").ajaxSubmit({
				success:function(result){
					ajaxLoadEnd();
					if(result.respCode!=0){
						$.messager.alert('提示', result.respDescription,'error');
					}else{
						$('#dlg-role').dialog('close');        // close the dialog
						 $.messager.alert('提示', result.respDescription,'info',function(r){
	                     	$('#maingrid4').datagrid('reload');
	                     });
					}
    			}
        	}); 
        }
        
        function showEditRoleMenu(){
        	$('#dlg-rolemenu').dialog('open');
        	var row = $('#maingrid4').datagrid('getSelected');
        	if(row){
        		var urlstr = 'rolemenumgr.jsp?roleid='+row.roleid + "&rolename="+encodeURI(row.rolename);
            	window.location = urlstr;
        	}else{
        		$.messager.alert('提示', '请先选择一条记录','error');
        	}
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
            width:160px;
        }
    </style>
<body style="padding:6px; overflow:hidden;">
 
	<div style="margin-top:10px ;">
		<table id="maingrid4" style="width:100%;height:500px">
		</table>
	</div>
  	<div style="display:none;" >
	</div>

	<div id="dlg-role" title="" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons-role">
        <form id="fm-role" method="post" action="<c:url value='/admin.role.update'/>">
        	<input type="hidden" id="roleid" name="roleid" value='0'/>
        	<div class="fitem">
                <label>角色名称:</label>
                <input name="rolename" id="rolename" class="easyui-textbox" >
            </div>
            <div class="fitem">
                <label>角色描述:</label>
                <input name="roledesc" id="roledesc" class="easyui-textbox" >
            </div>
        </form>
    </div>
    <div id="dlg-buttons-role">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveRole()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-role').dialog('close')" style="width:90px">Cancel</a>
    </div>
</body>

</html>