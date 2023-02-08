package com.glotms.emailservice.consumer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glotms.emailservice.service.EmailService;
import com.glotms.userservice.dto.MailRequest;

@Component
public class KafkaConsumer {

	private EmailService emailService;

	@Autowired
	public KafkaConsumer(EmailService emailService) {
		this.emailService = emailService;
	}

	@KafkaListener(topics = "OTPMailTopic", groupId = "user_group")
	public void registerEmail(String requestObject) {
		System.out.println(requestObject);
		try {

			ObjectMapper mapper = new ObjectMapper();
			MailRequest request = mapper.readValue(requestObject, MailRequest.class);
			Map<String, Object> model = new HashMap<>();
			model.put("firstName", request.getFirstName());
			model.put("email", request.getUserEmail());
			model.put("otp", request.getOtp());
			request.setSubject("Welcome to GLOTMS..!");
			emailService.sendEmail(request, model, "otp-template.ftl");
		} catch (Exception e) {
			System.out.println("Exception occured while sending email");
		}
	}

	@KafkaListener(topics = "delete_user_mail")
	public void deleteAccountEmail(String requestObject) {
		System.out.println(requestObject);
		try {
			ObjectMapper mapper = new ObjectMapper();
			MailRequest request = mapper.readValue(requestObject, MailRequest.class);
			Map<String, Object> model = new HashMap<>();

			model.put("email", request.getUserEmail());
			request.setSubject("Account Deactivated");
			emailService.sendEmail(request, model, "delete-account-template.ftl");
		} catch (Exception e) {
			System.out.println("Exception occured while sending email");
		}
	}

	@KafkaListener(topics = "reactivateMailTopic")
	public void reactivateAccountEmail(String requestObject) {
		System.out.println(requestObject);
		try {
			ObjectMapper mapper = new ObjectMapper();
			MailRequest request = mapper.readValue(requestObject, MailRequest.class);
			Map<String, Object> model = new HashMap<>();
			model.put("password", request.getPassword());
			model.put("email", request.getUserEmail());
			model.put("firstName", request.getFirstName());
			request.setSubject("Account Reactivated");
			emailService.sendEmail(request, model, "reactivate-account.ftl");
		} catch (Exception e) {
			System.out.println("Exception occured while sending email");
		}
	}
}
