<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'index.jsp' starting page</title>
    <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/sockjs.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/stomp.js"></script>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <script type="text/javascript">
        $(document).ready(function(){
            $("#connect").on("click",function(){
                connect();
            })

            $("#disconnect").on("click",function(){
                disconnect();
            })

            $("#sendName").on("click",function(){
                sendName();
            })
            var websocket;
            function setConnected(connected) {
                document.getElementById('connect').disabled = connected;
                document.getElementById('disconnect').disabled = !connected;
            }
            function connect() {
                var name=$("#name").val();
                if ('WebSocket' in window) {
                    websocket = new WebSocket("ws://" + document.location.host + "/webSocketServer?username="+name);
                } else if ('MozWebSocket' in window) {
                    websocket = new MozWebSocket("ws://" + document.location.host + "/webSocketServer?username="+name);
                } else {
                    websocket = new SockJS("http://" + document.location.host + "/sockjs/webSocketServer?username="+name);
                }
                websocket.onopen = function(evnt) {
                    setConnected(true);
                    console.log("lian jie jian li cheng gong")
                };

                websocket.onmessage = function(event) {
                    var data=event.data;
                    console.log(data);
                    $("#response")[0].innerHTML = data;
                    $("img").attr('src',data) ;
                };

                websocket.onerror = function(evnt) {
                    console.log("lian jie chu xian yi chang")
                };

                websocket.onclose = function(evnt) {
                    setConnected(false);
                    console.log("lian jie jian li si bai")
                }
            }
            function disconnect() {
                if(websocket!=null){
                    websocket.close();
                }
                setConnected(false);
                console.log("lian jie duan kai");
            }
            function sendName() {
                var name=$("#name").val();
                websocket.send(JSON.stringify({"content":name,"from":"xiaoming","to":"yang"}));
            }
        })
    </script>
</head>

<body>
<div>
    <div>
        <button id="connect" >连接</button>
        <button id="disconnect" disabled="disabled">断开连接</button>
    </div>

    <div id="conversationDiv">

        <label>输入你的名字</label><input type="text" id="name"/>
        <button id="sendName">发送</button>
        <p id="response"></p></hr>
        <img src="" width="250px" height="600px"/>

        <%--<a href="http://localhost:8081/webSocket/get?username=xiaoming">点击</a>--%>

    </div>
</div>
</body>
</html>

