package com.glotms.authenticationservice.consumer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glotms.authenticationservice.config.MQConfig;
import com.glotms.authenticationservice.model.User1;
import com.glotms.authenticationservice.repository.UserRepository;

@Component
public class KafkaConsumer {

	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	public KafkaConsumer(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@KafkaListener(topics = "registerTopic", groupId = "user_group")
	public void registerEmail(String requestObject) {
		System.out.println(requestObject);
		try {
			System.out.println("Message recieved from register queue : " + requestObject);
			ObjectMapper mapper = new ObjectMapper();
			User1 user = mapper.readValue(requestObject, User1.class);
			user.setPassword(encoder.encode(user.getPassword()));
			System.out.println(user);
			userRepository.save(user);
			}catch(Exception e) {
				System.out.println("Exception occured in register listner");
			}
	}
	
	@KafkaListener(topics = "delete_user")
	public void deleteFromQueue(String requestObject) {
		try {
		System.out.println("Message recieved from delete queue : " +  requestObject);
		ObjectMapper mapper = new ObjectMapper();
		User1 userInfo = mapper.readValue(requestObject, User1.class);
		userRepository.deleteById(userInfo.getUserEmail());
		}catch(Exception e) {
			System.out.println("Exception occured in listner");
		}
	}

}
