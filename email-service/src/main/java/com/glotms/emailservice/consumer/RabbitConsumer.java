//package com.glotms.emailservice.consumer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.glotms.emailservice.config.MQConfig;
//import com.glotms.emailservice.dto.MailRequest;
//import com.glotms.emailservice.service.EmailService;
//
//@Component
//public class RabbitConsumer {
//	
//	private EmailService emailService;
//	
//	@Autowired
//	public RabbitConsumer(EmailService emailService) {
//		this.emailService=emailService;
//	}
//	
//	@RabbitListener(queues = MQConfig.REGISTER_MAIL_QUEUE)
//	public void registerEmail(MailRequest request) {
//		System.out.println(request);
//		try {
//		Map<String, Object> model = new HashMap<>();
//		model.put("firstName", request.getFirstName());
//		model.put("email", request.getUserEmail());
//		model.put("otp", request.getOtp());
//		request.setSubject("Welcome to GLOTMS..!");
//		emailService.sendEmail(request, model,"otp-template.ftl");
//		}catch(Exception e) {
//			System.out.println("Exception occured while sending email");
//		}
//	}
//	
//	@RabbitListener(queues = MQConfig.DELETE_ACCOUNT_MAIL_QUEUE)
//	public void deleteAccountEmail(MailRequest request) {
//		System.out.println(request);
//		try {
//		Map<String, Object> model = new HashMap<>();
//		
//		model.put("email", request.getUserEmail());
//		request.setSubject("Account Deactivated");
//		emailService.sendEmail(request, model,"delete-account-template.ftl");
//		}catch(Exception e) {
//			System.out.println("Exception occured while sending email");
//		}
//	}
//	
//	@RabbitListener(queues = MQConfig.REACTIVATE_ACCOUNT_MAIL_QUEUE)
//	public void reactivateAccountEmail(MailRequest request) {
//		System.out.println(request);
//		try {
//		Map<String, Object> model = new HashMap<>();
//		model.put("password", request.getPassword());
//		model.put("email", request.getUserEmail());
//		model.put("firstName", request.getFirstName());
//		request.setSubject("Account Reactivated");
//		emailService.sendEmail(request, model,"reactivate-account.ftl");
//		}catch(Exception e) {
//			System.out.println("Exception occured while sending email");
//		}
//	}
//}
