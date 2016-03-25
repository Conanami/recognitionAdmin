<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<!-- 手机号码状态识别记录 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/metro/easyui.css'/>" />  
    <link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/icon.css'/>" />  
	<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js'/>" ></script>  
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js'/>" ></script> 
    <script type="text/javascript" src="<c:url value='/js/easyui/jquery.edatagrid.js'/>" ></script> 
    <script type="text/javascript" src="<c:url value='/js/jquery.form.js'/>" ></script>
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

		function format_status(value,row){
			if(value==1){
				return '尚未领取';
			}else if(value==2){
				return '已经领取';
			}else if(value==3){
                return '已经拨打';
            }else if(value==4){
                return '已经识别';
            }else if(value==5){
                return '录音文件丢失';
            }else if(value==8){
                return '已经写回';
            }else if(value==9){
                return '号码异常';
            }else if(value==11){
                return '拨打重试失败';
            }else if(value==12){
                return '识别重试失败';
            }
            return '';
	    }

        function format_result(value,row){
            if(value==1){
                return '正常';
            }else if(value==2){
                return '欠费停机';;
            }else if(value==3){
                return '空号';
            }else if(value==4){
                return '关机';
            }else if(value==0){
                return '未知';
            }else if(value==8){
                return '需要重新拨打';
            }else if(value==9){
                return '需要重新识别';
            }else if(value==-2){
                return '无声';
            }
            return '';
        }

        function format_audiourl(value, row){
            if(value!=undefined && value.length>10){
                return value.substring(value.indexOf("filename=")+9, value.length);
            }else{
                return '';
            }
        }
		
		function settingdgcell(value,rec){
			var m = rec.seqid+","+rec.mobile;
			var btn = '<a onclick="settingdg(\''+m+'\')" href="javascript:void(0)">修改</a>';
			return btn;  
		}
		function settingdg(rec){
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
                            $("#uploadFileForm").resetForm();
                            $('#uploadFileForm').find('#colnum').textbox('setValue',1);
                        }
                    }});
            }
        }

		function showNewForm(){
        	$('#dlgnew').dialog('open').dialog('center').dialog('setTitle','新增');
            $('#fm-new').form('clear');
            $('#fm-new').find('#seqid').textbox('setValue',0);
        }
		
		function showEditForm(){
            $('#fm').find('#seqid').textbox('setValue',0);
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('center').dialog('setTitle','查看');
	            $('#fm').form('clear');
                $('#fm').form('load',row);
                $('#fm').find('#dataurl').attr('src', row.dataurl);
            }else{
            	$.messager.alert('提示', '请先选择一笔记录','error');
            }
        }

        //新增的保存
        function saveNew(){
            if($('#fm-new').form('validate')==false){
                return;
            }
            var seqid = $('#fm-new').find("#seqid-new").textbox('getValue');
            var url = "<c:url value='/api.recogs.save'/>";
            $.post(url, {
                seqid : seqid,
                mobile : $('#fm-new').find("#mobile-new").textbox('getValue')
            }, function(result) {
                if(result.respCode!=undefined){
                    $.messager.alert('提示', result.respDescription,'error');
                }else{
                    $('#fm-new').find('#seqid-new').textbox('setValue',result.seqid);
                    $.messager.alert('提示', '新增成功','info');
                    javascript:$('#dg').datagrid('reload');
                }
            });
        }

        //保存修正
		function saveUpdate(){
			if($('#fm').form('validate')==false){
				return;
			}
			var seqid = $('#fm').find("#seqid").textbox('getValue');
			var url = "<c:url value='/api.recogs.update'/>";
			$.post(url, {
				seqid : seqid,
				mobile : $('#fm').find('#mobile').textbox('getValue'),
                merchid : $('#fm').find('#merchid').textbox('getValue'),
                manualresult : $('#fm').find("#manualresult").combobox('getValue')
			}, function(result) {
				if(result.respCode!=undefined){
					$.messager.alert('提示', result.respDescription,'error');
				}else{
					$.messager.alert('提示', '更新成功','info');
					javascript:$('#dg').datagrid('reload');
				}
			});
		}

        //导出某批次的手机号以及状态数据
        function f_explort(){
            var url = "<c:url value='/api.recog.batch.exportExcel'/>";
            ajaxLoading();
//            setTimeout("requestExportStatus()",1000);
            $.post(url, {
                batchid : "${param.batchid}"
            }, function(result) {
                ajaxLoadEnd();
                if (result.respCode==0){
                    $.messager.alert('提示', result.respDescription+'\n点击确定下载文件','info',function(r){
                        window.location = "<c:url value='/download/'/>"+result.rows[0];
                    });
                } else {
                    $.messager.alert('提示', result.respDescription,'error');
                }
            });
        }

        function querySearch(){
            $('#dg').edatagrid({
                pageSize: 15,//每页显示的记录条数
                pageList: [15,30,60],//可以设置每页记录条数的列表
                url: "<c:url value='/api.recogs.query'/>"+"?batchid=${param.batchid}",
                queryParams: {
                    status: $("#statusSearch").combobox('getValue'),         //服务器入参
                    result: $("#resultSearch").combobox('getValue'),
                    manualresult: $("#manualresultSearch").combobox('getValue'),
                    mobile: $("#mobileSearch").textbox('getValue')
                },
                updateUrl: "<c:url value='/api.recogs.update'/>",
                saveUrl: "<c:url value='/api.recogs.save'/>",
                destroyUrl: "<c:url value='/api.recogs.remove'/>"
            });
        }
        $(function(){
            querySearch();
		});
    </script>
</head>

