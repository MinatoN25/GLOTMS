package com.glotms.userservice.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glotms.userservice.dto.MailRequest;
import com.glotms.userservice.dto.RegisterUserDTO;
import com.glotms.userservice.dto.UserDTO;
import com.glotms.userservice.enums.Role;
import com.glotms.userservice.enums.Status;
import com.glotms.userservice.exception.OTPExpiredException;
import com.glotms.userservice.exception.OTPMismatchException;
import com.glotms.userservice.exception.UnauthorizedException;
import com.glotms.userservice.exception.UserAlreadyExistsException;
import com.glotms.userservice.exception.UserNotFoundException;
import com.glotms.userservice.model.DatabaseSequence;
import com.glotms.userservice.model.OtpData;
import com.glotms.userservice.model.User;
import com.glotms.userservice.repository.OtpRepository;
import com.glotms.userservice.repository.UserRepository;
import com.glotms.userservice.utils.UserConstants;

@Service
public class UserServiceImpl implements UserService {

	private MongoOperations mongoOp;
	private UserRepository userRepository;
	private OtpRepository otpRepository;
	private KafkaTemplate<String, String> kafkaTemplate;
	private ObjectMapper mapper;
	private Random random= new Random();;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, MongoOperations mongoOp, OtpRepository otpRepository,
			KafkaTemplate<String, String> kafkatemplate) {
		this.userRepository = userRepository;
		this.mongoOp = mongoOp;
		this.otpRepository = otpRepository;
		this.kafkaTemplate = kafkatemplate;
		mapper = new ObjectMapper();
	}

	public void sendMessage(String mailRequest, String topic) {
		Message<String> message = MessageBuilder.withPayload(mailRequest).setHeader(KafkaHeaders.TOPIC, topic).build();
		kafkaTemplate.send(message);

	}

	@Override
	public User deactivateUser(int userId, String userEmail) {
		userAdminSuperAdminAccess(userId, userEmail);
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			user.get().setStatus(Status.INACTIVE);
			userRepository.save(user.get());
			try {
				sendMessage(mapper.writeValueAsString(user), "delete_user");
				sendMessage(
						mapper.writeValueAsString(
								new MailRequest(user.get().getFirstName(), user.get().getUserEmail())),
						"delete_user_mail");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return user.get();
		}
		return null;
		
	}

	@Override
	public User reactivateUser(int userId, String userEmail) {
		adminSuperAdminAccess(userEmail);
		Optional<User> user = userRepository.findById(userId);

		if (user.isPresent()) {

			user.get().setStatus(Status.ACTIVE);
			userRepository.save(user.get());
			user.get().setPassword(generateRandomPassword());
			try {
				sendMessage(mapper.writeValueAsString(user.get()), "registerTopic");
				sendMessage(mapper.writeValueAsString(new MailRequest(user.get().getFirstName(),
						user.get().getUserEmail(), null, user.get().getPassword())), "reactivateMailTopic");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new UserNotFoundException(UserConstants.USER_NOT_FOUND_MSG);
		}
		return user.get();
	}

	@Override
	public User updateUser(UserDTO userDto, String userEmail) {
		User usero = userRepository.findByUserEmail(userDto.getUserEmail());

		userAdminSuperAdminAccess(usero.getUserId(), userEmail);

		User user = UserDTO.convertToEntity(userDto);
		user.setUserId(usero.getUserId());
		user.setImage(usero.getImage());
		user.setPassword(null);
		userRepository.save(user);

		return user;
	}

	@Override
	public User getUserById(int userId, String userEmail) {
		userAdminSuperAdminAccess(userId, userEmail);

		return userRepository.findById(userId).orElseThrow();

	}

	private synchronized int getNextSequence(String seqName) {
		DatabaseSequence counter = mongoOp.findAndModify(query(where("_id").is(seqName)), new Update().inc("seq", 1),
				options().returnNew(true).upsert(true), DatabaseSequence.class);
		Optional<DatabaseSequence> count =Optional.ofNullable(counter);
		if(count.isPresent()) {
			return count.get().getSeq();
		}
		throw new OTPExpiredException("");
	}

	@Override
	public User grantAdmin(String userEmail, Role role, String loggedInUserEmail) {
		User user = userRepository.findByUserEmail(userEmail);
		if (user != null) {
			superAdminAccess(loggedInUserEmail);
			user.setRole(role);
			userRepository.save(user);
			return user;
		} else {
			throw new UserNotFoundException(UserConstants.USER_NOT_FOUND_MSG);
		}
	}

	@Override
	public User validateOtpAndRegisterUser(UserDTO userDto, String otp) throws JsonProcessingException {
		Optional<OtpData> otpData = otpRepository.findById(userDto.getUserEmail());
		if (!otpData.isEmpty()) {
			if (otpData.get().getOtp().equals(otp) && otpData.get().getEmailId().equals(userDto.getUserEmail())) {
				if (userRepository.findByUserEmail(userDto.getUserEmail()) == null) {
					User user = UserDTO.convertToEntity(userDto);
					user.setUserId(getNextSequence(User.SEQUENCE_NAME));
					user.setRole(Role.USER);
					user.setStatus(Status.ACTIVE);
					userRepository.save(user);
					sendMessage(mapper.writeValueAsString(new RegisterUserDTO(user.getUserEmail(), user.getPassword())),
							"registerTopic");
					return user;
				} else {
					throw new UserAlreadyExistsException(UserConstants.USER_ALREADY_EXISTS_MSG);
				}
			} else {
				throw new OTPMismatchException(UserConstants.OTP_INVALID_MSG);
			}
		} else {
			throw new OTPExpiredException(UserConstants.OTP_EXPIRED_MSG);
		}

	}

	@Override
	public String registerUserAndGenerateOtp(UserDTO userDto) throws JsonProcessingException {
		User user = userRepository.findByUserEmail(userDto.getUserEmail());
		if (user == null) {
			OtpData o = new OtpData();
			o.setEmailId(userDto.getUserEmail());
			int n = random.nextInt(999999);
			o.setOtp(String.valueOf(n));
			o.setSomeDateField(new Date());
			otpRepository.save(o);

			sendMessage(
					mapper.writeValueAsString(
							new MailRequest(userDto.getFirstName(), userDto.getUserEmail(), o.getOtp())),
					"OTPMailTopic");
			return o.getOtp();
		} else {
			throw new UserAlreadyExistsException(UserConstants.USER_ALREADY_EXISTS_MSG);
		}

	}

	@Override
	public User updateProfilePicture(String userEmail, MultipartFile image, String loggedInUserEmail)
			throws IOException {
		User user = userRepository.findByUserEmail(userEmail);
		userAdminSuperAdminAccess(user.getUserId(), loggedInUserEmail);

		user.setImage(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
		userRepository.save(user);
		return user;

	}

	private String generateRandomPassword() {
		StringBuffer sb = new StringBuffer();
		sb.append(RandomStringUtils.random(2, 65, 90, true, true));
		sb.append(RandomStringUtils.random(2, 97, 122, true, true));
		sb.append(RandomStringUtils.randomNumeric(2));
		sb.append(RandomStringUtils.random(2, 33, 47, false, false));
		List<Character> listOfChar = sb.chars().mapToObj(data -> (char) data).collect(Collectors.toList());
		Collections.shuffle(listOfChar);
		String password = listOfChar.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();

		return password;
	}

	private boolean userAdminSuperAdminAccess(int userId, String userEmail) {
		Optional<User> user = userRepository.findById(userId);

		if (user.isPresent()) {
			User loggedInUser = userRepository.findByUserEmail(userEmail);
			if (loggedInUser != null) {
				if ((loggedInUser.getRole().equals(Role.ADMIN) || loggedInUser.getRole().equals(Role.SUPER_ADMIN)
						|| user.get().getUserEmail().equals(userEmail))
						&& loggedInUser.getStatus().equals(Status.ACTIVE)) {
					return true;
				} else {
					throw new UnauthorizedException(UserConstants.UNAUTHORIZED_MSG);
				}
			}
		} else {
			throw new UserNotFoundException(UserConstants.USER_NOT_FOUND_MSG);
		}
		return false;

	}

	private boolean adminSuperAdminAccess(String userEmail) {
		User loggedInUser = userRepository.findByUserEmail(userEmail);
		if (loggedInUser != null) {
			if ((loggedInUser.getRole().equals(Role.ADMIN) || loggedInUser.getRole().equals(Role.SUPER_ADMIN))
					&& loggedInUser.getStatus().equals(Status.ACTIVE)) {
				return true;
			} else {
				throw new UnauthorizedException(UserConstants.UNAUTHORIZED_MSG);
			}
		}
		return false;

	}

	private boolean superAdminAccess(String userEmail) {
		User loggedInUser = userRepository.findByUserEmail(userEmail);
		if (loggedInUser != null) {
			if (loggedInUser.getRole().equals(Role.SUPER_ADMIN) && loggedInUser.getStatus().equals(Status.ACTIVE)) {
				return true;
			} else {
				throw new UnauthorizedException(UserConstants.UNAUTHORIZED_MSG);
			}
		}
		return false;

	}

}
