<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 未授权 -->
<!DOCTYPE html>
<html >
<head>
    <meta charset="UTF-8">
    <title></title>
    <style>
        #totalBody{
            width: 100%;
            height: 100%;
            /*background-color: red;*/
            text-align: center;
            margin-top: 100px;
        }
        .errorDesc {
            font-family: '微软雅黑';
            font-size: 22px;
            margin: 10px;
        }
        #sorryInfo{
            color: #333333;
        }
        #noPower{
            color: #999999;
        }
    </style>
</head>
<body >
    <div id="totalBody">
        <div>
            <div> <img src="<c:url value='/images/forbidden01.png'/>"></div>
            <div class="errorDesc" id="sorryInfo">对不起 !</div>
            <div class="errorDesc" id="noPower">你没有权限访问该信息</div>
        </div>
    </div>
</body>
</html>