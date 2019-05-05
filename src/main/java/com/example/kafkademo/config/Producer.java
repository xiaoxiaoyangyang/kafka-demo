package com.example.kafkademo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("kafka")
public class Producer {
    private static final Logger logger=LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "send",method = RequestMethod.GET)
    public ResponseEntity<?> send(){
    //    String abc = request.getParameter("abc");
        String message = new String("zhe shi wo yong lai ce shi de");
        kafkaTemplate.send("SensefacePush","1",message);
        return new ResponseEntity<>("hen hao",HttpStatus.OK);
    }
}
