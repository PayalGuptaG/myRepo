package com.kafka.demo.producer.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.function.Predicate;

import com.kafka.demo.producer.dto.Statement;

public class Utility {

	public static Predicate<Statement> validTransactionDatePredicate() {
		Predicate<Statement> validTransactionDate = record -> !isDateFuture(record.getTransactionDate(), "yyyy-MM-dd");
		return validTransactionDate;
	}

	// predicate for unique reference check
	public static Predicate<Statement> duplicateReferencePredicate(Set<Statement> fileRecordSet) {
		Predicate<Statement> validReference = record -> !fileRecordSet.add(record);
		return validReference;
	}

	public static boolean isDateFuture(final String date, final String dateFormat) {
		boolean isFutureDate = false;
		LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate inputDate = LocalDate.parse(date, dtf);
		if (inputDate.isAfter(localDate)) {
			isFutureDate = true;
		}
		return isFutureDate;
	}
}
