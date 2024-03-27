package com.kafka.demo.producer.controller.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.demo.producer.dto.CustomerInfo;
import com.kafka.demo.producer.exception.DataValidationException;
import com.kafka.demo.producer.service.KafkaProducer;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unchecked","rawtypes"})
public class KafkaProducerTests {

    private KafkaProducer kafkaProducer;

    
	private KafkaTemplate kafkaTemplate;

    private ObjectMapper objectMapper; 
    
	@BeforeEach
    public void setUp(){
        objectMapper = new ObjectMapper();      
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        kafkaProducer = new KafkaProducer(kafkaTemplate);
    }

	@Test
    public void sendMessageTestSuccess() throws Exception {
        CustomerInfo customerInfo = objectMapper.readValue(new File("src/test/resources/customerInfo.json"),CustomerInfo.class);
        RecordMetadata recordMetadata = Mockito.mock(RecordMetadata.class);
        ProducerRecord<String,CustomerInfo> producerRecord = new ProducerRecord<>("CUSTOMER_TOPIC",customerInfo);
        SendResult<String, CustomerInfo> result = new SendResult<>(producerRecord,recordMetadata);
        CompletableFuture<SendResult<String, CustomerInfo>> future = CompletableFuture.completedFuture(result);
        Mockito.when(kafkaTemplate.send((Message<CustomerInfo>) Mockito.any())).thenReturn(future);       
        boolean sentMessageStatus = kafkaProducer.sendMessage(customerInfo);
        assertTrue(sentMessageStatus);

    }
	
	@Test
    public void sendMessageTestFail() throws Exception {
        CustomerInfo customerInfo = objectMapper.readValue(new File("src/test/resources/invalidstatementInfo.json"),CustomerInfo.class);       
        assertThrows(DataValidationException.class, () ->{ kafkaProducer.sendMessage(customerInfo);});
    }

    @Test
    public void sendMessageTestFailForStatement() throws Exception {
        CustomerInfo customerInfo = objectMapper.readValue(new File("src/test/resources/invalidcustomerstatementInfo.json"),CustomerInfo.class);
        assertThrows(DataValidationException.class, () ->{ kafkaProducer.sendMessage(customerInfo);});
    }

}
