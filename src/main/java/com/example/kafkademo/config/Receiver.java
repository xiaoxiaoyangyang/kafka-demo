/**
* Receiver 负责从kafka的SensefacePush这个topic收消息
* 
* @author wangxinxin
* @since 0.0.1
* @date 2018-04-16
*
*/
package com.example.kafkademo.config;


import com.example.kafkademo.websocket.ChatMessageHandler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;



public class Receiver {
	private static final Logger logger=LoggerFactory.getLogger(Receiver.class);
	@Autowired
	private ChatMessageHandler chatMessageHandler;

    @KafkaListener(topics = "${kafka.consumer.topic}")
    public void receive(JSONObject payload) throws Exception{
		JSONObject image =(JSONObject) payload.get("image");
		String url =(String) image.get("url");
		chatMessageHandler.sendMessageToUsers(url);
	}

}