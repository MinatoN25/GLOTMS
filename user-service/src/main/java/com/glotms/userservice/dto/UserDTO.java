package com.glotms.userservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.WordUtils;

import com.glotms.userservice.enums.Gender;
import com.glotms.userservice.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	@NotNull(message = "gender can not be null")
	private Gender gender;
	@NotNull(message = "firstName can not be null")
	@NotBlank(message = "firstName can not be blank")
	private String firstName;
	@NotNull(message = "lastName can not be null")
	@NotBlank(message = "lastName can not be blank")
	private String lastName;
	@NotNull(message = "email can not be null")
	@NotBlank(message = "email can not be blank")
	@Email(message = "Please enter a valid email address")
	private String userEmail;
	@NotNull(message = "password can not be null")
	@NotBlank(message = "password can not be blank")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Please enter a valid password")
	private String password;

	public static User convertToEntity(UserDTO userDto) {
		User user = new User();
		user.setFirstName(WordUtils.capitalizeFully(userDto.getFirstName()));
		user.setLastName(WordUtils.capitalizeFully(userDto.getLastName()));
		user.setUserEmail(userDto.getUserEmail());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		return user;

	}

}
