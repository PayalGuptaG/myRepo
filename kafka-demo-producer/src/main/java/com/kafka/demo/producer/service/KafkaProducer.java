package com.kafka.demo.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.kafka.demo.producer.dto.CustomerInfo;
import com.kafka.demo.producer.exception.KafkaException;
import com.kafka.demo.producer.utils.DataValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducer {

	private KafkaTemplate<String, CustomerInfo> kafkaTemplate;

	@Value("${spring.kafka.topic.name}")
	private String topic;

	public KafkaProducer(KafkaTemplate<String, CustomerInfo> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public boolean sendMessage(CustomerInfo customer) throws KafkaException {
		log.debug("Message sent with customer::{}", customer);
		
		boolean publishMessageSuccess = false;
		
		DataValidator.validateRecord(customer);

		Message<CustomerInfo> message = MessageBuilder.withPayload(customer).setHeader(KafkaHeaders.TOPIC, topic)
				.build();

		log.debug("Final Message sent with customer::{}", message);

		try {
			kafkaTemplate.send(message);
			publishMessageSuccess = true;
		} catch (Exception ex) {
			log.debug("Exception while sending message.");
			throw new KafkaException(ex.getMessage());
		}
		return publishMessageSuccess;
	}
}
