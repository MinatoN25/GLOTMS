package com.glotms.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Enabled auto index creation wherever annotated.
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return "GLOTMS";
	}
 }
