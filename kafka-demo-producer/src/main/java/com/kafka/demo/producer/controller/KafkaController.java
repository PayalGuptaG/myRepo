package com.kafka.demo.producer.controller;

import com.kafka.demo.producer.service.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.demo.producer.constants.Constants;
import com.kafka.demo.producer.dto.CustomerInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;

@RestController
@RequestMapping(Constants.BASE_MAPPING)
@Tag(name = Constants.TAG_NAME, description = Constants.TAG_DESCRIPTION)
public class KafkaController {

	private KafkaProducer kafkaProducer;

	public KafkaController(KafkaProducer kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}

	@Operation(summary = Constants.OPERATION_PUBLISH)
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.HTTP_STATUS_200, description = Constants.SUCCESS_MESSAGE, content = {
					@Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_400, description = Constants.INVALID_DATA, content = @Content),
			@ApiResponse(responseCode = Constants.HTTP_STATUS_500, description = Constants.INTERNAL_SERVER_ERROR, content = @Content) })
	@PostMapping(Constants.PUBLISH_API)
	public ResponseEntity<String> publish(@Valid @RequestBody CustomerInfo customer) {
		kafkaProducer.sendMessage(customer);
		return ResponseEntity.ok(Constants.SUCCESS_MESSAGE);
	}
}
