package com.glotms.authenticationservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.glotms.authenticationservice.config.MQConfig;
import com.glotms.authenticationservice.model.User1;
import com.glotms.authenticationservice.repository.UserRepository;

@Component
public class Consumer {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;

	@RabbitListener(queues = MQConfig.REGISTER_QUEUE)
	public void registerFromQueue(User1 userInfo) {
		try {
		System.out.println("Message recieved from register queue : " + userInfo);
	
		User1 user = new User1(userInfo.getUserEmail(), encoder.encode(userInfo.getPassword()));

		userRepository.save(user);
		}catch(Exception e) {
			System.out.println("Exception occured in register listner");
		}
	}
	
	@RabbitListener(queues = MQConfig.DELETE_QUEUE)
	public void deleteFromQueue(User1 userInfo) {
		try {
		System.out.println("Message recieved from delete queue : " + userInfo);

		userRepository.deleteById(userInfo.getUserEmail());
		}catch(Exception e) {
			System.out.println("Exception occured in listner");
		}
	}

}
