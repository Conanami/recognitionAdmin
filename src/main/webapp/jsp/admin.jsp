<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.sf.json.JSONArray" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%
	String userName = (String)request.getSession().getAttribute("username");
	JSONArray menuArray =(JSONArray)request.getSession().getAttribute("menuArray");
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>号码识别后台管理系统</title>

<link rel="shortcut icon" type="image/x-icon" href="<c:url value='/images/ooopic_1449567401.png'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/mobile.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />
<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>"></script>

<script type="text/javascript">


	$(function () {
	     var treeData = eval('<%=menuArray%>');
	    //实例化树形菜单
	    $("#tree").tree({
	        data : treeData,
	        lines : true,
	        onClick : function (node) {
	            if (node.attributes) {
	            	var url = "<%=basePath%>"+node.attributes.url;
	                Open(node.text, url);
	            }
	        }
	    });
	    //在右边center区域打开菜单，新增tab
	    function Open(text, url) {
	    	//showWait1();
	    	
	        if ($("#tabs").tabs('exists', text)) {
	            $('#tabs').tabs('select', text);
	        } else {
	        	var content = '<iframe scrolling="yes" id="mainFrame" name="mainFrame" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';  
	        	
	            $('#tabs').tabs('add', {
	                title : text,
	                closable : true,
	                content : content
	                //href:url
	            });
	        }
	    }
	    
	    //绑定tabs的右键菜单
	    $("#tabs").tabs({
	        onContextMenu : function (e, title) {
	            e.preventDefault();
	            $('#tabsMenu').menu('show', {
	                left : e.pageX,
	                top : e.pageY
	            }).data("tabTitle", title);
	        }
	    });
	    
	    //实例化menu的onClick事件
	    $("#tabsMenu").menu({
	        onClick : function (item) {
	            CloseTab(this, item.name);
	        }
	    });
	    
	    //几个关闭事件的实现
	    function CloseTab(menu, type) {
	        var curTabTitle = $(menu).data("tabTitle");
	        var tabs = $("#tabs");
	        
	        if (type === "close") {
	            tabs.tabs("close", curTabTitle);
	            return;
	        }
	        
	        var allTabs = tabs.tabs("tabs");
	        var closeTabsTitle = [];
	        
	        $.each(allTabs, function () {
	            var opt = $(this).panel("options");
	            if (opt.closable && opt.title != curTabTitle && type === "Other") {
	                closeTabsTitle.push(opt.title);
	            } else if (opt.closable && type === "All") {
	                closeTabsTitle.push(opt.title);
	            }
	        });
	        
	        for (var i = 0; i < closeTabsTitle.length; i++) {
	            tabs.tabs("close", closeTabsTitle[i]);
	        }
	    }
	});

	
	function loginOut(){
		var url = "<c:url value='/login/out'/>";
	    window.location.href=url;
	}
</script>


<style type="text/css">
	.body-gray2014 #framecenter {
		margin-top: 3px;
	}
	
	#skinSelect {
		margin-right: 6px;
	}
	article, aside, figure, footer, header, hgroup, 
	  menu, nav, section { display: block; }
	.west{
	  width:200px;
	  padding:10px;
	}
	.north{
	  height:100px;
	}

</style>
</head>

<body class="easyui-layout" data-options="fit : true,border : false">
 
 <div region="north" style="height:62px; width:100%; ">
    <div id="topmenu" > 		
    	 <div >
	    	 <div  style="width: 9% ; height: 60px; float:left;  text-align:left; background: url(<c:url value='/images/topicon.png'/>) center top no-repeat ;background-size: 100% 100% ;  position: relative " >
	             <div style= "color: white; position: relative ; left: 60px ; font-size: 16px ; width:200px; margin-top: 20px; " >号码识别后台管理系统</div>
	         </div>
	         <div  style=" width: 91%;  height: 60px;  float:right; text-align:center; background: url(<c:url value='/images/top.png'/>) center top no-repeat ; background-size: 100% 100% ; " >
	             <div style= "color: #f5f5f6;  float:right;  margin-right: 20px; " >
	             	 <div >
	             	 	<div style="float:left; margin-right: 20px; font-size: 14px ; margin-top: 22px; ">
	             	 	 	<label>你好，<%=userName %></label>
	             	 	</div>
	             	 	
	             	 	<div style="float:left;  ">
		             	 	<div onclick="loginOut()" style=" width:70px;  padding-top: 4px;  margin-top: 16px; background: #33485e;  margin-right: 5%;margin-left: 5%; cursor: pointer; vertical-align: middle ; text-align: center;  height: 22px; font-size: 0.9rem; color: #f5f5f6; border: 1px solid #2b3e51; border-radius: 4px; ">
				             	退出系统
				            </div>
	             	 	</div>
	             	 </div>
	             </div>
	         </div>
    	 </div>
	</div>
  </div>
 
 
 
  <div region="center" title="">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
      <div title="我的主页"></div>
    </div>
  </div>
  <div region="west" class="west" title="主要菜单">
    <ul id="tree"></ul>
  </div>
  
  <div id="tabsMenu" class="easyui-menu" style="width:120px;">  
    <div name="close">关闭</div>  
    <div name="Other">关闭其他</div>  
    <div name="All">关闭所有</div>
  </div>  
</body>
</html>