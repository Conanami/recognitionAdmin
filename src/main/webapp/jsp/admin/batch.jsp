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

        //格式化秒数 为 00:01:30秒
        function formatTimeSeconds(value, rec){
            if(value==undefined){
                return '';
            }
            // 计算
            var h=0,i=0,s=value;
            if(s>60){
                i=parseInt(s/60);
                s=parseInt(s%60);
                if(i > 60) {
                    h=parseInt(i/60);
                    i = parseInt(i%60);
                }
            }
            // 补零
            var zero=function(v){
                return (v>>0)<10?"0"+v:v;
            };
            return [zero(h),zero(i),zero(s)].join(":");
        }

        function showdetailcell(value,rec){
            var m = rec.seqid+","+rec.merchid+","+rec.batchid+","+rec.mark;
            var btn = '<a onclick="showdetail(\''+m+'\')" href="javascript:void(0)">查看详情</a>';
            return btn;
        }
        function showdetail(rec){
            var arrs = rec.split(',');
            var url = '<c:url value='/jsp/model/mobile.jsp'/>'+'?batchid='+arrs[2];
            var content = '<iframe scrolling="yes" id="questionFrame" frameborder="0"  src="'+url+'" style="width:100%;height:90%;"></iframe>';

            $('#dlg2').dialog('open').panel({
                title: '手机状态识别详情-----商户号：' + arrs[1] + '  批次号:'+arrs[2] + '  备注:'+arrs[3],
                left:100,
                top:100,
                height: 700,
                closable : true,
                minimizable:false,
                maximizable:true,
                content : content
            });
        }

        function showImport(){
            $('#uploaddlg').dialog('open');
            $('#uploaddlg').panel({title: "导入手机号列表"});
        }

        function submit(){
            var length = $("#file").val();
            if(length==""){
                alert("请选择需要上传的文件");
            }else{
                //关闭
                $('#uploaddlg').dialog('close');
                ajaxLoading();
//                setTimeout("requestStatus()",1000);	//1秒后执行requestStatus()方法，更新上传进度
                $("#uploadFileForm").ajaxSubmit({
                    success:function(result){
                        ajaxLoadEnd();
                        if(result.respCode!=0){
                            $.messager.alert('提示', result.respDescription,'error');
                        }else{
                            $.messager.alert('提示', result.respDescription,'info');
                            $('#dg').datagrid('reload');
                            resetUploadForm();
                        }
                    }});
            }
        }

        function resetUploadForm(){
            $("#uploadFileForm").resetForm();
            $('#uploadFileForm').find('#colnum').textbox('setValue',1);
            $('#uploadFileForm').find('#rowstart').textbox('setValue',1);
        }
		
	    $(function(){
            $('#dg').edatagrid({
                pageSize: 15,//每页显示的记录条数
                pageList: [15,30,60],//可以设置每页记录条数的列表
				url: "<c:url value='/api.batchlog.query'/>",
                destroyUrl: "<c:url value='/api.batchlog.remove'/>"
			}); 
		});
    </script>
</head>

<body style="padding:6px;">
	<table id="dg" title="导入批次记录" style="width:auto;height:auto"
			toolbar="#toolbar" pagination="true" idField="seqid"
			rownumbers="true" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="seqid" >序号</th>
				<th field="merchid" >商户号</th>
                <th field="batchid" >批次号码</th>
                <th field="totalcount" >总数量</th>
                <th field="mark" >备注</th>
                <th field="createtime" >导入时间</th>
                <th field="callstarttime" >呼叫开始时间</th>
                <th field="callendtime" >呼叫完成时间</th>
                <th field="callcount" >完成呼叫数量</th>
                <th field="recogstarttime" >识别开始时间</th>
                <th field="recogendtime" >识别完成时间</th>
                <th field="recogcount" >完成识别数量</th>
                <th field="totalcalltime" formatter="formatTimeSeconds">总呼叫时间</th>
                <th field="totalrecogtime" formatter="formatTimeSeconds">总识别时间</th>
                <th field="opt2" formatter="showdetailcell">详情</th>
			</tr>
		</thead>
	</table>
	
	<!-- 隐藏组件区域 -->
	<div style="display: none">

        <div id="uploaddlg" class="easyui-dialog" title="选择附件" closed="true" data-options="iconCls:'icon-save'" style="width:600px;height:500px;padding:10px">
            <div>&nbsp;</div>
            <form  id="uploadFileForm" action="<c:url value='/api.mobile.upload'/>" method="POST" enctype="multipart/form-data">
                <table cellpadding="5" width="100%">
                    <tr>
                        <td style="width:40%;">cvs或者txt文件:</td>
                        <td style="width:60%;"><input type='file' name="file" id="file"/></td>
                    </tr>
                    <tr>
                        <td >从第几列取手机号（从1开始）:</td>
                        <td ><input id="colnum" name="colnum" class="easyui-textbox" value="1" style="width:100px;"/></td>
                    </tr>
                    <tr>
                        <td >从第几行开始取手机号（从1开始）:</td>
                        <td ><input id="rowstart" name="rowstart" class="easyui-textbox" value="1" style="width:100px;"/></td>
                    </tr>
                    <tr>
                        <td >备注:</td>
                        <td ><input type='easyui-textbox' name="mark" id="mark" data-options="multiline:true" style="height:60px"/></td>
                    </tr>
                </table>
            </form>
            <div style="text-align:center;padding:5px">
                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submit()">Submit</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="resetUploadForm()">Clear</a>
            </div>
        </div>

		<!-- 工具栏 -->
		<div id="toolbar">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:showImport()">导入</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#dg').edatagrid('destroyRow')">删除</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="javascript:$('#dg').datagrid('reload')">reload</a>
		</div>

        <!-- 弹出的panel -->
        <div id="dlg2" class="easyui-dialog" title="查看详情" closed="true"
             data-options="iconCls:'icon-save',cache:false, border:true, noheader:false"
             style="width:90%;height:600px;padding:10px;background:#fafafa;">
        </div>
	</div>
</body>

</html>