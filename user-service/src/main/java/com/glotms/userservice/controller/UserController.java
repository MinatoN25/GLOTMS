package com.glotms.userservice.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import com.glotms.userservice.config.MQConfig;
import com.glotms.userservice.dto.MailRequest;
import com.glotms.userservice.dto.UserDTO;
import com.glotms.userservice.enums.Role;
import com.glotms.userservice.exception.OTPExpiredException;
import com.glotms.userservice.exception.OTPMismatchException;
import com.glotms.userservice.exception.UnauthorizedException;
import com.glotms.userservice.exception.UserAlreadyExistsException;
import com.glotms.userservice.exception.UserNotFoundException;
import com.glotms.userservice.model.User;
import com.glotms.userservice.service.UserService;
import com.glotms.userservice.utils.UserConstants;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping(UserConstants.ROOT_PATH)
public class UserController {

	private UserService userService;

	private RabbitTemplate rabbitTemplate;

	@Autowired
	public UserController(UserService userService, RabbitTemplate rabbitTemplate) {
		this.userService = userService;
		this.rabbitTemplate = rabbitTemplate;
	}

	@PostMapping(UserConstants.REGISTER_PATH)
	public ResponseEntity<?> generateOTPForRegistration(@Valid @RequestBody UserDTO userDto) {
		try {
			String otp = userService.registerUserAndGenerateOtp(userDto);
			System.out.println(otp);
//			rabbitTemplate.convertAndSend(MQConfig.MAIL_EXCHANGE, MQConfig.REGISTER_MAIL_ROUTING_KEY,
//					new MailRequest(userDto.getFirstName(), userDto.getUserEmail(), otp));
			System.out.println(otp);
			userDto.setPassword(null);
			return new ResponseEntity<>("Please enter the otp sent to your Email ID", HttpStatus.CREATED);
		} catch (UserAlreadyExistsException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(UserConstants.VALIDATE_OTP_PATH)
	public ResponseEntity<?> validateOTPAndRegisterUser(@Valid @RequestBody UserDTO userDto, @PathVariable String otp) {
		try {

			User user = userService.validateOtpAndRegisterUser(userDto, otp);

//			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.REGISTER_ROUTING_KEY, userDto);
			user.setPassword(null);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (UserAlreadyExistsException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (OTPExpiredException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (OTPMismatchException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PutMapping(UserConstants.DELETE_PATH)
	public ResponseEntity<?> deleteUser(@PathVariable int userId, @RequestAttribute Claims claims) {
		try {
			User user = userService.deactivateUser(userId, claims.getSubject());
//			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.DELETE_ROUTING_KEY, user);
//			rabbitTemplate.convertAndSend(MQConfig.MAIL_EXCHANGE, MQConfig.DELETE_ACCOUNT_MAIL_ROUTING_KEY,
//					new MailRequest(user.getFirstName(), user.getUserEmail()));
			System.out.println("Message sent");
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (UserNotFoundException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (UnauthorizedException b) {
			return new ResponseEntity<>(b.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PutMapping(value = UserConstants.UPDATE_PATH)
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDto, @RequestAttribute Claims claims) {
		try {
			User usero = userService.updateUser(userDto, claims.getSubject());
//			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.REGISTER_ROUTING_KEY, userDto);
			usero.setPassword(null);
			return new ResponseEntity<>(usero, HttpStatus.CREATED);

		} catch (UserNotFoundException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (UnauthorizedException b) {
			return new ResponseEntity<>(b.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(UserConstants.GET_BY_ID_PATH)
	public ResponseEntity<?> getUserByUserId(@RequestParam int userId, @RequestAttribute Claims claims) {
		try {
			return new ResponseEntity<>(userService.getUserById(userId, claims.getSubject()), HttpStatus.CREATED);
		} catch (UnauthorizedException b) {
			return new ResponseEntity<>(b.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (UserNotFoundException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PutMapping(UserConstants.GRANT_ADMIN_PATH)
	public ResponseEntity<?> grantAdmin(@RequestParam String userEmail, @RequestParam Role role,
			@RequestAttribute Claims claims) {
		try {
			User usero = userService.grantAdmin(userEmail, role, claims.getSubject());
			return new ResponseEntity<>(usero, HttpStatus.CREATED);

		} catch (UnauthorizedException b) {
			return new ResponseEntity<>(b.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (UserNotFoundException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/updateProfilePicture", consumes = "multipart/form-data")
	public ResponseEntity<?> updateProfilePicture(@RequestParam String email, @RequestPart(required = false) MultipartFile image,
			@RequestAttribute Claims claims) throws IOException {
		try {
			return new ResponseEntity<>(userService.updateProfilePicture(email, image, claims.getSubject()),
					HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PutMapping(value = "/reactivateUser/{userId}")
	public ResponseEntity<?> reactivateUser(@PathVariable int userId, @RequestAttribute Claims claims) {
		try {
			User usero = userService.reactivateUser(userId, claims.getSubject());
//			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.REGISTER_ROUTING_KEY, usero);
//			rabbitTemplate.convertAndSend(MQConfig.MAIL_EXCHANGE, MQConfig.REACTIVATE_ACCOUNT_MAIL_ROUTING_KEY,
//					new MailRequest(usero.getFirstName(), usero.getUserEmail(), null, usero.getPassword()));
			return new ResponseEntity<>(usero, HttpStatus.CREATED);

		} catch (UserNotFoundException u) {
			return new ResponseEntity<>(u.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (UnauthorizedException b) {
			return new ResponseEntity<>(b.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(UserConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

}
