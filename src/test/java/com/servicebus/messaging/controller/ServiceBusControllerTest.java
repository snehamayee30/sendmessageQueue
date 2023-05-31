
package com.servicebus.messaging.controller;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.servicebus.messaging.entity.Messages;
import com.servicebus.messaging.repo.MessagingRepo;

import static org.mockito.Mockito.*;

public class ServiceBusControllerTest {

	@Mock
	private MessagingRepo messageRepo;
	
	private MockMvc mockMvc;

	@Mock
	private JmsTemplate jmsTemplate;

	@InjectMocks
	private ServiceBusController controller;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSendMessageToSB() {
		String message = "Test Message";
		String expectedResponse = "Message sent successfully to Service bus";

		// String response = controller.sendMessageToSB(message);

		verify(jmsTemplate, times(1)).convertAndSend("spring-order-queue", message);
		//assertEquals(expectedResponse, response);
	}

	@Test
	public void testFetchAllDataFromDB() {
		List<Messages> messages = new ArrayList<>();
		messages.add(new Messages("A01", "PR-A", 3));
		messages.add(new Messages("B01", "PR-B", 1));

		when(messageRepo.findAll()).thenReturn(messages);

		List<Messages> response = controller.fetchAllDatafromDB();

		verify(messageRepo, times(1)).findAll();
		assertEquals(messages, response);
	}
}
