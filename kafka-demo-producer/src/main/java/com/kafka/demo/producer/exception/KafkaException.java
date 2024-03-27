package com.kafka.demo.producer.exception;

public class KafkaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public KafkaException(String message) {
		super(message);
	}
}
