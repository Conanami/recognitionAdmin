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

        // 格式化手机状态
        function showstatus(value,rec){
            if(rec.difference>30*1000){
                return '离线';
            }else{
                return '在线';
            }
        }

        //格式化状态字段的颜色
        function cellStyler(value,row,index){
            if (row.difference > 30*1000){
                return 'background-color:#ffffff;color:black;';
            }else{
                return 'background-color:#ffee00;color:green;font-weight:600';
            }
        }

        function querySearch(){
            $('#dg2').edatagrid({
                pageSize: 15,//每页显示的记录条数
                pageList: [15,30,60],//可以设置每页记录条数的列表
                url: "<c:url value='/api.devicelog.query'/>",
                queryParams: {
                }
            });
        }

        $(function(){
            queryOfflineStatus();
            querySearch();
        });
    </script>
</head>

<body style="padding:6px;">
    <div class="easyui-layout" style="width:auto;height:30px;">
        <label id="offlinestatus" style="font-size: small">状态:</label>
    </div>
    <table id="dg2" title="手机终端运行状况" style="width:auto;height:auto"
           pagination="false" idField="uniqueid" toolbar="#toolbar"
           rownumbers="true" fitColumns="true" singleSelect="true">
        <thead>
        <tr>
            <th field="mobile" style="width: 100px;">手机当前号码</th>
            <th field="status" style="width: 100px;" formatter="showstatus" styler="cellStyler">状态</th>
            <th field="lasttime" style="width: 150px;">最近一次拨打时间</th>
            <th field="uniqueid" style="width: 150px;">唯一标识</th>
        </tr>
        </thead>
    </table>

    <!-- 工具栏 -->
    <div id="toolbar">
        <a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="javascript:$('#dg2').datagrid('reload')">reload</a>
    </div>
</body>
</html>