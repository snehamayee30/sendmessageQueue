package com.servicebus.messaging.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicebus.messaging.MessagingApplication;
import com.servicebus.messaging.entity.Messages;
import com.servicebus.messaging.repo.MessagingRepo;

@Controller
@EnableJms
public class ServiceBusController {

	@Autowired
	private MessagingRepo messageRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingApplication.class);
	//private static final String QUEUE_NAME = "spring-queue";
	private static final String QUEUE_NAME = "spring-order-queue";

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@GetMapping("/newMessageToSB")
	public String newMessageToSB() {
		return "index";
	}

	@RequestMapping(value = "/sendMessageToSB", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public String sendMessageToSB(@RequestBody Messages message) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			byte[] messageMapper = objectMapper.writeValueAsBytes(message);
			LOGGER.info("Sending message to SB**", message);
			jmsTemplate.convertAndSend(QUEUE_NAME, messageMapper);
			
			return "Message sent successfully to Service bus";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;	
	}

	@RequestMapping(value = "/allDataFromDb", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Messages> fetchAllDatafromDB() {
		LOGGER.info("All details**");
		return messageRepo.findAll();
	}
}
