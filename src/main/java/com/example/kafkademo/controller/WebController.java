package com.example.kafkademo.controller;

import com.example.kafkademo.websocket.ChatMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("webSocket")
public class WebController {
    private static Logger logger=LoggerFactory.getLogger(WebController.class);
    @Autowired
    private ChatMessageHandler chatMessageHandler;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "get",method = RequestMethod.GET)
    public ResponseEntity<?> getValue(){
        String username = httpServletRequest.getParameter("username");
        try {
            chatMessageHandler.sendMessageToUsers(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(username,HttpStatus.OK);
    }
}
