<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%
	String courseid = request.getParameter("courseid")==null?"0":(String)request.getParameter("courseid");
	String coursename = request.getParameter("coursename")==null?"":(String)request.getParameter("coursename");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />  
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />  
	<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>" ></script>  
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>" ></script> 
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.edatagrid.js'/>" ></script> 
    <script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>" ></script> 
	 
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/form_common.css'/>" />  
    <script type="text/javascript">

        //采用jquery easyui loading css效果
        function ajaxLoading(parent){
            var mask = $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:parent.height()});
            var msg = $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。");
            msg.css({display:"block",left:(parent.outerWidth(true) - 190) / 2,top:(parent.height() - 45) / 2});
            msg.appendTo(parent);
        }
        function ajaxLoadEnd(){
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }

		function f_back()
        {
			history.back() ;
        }

        function fmt_bank(value, row, index){
            if(value!=undefined && value.length>10){
                return value.substring(0,10);
            }
            return value;
        }

        function bankflowquery(){
            $('#dgpapers').datagrid({
                pageSize: 15,//每页显示的记录条数，默认为10
                pageList: [15,30,60],//可以设置每页记录条数的列表
                url: "<c:url value='/api.bankflowlog.query'/>",
                queryParams: {
                    userid: $("#merchid").val(),  		//服务器入参
                    cardno: $("#cardno").val()         //服务器入参
                },
            });
        }

        $(function(){
            bankflowquery();
		});
    </script>
</head>

<body style="padding:6px;">
	<div class="easyui-layout" style="width:auto;height:30px;">
        <label>商户号:</label>
		<input id="merchid" type="text" placeholder="请输入商户号"/>
        <label>银行卡号:</label>
        <input id="cardno" type="text" placeholder="请输入银行卡号"/>
        <a href="#" class="easyui-linkbutton" iconCls="icon-query" plain="true" onclick="javascript:bankflowquery()">查询</a>
    </div>
	<table id="dgpapers" title="银行流水查询记录" style="width:auto;height:auto;"
			pagination="true" idField="seqid"
			rownumbers="false" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="merchid" >商户号</th>
                <th field="bankname" >银行名称</th>
				<th field="cardno" >银行卡号</th>
                <th field="mobile" >手机号</th>
                <th field="certno" >身份证号码</th>
				<th field="querydate" >查询时间</th>
				<th field="queryid" >查询批次id</th>
				<th field="opt2" formatter="showdetailcell">详情</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">

			function showdetailcell(value,rec){
				var m = rec.merchid+","+rec.cardno+","+rec.queryid+","+rec.mobile+","+rec.certno;
				var btn = '<a onclick="showdetail(\''+m+'\')" href="javascript:void(0)">查看详情</a>';
				return btn;  
			}
			function showdetail(rec){
				var arrs = rec.split(',');
				var url = '<c:url value='/jsp/model/bankflowdetail.jsp'/>'+'?queryid='+arrs[2];
				var content = '<iframe scrolling="yes" id="questionFrame" frameborder="0"  src="'+url+'" style="width:100%;height:90%;"></iframe>';
	        	
				$('#dlg2').dialog('open').panel({
					title: '银行流水详情-----银行卡号：' + arrs[1] + '  手机号:'+arrs[3]+' 身份证号码:'+arrs[4],
					left:100,
					top:100,
					height: 700,
					closable : true,
					minimizable:false,
					maximizable:true,
	                content : content
				});
			}

	</script>
	
	<!-- 隐藏组件区域 -->
	<div style="display: none">
	    <div id="dlg2" class="easyui-dialog" title="编辑题目" closed="true"
	    	data-options="iconCls:'icon-save',cache:false, border:true, noheader:false"
	    	 style="width:90%;height:600px;padding:10px;background:#fafafa;">
	    </div>
	</div>
</body>
</html>