package com.kafka.demo.producer.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kafka.demo.producer.service.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.demo.producer.constants.Constants;
import com.kafka.demo.producer.dto.Customer;
import com.kafka.demo.producer.dto.CustomerInfo;
import com.kafka.demo.producer.dto.Statement;

@WebMvcTest()
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class KafkaControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private KafkaProducer kafkaProducer;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	private CustomerInfo validCustomerInfo;
	
	private CustomerInfo inValidCustomerInfo;
	
	@Test
	public void publishTest() throws Exception {
		when(kafkaProducer.sendMessage(Mockito.any())).thenReturn(true);
		CustomerInfo customerInfo = objectMapper.readValue(new File("src/test/resources/customerInfo.json"),CustomerInfo.class);
		
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(Constants.BASE_MAPPING+Constants.PUBLISH_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerInfo)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());		
	}
	
	@Test
	public void publishTestInvalidData() throws Exception {
		when(kafkaProducer.sendMessage(Mockito.any())).thenReturn(true);
		
		CustomerInfo customerInfo = objectMapper.readValue(new File("src/test/resources/invalidcustomerInfo.json"),CustomerInfo.class);

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(Constants.BASE_MAPPING+Constants.PUBLISH_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerInfo)));

        response.andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andDo(MockMvcResultHandlers.print());		
	}
}
