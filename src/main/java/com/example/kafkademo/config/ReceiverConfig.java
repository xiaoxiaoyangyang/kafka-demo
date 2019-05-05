/**
* ReceiverConfig 配置Kafka收消息
* 
* @author wangxinxin
* @since 0.0.1
* @date 2018-04-16
*
*/
package com.example.kafkademo.config;

import com.example.kafkademo.websocket.ChatMessageHandler;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class ReceiverConfig {
  private static final Logger logger = LoggerFactory.getLogger(ReceiverConfig.class);
  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;
  @Value("${kafka.consumer.concurrency}")  
  private int concurrency;
  
  @Value("${kafka.consumer.auto-offset-reset}")
  private String autoOffsetReset;
  @Value("${kafka.consumer.groupid}")  
  private String groupid;

  @Value("${kafka.consumer.enbale-auto-commit}")
  private boolean enbaleAutoCommit;

  @Value("${kafka.consumer.commit-interval}")
  private Integer commitInterval;
  
  public Map<String,Object> consumerConfigs() {
    Map<String,Object> props = new HashMap<>();
    logger.info("bootstrapServers is "+bootstrapServers);
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupid);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enbaleAutoCommit);
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, commitInterval);
    
    return props;
  }

  @Bean
  public ConsumerFactory<String,String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String,String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String,String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setConcurrency(concurrency);  
    return factory;
  }

  @Bean
  public Receiver receiver() {
    return new Receiver();
  }


}