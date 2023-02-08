package com.glotms.userservice.utils;

public final class UserConstants {
	
	private UserConstants() {}
	
	//Controller
	public static final String ROOT_PATH="/user-service";
	public static final String REGISTER_PATH="/generateOTPforRegistration";
	public static final String VALIDATE_OTP_PATH="/validateOTP/{otp}";
	public static final String DELETE_PATH="/deleteUser/{userId}";
	public static final String UPDATE_PATH="/updateUser";
	public static final String GET_BY_ID_PATH="/getUserByUserId";
	public static final String GRANT_ADMIN_PATH="/grantAdmin";
	public static final String CONFLICT_MSG="Conflict Occured";
	public static final String INVALID_PASSWORD_MSG="Please enter valid password";
	public static final String INVALID_EMAIL_MSG="Please enter valid email address";
	
	
	//Service
	public static final String USER_ALREADY_EXISTS_MSG = "User with this email already exists in the system";
	public static final String USER_NOT_FOUND_MSG = "User not found in the system";
	public static final String UNAUTHORIZED_MSG = "You are not authorized to perform this action";
	public static final String OTP_INVALID_MSG = "You have entered wrong OTP, please check carefully";
	public static final String OTP_EXPIRED_MSG = "OTP has been expired, click on resend button to generate again";

}
