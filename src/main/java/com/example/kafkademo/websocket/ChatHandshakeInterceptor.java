package com.example.kafkademo.websocket;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor  {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        System.out.println("Before Handshake");
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String userName = servletRequest.getParameter("username");
        HttpSession session = servletRequest.getSession();
        String sessionId = session.getId();
        attributes.put(userName,sessionId);

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        System.out.println("After Handshake");

        super.afterHandshake(request, response, wsHandler, ex);
    }

}
