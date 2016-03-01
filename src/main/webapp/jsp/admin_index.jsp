<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!-- 批次上传待识别的记录 -->
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>" ></script>
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>" ></script>
    <script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>" ></script>
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.edatagrid.js'/>" ></script>
    <script type="text/javascript" src="<c:url value='/js/audioplayer.js'/>"></script>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/form_common.css'/>" />
    <style>
    </style>
    <script type="text/javascript">
        //采用jquery easyui loading css效果

        //查询离线状态
        function queryOfflineStatus(){
            var url = "<c:url value='/api.offline.status.query'/>";
            $("#offlinestatus").text("正在查询...").css({"color":"black", "font-weight":"10"});
            $.post(url, {
            }, function(result) {
                if (result.respCode==0){
                    var interval = result.rows[0];
                    if (interval<30){
                        $("#offlinestatus").text("识别手机端应用程序在线").css({"color":"green", "font-weight":"600"});
                    }else{
                        $("#offlinestatus").text("识别手机端应用程序离线").css({"color":"red", "font-weight":"600"});
                    }
                } else {
                    $("#offlinestatus").text("查询手机端应用异常"+result.respDescription);
                }
                setTimeout("queryOfflineStatus()",3000);
            });
        }

        $(function(){
            queryOfflineStatus();
        });
    </script>
</head>

<body style="padding:6px;">
    <div class="easyui-layout" style="width:auto;height:30px;">
        <label id="offlinestatus" style="font-size: small">状态:</label>
    </div>
</body>
</html>