<body style="padding:6px;">
    <div class="easyui-layout" style="width:auto;height:50px;">
        <label style="font-size: small">状态:</label>
        <select class="easyui-combobox" id="statusSearch" name="statusSearch" editable="false" style="width:100px;">
            <option value="99">全部</option>
            <option value="1">尚未领取</option>
            <option value="2">已经领取</option>
            <option value="3">已经拨打</option>
            <option value="4">已经识别</option>
            <option value="5">录音文件丢失</option>
            <option value="9">号码异常</option>
            <option value="8">已经写回</option>
            <option value="11">拨打重试失败</option>
            <option value="12">识别重试失败</option>
        </select>
        <label style="font-size: small">语音识别结果:</label>
        <select class="easyui-combobox" id="resultSearch" name="resultSearch" editable="false" style="width:100px;">
            <option value="99">全部</option>
            <option value="-1">尚未处理</option>
            <option value="-2">无声</option>
            <option value="0">未知</option>
            <option value="8">需要重新拨打</option>
            <option value="9">需要重新识别</option>
            <option value="1">正常</option>
            <option value="2">欠费停机</option>
            <option value="3">空号</option>
            <option value="4">关机</option>
        </select>
        <label style="font-size: small">人工分析结果:</label>
        <select class="easyui-combobox" id="manualresultSearch" name="manualresultSearch" editable="false" style="width:100px;">
            <option value="99">全部</option>
            <option value="-1">尚未处理</option>
            <option value="-2">无声</option>
            <option value="0">未知</option>
            <option value="8">需要重新拨打</option>
            <option value="9">需要重新识别</option>
            <option value="1">正常</option>
            <option value="2">欠费停机</option>
            <option value="3">空号</option>
            <option value="4">关机</option>
        </select>
        <label style="font-size: small">手机号码:</label>
        <input id="mobileSearch" class="easyui-textbox"  placeholder="请输入手机号码"/>

        <br />
        <a href="#" class="easyui-linkbutton" plain="true" onclick="javascript:querySearch()">查询</a>
    </div>
	<table id="dg" title="手机号列表" style="width:auto;height:auto"
			toolbar="#toolbar" pagination="true" idField="seqid"
			rownumbers="true" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
                <th field="mobile" >被叫号码</th>
                <th field="status" formatter="format_status">状态</th>
                <th field="result" formatter="format_result">识别结果</th>
                <th field="manualresult" formatter="format_result">人工结果</th>
                <th field="dataurl" formatter="format_audiourl">录音文件</th>
                <th field="calltime" >拨打时间</th>
                <th field="callcount" >呼叫次数</th>
			</tr>
		</thead>
	</table>
	
	<!-- 隐藏组件区域 -->
	<div style="display: none">

        <div id="uploaddlg" class="easyui-dialog" title="选择附件" closed="true" data-options="iconCls:'icon-save'" style="width:400px;height:200px;padding:10px">
            <div>&nbsp;</div>
            <form  id="uploadFileForm" action="<c:url value='/api.mobile.upload'/>" method="POST" enctype="multipart/form-data">
                <div class="fitem">
                    <label></label>
                    <input type='file' name="file" id="file" style="width:300px;"/> <a href="#" onclick="submit();" >上传</a>
                </div>
                <div class="fitem">
                    <label style="width: 200px;">上传表格中，手机号所在列数:</label>
                    <input id="colnum" name="colnum" class="easyui-textbox" value="1" style="width:100px;"/>
                </div>
            </form>
        </div>

		<!-- 工具栏 -->
		<div id="toolbar">
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:showEditForm()">查看</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="javascript:$('#dg').datagrid('reload')">reload</a>
		</div>

        <!-- 新增 区域 -->
        <div id="dlgnew" class="easyui-dialog" style="width:500px;height:450px;padding:10px 20px"
             closed="true" buttons="#dlg-buttons-new">
            <div class="ftitle">手机号信息</div>
            <form id="fm-new" method="post" >
                <div class="fitem">
                    <label>id:</label>
                    <input id="seqid-new" name="seqid" class="easyui-textbox" editable="false" value="0" >
                </div>
                <div class="fitem">
                    <label>被叫号码:</label>
                    <input id="mobile-new" name="mobile" class="easyui-textbox" required="true">
                </div>
            </form>
        </div>

		<!-- 编辑 区域 -->
		<div id="dlg" class="easyui-dialog" style="width:500px;height:450px;padding:10px 20px"
	            closed="true" buttons="#dlg-buttons">
	        <div class="ftitle">手机号信息</div>
	        <form id="fm" method="post" >
	        	<div class="fitem">
	                <label>id:</label>
	                <input id="seqid" name="seqid" class="easyui-textbox" editable="false">
	            </div>
                <div class="fitem">
                    <label>商户号:</label>
                    <input id="merchid" name="merchid" class="easyui-textbox" editable="false" required="true">
                </div>
                <div class="fitem">
                    <label>被叫号码:</label>
                    <input id="mobile" name="mobile" class="easyui-textbox" editable="false" required="true">
                </div>
                <div class="fitem">
                    <label>录音文件:</label>
                    <audio id="dataurl" name="dataurl" src="audio.wav" preload="auto" controls ></audio>
                </div>
                <label>下载录音文件需要2-3秒，请耐心等待播放键可用</label>
	        </form>
	    </div>

        <!-- 编辑的 工具栏 -->
        <div id="dlg-buttons-new">
            <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveNew()" style="width:90px">Save</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgnew').dialog('close')" style="width:90px">Cancel</a>
        </div>

	    <!-- 编辑的 工具栏 -->
	    <div id="dlg-buttons">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">Cancel</a>
	    </div>
    
	</div>
</body>

</html>