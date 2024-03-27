package com.kafka.demo.producer.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Customer {

	@NotBlank
	private String firstName;
	private String lastName;
	@Email
	private String email;
	@NotBlank
	private String contact;
	@NotBlank
	private String dob;
	@Valid
	private List<Statement> statement;
}
