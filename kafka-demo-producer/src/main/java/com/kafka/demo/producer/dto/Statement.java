package com.kafka.demo.producer.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Statement {

	@Positive
	private Long reference;
	@NotBlank
	private String accountNumber;
	private String description;
	@Positive
	private Double startBalance;
	@Positive
	private Double endBalance;
	private String transactionDate;

	// need to override both hashcode and equals because the records should be
	// unique based on reference number
	@Override
	public int hashCode() {
		return Objects.hash(reference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statement other = (Statement) obj;
		return reference.equals(other.reference);
	}
}
