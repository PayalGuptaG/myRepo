package com.kafka.demo.producer.dto;

import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CustomerInfo {

	@Valid
	private List<Customer> customers;
}
