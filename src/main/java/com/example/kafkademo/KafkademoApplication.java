package com.example.kafkademo;

import com.example.kafkademo.websocket.ChatMessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KafkademoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkademoApplication.class, args);
    }

    @Bean
    public ChatMessageHandler chatMessageHandler(){
        return new ChatMessageHandler();
    }
}
