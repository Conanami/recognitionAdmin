<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>人员管理</title>
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
		var _pageSize = 15;
		var _page = 1;
		
        $(function () {
        	requestGridData();
        	requestRoleInfo();
        });
        
        function requestRoleInfo(){
        	var url = "<c:url value='/admin.role.query'/>";
			$.post(url, {
			}, function(result) {		
				if(result.respCode!=0){
					$.messager.alert('提示', result.respDescription,'error');
				}else{
					$('#roleid').combobox({
		                 data: result.rows,
		                 textField: 'rolename',
		                 valueField: 'roleid'
		             });
				}
			}); 
        }
        
        function requestGridData(){
        	var url = "<c:url value='/admin.user.query'/>";
        	$('#maingrid4').datagrid({
        	    url: url,
        	    
        	    pagination: true,
        	    rownumbers: false,
        	    singleSelect: true,
        	    
        	    toolbar: [{
                    text:'新增用户',
                    iconCls:'icon-add',
                    handler:function(){showAddUser();}
                },'-',{
                    text:'删除',
                    iconCls:'icon-remove',
                    handler:function(){removeUser();}
                }],
        	
        	    queryParams: {
        	    	mobile_id : $("#txtmobile").val(),
        	    	page: _page,  		//服务器入参
        	    	pagesize: _pageSize //服务器入参
        		}, 
        	    
        	    columns:[[
							{ title: '登录名', field: 'username' },
							{ title: '真实姓名', field: 'realname'}, 
							{ title: '部门', field: 'department' },
							{ title: '手机号码', field: 'mobile'},
							{ title: '状态', field: 'status'},
							{ title: '权限', field: 'rolename'},
							{field:'opt',title:'配置',width:200,align:'center',  
								formatter:function(value,rec){  
								var btn = '<a onclick="showPwdEdit(\''+rec.username+'\')" href="javascript:void(0)">重置密码</a>'
								+ '&nbsp;&nbsp;&nbsp;  <a onclick="showRoleEdit(\''+rec.userid+'\',\''+rec.username+'\',\''+rec.roleid+'\')" href="javascript:void(0)">配置权限</a>';  
									return btn;  
								}  
							}
		        	    ]],
        	    onLoadSuccess:function(data){  
					$('.editcls').linkbutton({text:'',plain: true,iconCls:'icon-edit'});  
				}  
        	});  
        	
       	    //设置分页控件       
            var p = $('#maingrid4').datagrid('getPager');       
            $(p).pagination({  
            	pageNumber: _page,
            	pageSize: _pageSize,//每页显示的记录条数，默认为10 
            	pageList: [15],//可以设置每页记录条数的列表      
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
        
        function f_doSearch(){
        	requestGridData();
        }
        
        function showPwdEdit(username){
        	$("#password").textbox('setValue', '');
			$("#password2").textbox('setValue', '');
			
        	$('#dlg-pwd').dialog('open');
        	$('#username').textbox('setValue', username);
        }
        
        function savePassword(){
        	if($("#password").textbox('getValue') != $("#password2").textbox('getValue')){
        		$.messager.alert('提示', '两次密码输入请一致','error',function(r){
        			$("#password").textbox('setValue', '');
        			$("#password2").textbox('setValue', '');
                 });
        	}
        	var url = "<c:url value='/admin.user.updatepwd'/>";
			$.post(url, {
				username : $("#username").textbox('getValue'),
				password : $("#password").textbox('getValue')
			}, function(result) {		
				if(result.respCode!=0){
					$.messager.alert('提示', result.respDescription,'error');
				}else{
					 $('#dlg-pwd').dialog('close');        // close the dialog
                     $.messager.alert('提示', result.respDescription,'info',function(r){
                     	$('#maingrid4').datagrid('reload');
                     });
				}
			}); 
        }
        
        function showRoleEdit(userid, username, roleid){
        	$('#dlg-role').dialog('open');
        	$('#role-userid').textbox('setValue', userid);
        	$('#role-username').textbox('setValue', username);
        	$('#roleid').combobox('setValue', roleid);
        }
        
        function saveUserRole(){
        	var url = "<c:url value='/admin.user.role.update'/>";
			$.post(url, {
				userid : $("#role-userid").textbox('getValue'),
				roleid : $("#roleid").combobox('getValue')
			}, function(result) {		
				if(result.respCode!=0){
					$.messager.alert('提示', result.respDescription,'error');
				}else{
					 $('#dlg-role').dialog('close');        // close the dialog
                     $.messager.alert('提示', result.respDescription,'info',function(r){
                     	$('#maingrid4').datagrid('reload');
                     });
				}
			}); 
        }
        
        function showAddUser(){
        	$('#dlg-new').dialog('open');
        }
        
        function saveAddUser(){
        	$.messager.confirm('Confirm','确认要新增用户 吗?',function(r){
                if (r){
                	ajaxLoading();
                	$("#fm-new").ajaxSubmit({
        				success:function(result){
        					ajaxLoadEnd();
        					if(result.respCode!=0){
        						alert(result.respDescription);
        					}else{
        						$('#dlg-new').dialog('close');        // close the dialog
        						alert(result.respDescription);
        						requestGridData();
        					}
	        			}
                	}); 
                }
            });
        }
        
        function removeUser(){
        	var row = $('#maingrid4').datagrid('getSelected');
            if (row){
	        	$.messager.confirm('Confirm','确认要删除用户'+row.username+' 吗?',function(r){
	                if (r){
	                	ajaxLoading();
	                	var url = "<c:url value='/admin.user.remove'/>";
	        			$.post(url, {
	        				userid : row.userid
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
            width:160px;
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

	<div id="dlg-pwd" title="重置密码" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons-pwd">
        <form id="fmrow" method="post">
        	<input type="hidden" id="paperid" value='0'/>
        	<div class="fitem">
                <label>登录名:</label>
                <input name="username" id="username" class="easyui-textbox" editable="false" >
            </div>
            <div class="fitem">
                <label>新密码:</label>
                <input name="password" id="password" class="easyui-textbox" type="password">
            </div>
            <div class="fitem">
                <label>请再输入一遍密码:</label>
                <input name="password2" id="password2" class="easyui-textbox" type="password">
            </div>
        </form>
    </div>
    <div id="dlg-buttons-pwd">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="savePassword()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-pwd').dialog('close')" style="width:90px">Cancel</a>
    </div>
    
    <div id="dlg-role" title="重新分配角色" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons-role">
        <form id="fm-role" method="post">
        	<input name="role-userid" id="role-userid" class="easyui-textbox" type="hidden">
        	<div class="fitem">
                <label>登录名:</label>
                <input name="role-username" id="role-username" class="easyui-textbox" editable="false" >
            </div>
            <div class="fitem">
                <label>角色:</label>
                <input name="roleid" id="roleid" class="easyui-combobox" editable="false">
            </div>
        </form>
    </div>
    <div id="dlg-buttons-role">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveUserRole()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-role').dialog('close')" style="width:90px">Cancel</a>
    </div>
    
    
    <div id="dlg-new" title="新增用户" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
            closed="true" buttons="#dlg-buttons-new">
        <form id="fm-new" method="post" action="<c:url value='/admin.user.add'/>">
        	<input type="hidden" id="paperid" value='0'/>
        	<div class="fitem">
                <label>登录名:</label>
                <input name="username" id="username" class="easyui-textbox" >
            </div>
            <div class="fitem">
                <label>真实姓名:</label>
                <input name="realname" id="realname" class="easyui-textbox" >
            </div>
            <div class="fitem">
                <label>部门:</label>
                <input name="department" id="department" class="easyui-textbox" >
            </div>
            <div class="fitem">
                <label>手机号码:</label>
                <input name="mobile" id="mobile" class="easyui-textbox" >
            </div>
        </form>
    </div>
    <div id="dlg-buttons-new">
        <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveAddUser()" style="width:90px">Save</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-new').dialog('close')" style="width:90px">Cancel</a>
    </div>
 
</body>

</html>