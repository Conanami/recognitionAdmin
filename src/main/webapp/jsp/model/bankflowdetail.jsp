<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String queryid = request.getParameter("queryid") == null ? "" : (String) request.getParameter("queryid");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>

    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>"/>
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.edatagrid.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>"></script>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/form_common.css'/>"/>
    <script type="text/javascript">

        //采用jquery easyui loading css效果
        function ajaxLoading(parent) {
            var mask = $("<div class=\"datagrid-mask\"></div>").css({
                display: "block",
                width: "100%",
                height: parent.height()
            });
            var msg = $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。");
            msg.css({display: "block", left: (parent.outerWidth(true) - 190) / 2, top: (parent.height() - 45) / 2});
            msg.appendTo(parent);
        }
        function ajaxLoadEnd() {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }

        $(function () {
            var queryid = <%=queryid%>;
            $('#dg').datagrid({
                pageSize: 15,//每页显示的记录条数，默认为10
                pageList: [15,30,60],//可以设置每页记录条数的列表
                url: "<c:url value='/api.bankflow.detail.query'/>",
                queryParams: {
                    userid: '',  		//服务器入参
                    cardno: '',         //服务器入参
                    queryid: queryid
                },
            });
        });

        function f_explort() {
        }

        function formaterIOFlag(value, row) {
            if (value == 1) {
                return '转入';
            } else if(value==-1){
                return '转出';
            }
            return '';
        }

    </script>
</head>

<body style="padding:6px;">
    <div style="margin-top:10px ;">
        <table id="dg" title="银行流水详情" style="width:auto;height:auto"
               pagination="true" idField="flowid"
               rownumbers="false" fitColumns="true" singleSelect="true">
            <thead>
            <tr>
                <th field="merchid">商户号</th>
                <th field="txamt" >交易金额</th>
                <th field="balance" >银行卡余额</th>
                <th field="tradedesc" >备注</th>
                <th field="ioflag" formatter="formaterIOFlag">转账方向</th>
                <th field="tradedate" >交易时间</th>
                <th field="cyy" >币种</th>
            </tr>
            </thead>
        </table>
</div>

</body>

</html>