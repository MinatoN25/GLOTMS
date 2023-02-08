//package com.glotms.searchservice.config;
//
//import java.io.IOException;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.client.RestClients.ElasticsearchRestClient;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.glotms.searchservice.repository")
//@ComponentScan(basePackages = "com.glotms.searchservice")
//public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
//
//	@Value("${spring.elasticsearch.rest.uris}")
//	public String elasticSearchUrl;
//	
////	spring.elasticsearch.rest.uris=localhost:9200
////			spring.elasticsearch.rest.connection-timeout=1s
////			spring.elasticsearch.rest.read-timeout=1m
////			spring.elasticsearch.rest.password=B1V-oThdf7lUNuxuizj8
////			spring.elasticsearch.rest.username=elastic
//
////	@Bean
////	@Override
////	public RestHighLevelClient elasticsearchClient() {
////		final ClientConfiguration config = ClientConfiguration.builder().connectedTo(elasticSearchUrl).build();
////		return RestClients.create(config).rest();
////	}
//	
////	  @Bean
////	  @Override
////	    public RestHighLevelClient elasticsearchClient() {
////	        ClientConfiguration clientConfiguration
////	                = ClientConfiguration.builder()
////	                .connectedTo(elasticSearchUrl)
////	                .withBasicAuth("elastic", "B1V-oThdf7lUNuxuizj8") // put your credentials
////	                .build();
////	        return RestClients.create(clientConfiguration).rest();
////	    }
//
////	    @Bean
////	    public ElasticsearchOperations elasticsearchOperations() {
////	        return new ElasticsearchRestTemplate(elasticsearchClient());
////	    }	
//
//}
