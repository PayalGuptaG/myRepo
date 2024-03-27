package com.kafka.demo.producer.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.kafka.demo.producer.constants.Constants;
import com.kafka.demo.producer.dto.Customer;
import com.kafka.demo.producer.dto.CustomerInfo;
import com.kafka.demo.producer.dto.Statement;
import com.kafka.demo.producer.exception.DataValidationException;

public class DataValidator {

	// combined predicate
	public static boolean validateRecord(CustomerInfo customer){
		Set<Statement> duplicateRecords = new HashSet<>(); 
		Map<String,String> validationErrors = new HashMap<>();
		
		for(Customer customerData : customer.getCustomers()) {
			for(Statement statement : customerData.getStatement()) {
				Predicate<Statement> validRecord = Utility.validTransactionDatePredicate();
				if (!validRecord.test(statement)) {
					validationErrors.computeIfPresent(Constants.INVALID_DATE_VALIDATION_KEY, (key,val)->{
						val = statement.getTransactionDate() + " ";
						val = val+=validationErrors.get(key);
						return val;
						});
					validationErrors.computeIfAbsent(Constants.INVALID_DATE_VALIDATION_KEY, val->statement.getTransactionDate());
				}
				if (validateDuplicateRecord(statement,duplicateRecords)) {
					validationErrors.computeIfPresent(Constants.DUPLICATE_REFERENCE_KEY, (key,val)->{
						val = statement.getReference().toString() + " ";
						val = val+=validationErrors.get(key);
						return val;
						});
					validationErrors.computeIfAbsent(Constants.DUPLICATE_REFERENCE_KEY, val->statement.getReference().toString()+" "); 
				}
			}
		}		
		if(!validationErrors.isEmpty())
			throw new DataValidationException(validationErrors.toString());
		else
			return true;
	}

	// predicate for duplicate record
	public static boolean validateDuplicateRecord(Statement fileRecord,Set<Statement> duplicateRecords) {

		boolean isDuplicate = false;

		if (Utility.duplicateReferencePredicate(duplicateRecords).test(fileRecord))
			isDuplicate = true;

		return isDuplicate;
	}
}
