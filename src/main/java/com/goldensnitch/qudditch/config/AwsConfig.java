package com.goldensnitch.qudditch.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${aws.profile}")
    private String profile;
    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonRekognition rekognitionClient() {
        return AmazonRekognitionClient.builder()
            .withRegion(Regions.fromName(region))
            .withCredentials(new ProfileCredentialsProvider(profile))
            .build();
    }

    @Bean
    public AmazonKinesis kinesisClient() {
        return AmazonKinesisClient.builder()
            .withRegion(Regions.fromName(region))
            .withCredentials(new ProfileCredentialsProvider(profile))
            .build();
    }
}