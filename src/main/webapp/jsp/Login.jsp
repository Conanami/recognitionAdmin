<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>号码识别后台管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-store, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="0">
<link rel="shortcut icon" type="image/x-icon" href="<c:url value='/images/ooopic_1449567401.png'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />

<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.cookie.js'/>"></script>
<style>
a {
	text-decoration: none !important
}

.button_bg {
	background: #16b7f3;
	border: 1px solid #16b7f3;
}

.button_bg:hover {
	background: #35c3f8;
	border: 1px solid #35c3f8;
}

.box {
	-moz-border-radius: 4px; /* Firefox */
	behavior: url(css/ie-css3.htc); /* 对IE-CSS3的引用 */
}

::-webkit-input-placeholder { /* WebKit browsers */
	color: #cccccc;
}

:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
	color: #cccccc;
}

::-moz-placeholder { /* Mozilla Firefox 19+ */
	color: #cccccc;
}

:-ms-input-placeholder { /* Internet Explorer 10+ */
	color: #cccccc;
}
BODY {background-image: URL('<c:url value='/images/login_bg.jpg'/>');
    background-position: top center;
    background-repeat: no-repeat;
    background-attachment: fixed;}

</style>
</head>
<script type="text/javascript"> 

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

function reset(){
	$("#username").val("");
	$("#loginpassword").val("");
}
function login(){
	var url = "<c:url value='/sxd.admin.login'/>";
	ajaxLoading();
	$.post(url, {
		username : $("#username").val(),
		password : $("#loginpassword").val()
	}, function(obj) {	
		ajaxLoadEnd();
		if (obj.respCode != 0) {
            $.messager.alert('提示', obj.respDescription,'error');
		} else {
			$.cookie('usernameCookie', $("#username").val()); // 存储 cookie 
			window.location.href = "<c:url value='/jsp/admin.jsp'/>";
		}
	});
}

function strToObj(json) {
	return eval("(" + json + ")");
}
function gotoregister(){
	var url = "<c:url value="/mobile/index"/>";
	$.mobile.changePage(url, "slideup");
}

$(function () {
	$("#username").val($.cookie("usernameCookie"));
	
    $("#loginpassword").keydown(function(event) {
        if (event.keyCode == 13) {
        	login();
        }
    });
});

</script>

<body>

	<div
		style="vertical-align: middle; text-align: center; width: 440px; position: fixed; margin-left: -220px; margin-top: -164px; height: 100%; left: 50%; top: 50%;"
		id="contentDiv">
		<div
			style="height: 100px;  background:url('<c:url value='/images/login_area_bg.jpg'/>'); center top no-repeat ;  background-size: 100% 100% ;  ">
			<label
				style="color: white; line-height: 100px; font-size: 18px; font-weight: 600;">号码识别后台管理系统</label>
		</div>
		<div
			style="height: 228px; border-bottom-left-radius: 4px; border-bottom-right-radius: 4px; background: white;">
			<div
				style="text-align: center; width: 240px; padding-left: 100px; padding-top: 10px; padding-right: 100px; height: 52px;">
				<div style="float: left;">
					<img src="<c:url value='/images/login_user_icon.png'/>"
						style="width: 22px; margin-top: 15px;">
					<!-- 动态 icon-->
				</div>
				<div style="float: left;">
					<input id="username" type="text"
						style="width: 100%; padding-top: 15px; padding-bottom: 15px; padding-left: 10px; padding-right: 10px; outline: none; font-size: 16px; color: #333333; border-top: solid 0px white; border-bottom: solid 0px white; border-left: solid 0px white; border-right: solid 0px white; font-family: Tahoma, Geneva, sans-serif"
						placeholder="请输入用户名">
				</div>
			</div>
			<div
				style="height: 1px; width: 240px; margin-right: 100px; margin-left: 100px; background: #c4c4c4">
			</div>

			<div
				style="text-align: center; width: 240px; padding-left: 100px; padding-right: 100px; height: 52px;">
				<div style="float: left;">
					<img src="<c:url value='/images/login_password_icon.png'/>"
						style="width: 22px; margin-top: 15px;">
					<!-- 动态 icon-->
				</div>
				<div style="float: left;">
					<input id="loginpassword" type="password"
						style="width: 100%; padding-top: 15px; padding-bottom: 15px; padding-left: 10px; padding-right: 10px; outline: none; font-size: 16px; color: #333333; border-top: solid 0px white; border-bottom: solid 0px white; border-left: solid 0px white; border-right: solid 0px white; font-family: Tahoma, Geneva, sans-serif"
						placeholder="请输入密码">
				</div>
			</div>
			<div
				style="height: 1px; width: 240px; margin-right: 100px; margin-left: 100px; background: #c4c4c4">
			</div>

			<div
				style="cursor: pointer; vertical-align: middle; text-align: center; margin-top: 30px;">
				<button class="button_bg" onclick="login()"
					style="line-height: 44px; color: white; vertical-align: middle; width: 240px; height: 44px; font-size: 22px; color: white; border-radius: 4px;">登&nbsp;录</button>
			</div>
		</div>
	</div>
</body>

</html>