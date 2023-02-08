package com.glotms.userservice.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "otp_data")
public class OtpData {
	
		@Id
	    private String emailId;
	    private String otp;

	    @Field
	    @Indexed(name="someDateField", expireAfterSeconds=60)
	    private Date someDateField;



	
}
