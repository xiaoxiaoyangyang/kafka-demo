package com.example.kafkademo.websocket;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class ChatMessageHandler extends TextWebSocketHandler {
    private static final Map<String,WebSocketSession> users;// 这个会出现性能问题，最好用Map来存储，key用userid
    private static Logger logger = LoggerFactory.getLogger(ChatMessageHandler.class);

    static {
        users = new HashMap<String,WebSocketSession>();
    }

    /**
     * 连接成功时候，会触发UI上onopen方法
     * 并且将会话的id进行存储
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Map<String, Object> attributes = webSocketSession.getAttributes();
        String[] split = webSocketSession.getUri().getQuery().split("=");
        String sessionId = (String)attributes.get(split[1]);
        users.put(sessionId,webSocketSession);


    }

    //关闭连接的时候会执行的方法
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        if(webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        Iterator<Map.Entry<String, WebSocketSession>> iterator = users.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            String[] split = webSocketSession.getUri().getQuery().split("=");
            Map<String, Object> attributes = webSocketSession.getAttributes();
            String sessionId = (String) attributes.get(split[1]);
            if(entry.getKey().equals(sessionId)){
                users.remove(sessionId);
                attributes.remove(split[1]);
                break;
            }
        }
    }

    //连接失败时，所需要调用的方法
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if(webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        Iterator<Map.Entry<String, WebSocketSession>> iterator = users.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            String[] split = webSocketSession.getUri().getQuery().split("=");
            Map<String, Object> attributes = webSocketSession.getAttributes();
            String sessionId = (String) attributes.get(split[1]);
            if(entry.getKey().equals(sessionId)){
                users.remove(sessionId);
                attributes.remove(split[1]);
                break;
            }
        }
    }

    //前台进行消息传递的时候
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        String s = webSocketMessage.getPayload().toString();
        Message message = com.alibaba.fastjson.JSONObject.parseObject(s, Message.class);
        System.out.println(message.getContent());
    }


    /**
     * 群发
     *
     */
    public void sendMessageToUsers(String message) throws Exception{
        TextMessage textMessage=new TextMessage(message);
        Iterator<Map.Entry<String, WebSocketSession>> iterator = users.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            if(entry.getValue().isOpen()){
                new Thread(()->{
                    try {
                        entry.getValue().sendMessage(textMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    /**
     * 给某一个用户发送消息
     */
//    public void sendMessageToUser(String message){
//        TextMessage textMessage=new TextMessage(message);
//        Set<String> key = userSocketSessionMap.keySet();
//        if(key.contains(jspCode)){
//            WebSocketSession webSocketSession = userSocketSessionMap.get(jspCode);
//            try {
//                if(webSocketSession.isOpen()){
//                    webSocketSession.sendMessage(textMessage);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
