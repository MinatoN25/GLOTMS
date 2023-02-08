package com.glotms.userservice.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glotms.userservice.dto.UserDTO;
import com.glotms.userservice.enums.Role;
import com.glotms.userservice.enums.Status;
import com.glotms.userservice.model.User;

public interface UserService {

	User validateOtpAndRegisterUser(UserDTO userDto, String otp) throws JsonProcessingException;
	String registerUserAndGenerateOtp(UserDTO userDto) throws JsonProcessingException;
	User deactivateUser(int userId, String userEmail);
	User reactivateUser(int userId, String userEmail);
	User updateUser(UserDTO userDto, String userEmail);
	User getUserById(int userId, String userEmail);
	User grantAdmin(String userEmail, Role role, String loggedInUserEmail);
	User updateProfilePicture(String userEmail, MultipartFile image, String loggedInUserEmail) throws IOException;
	
}
