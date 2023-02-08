package com.glotms.authenticationservice.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.glotms.authenticationservice.model.User1;
import com.glotms.authenticationservice.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		System.out.println(userEmail);
		
		User1 user1 = userRepository.findById(userEmail).orElseThrow();
		
		System.out.println(user1.getPassword());
		return new User(user1.getUserEmail(), user1.getPassword(), new ArrayList<>());
	}

	

}
