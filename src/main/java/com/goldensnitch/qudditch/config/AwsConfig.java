package com.goldensnitch.qudditch.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesisvideo.AmazonKinesisVideo;
import com.amazonaws.services.kinesisvideo.AmazonKinesisVideoClient;
import com.amazonaws.services.kinesisvideosignalingchannels.AmazonKinesisVideoSignalingChannelsClient;
import com.amazonaws.services.kinesisvideosignalingchannels.AmazonKinesisVideoSignalingChannelsClientBuilder;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${aws.region}")
    private String region;
    @Value("${aws.access-key-id}")
    private String accessKeyId;
    @Value("${aws.secret-access-key}")
    private String secretAccessKey;

    private BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKeyId, secretAccessKey);
    }

    private AWSStaticCredentialsProvider staticCredentialsProvider() {
        return new AWSStaticCredentialsProvider(basicAWSCredentials());
    }

    @Bean
    public AmazonRekognition rekognitionClient() {
        return AmazonRekognitionClient.builder()
            .withRegion(Regions.fromName(region))
            .withCredentials(staticCredentialsProvider())
            .build();
    }

    @Bean
    public AmazonKinesis kinesisClient() {
        return AmazonKinesisClient.builder()
            .withRegion(Regions.fromName(region))
            .withCredentials(staticCredentialsProvider())
            .build();
    }

    @Bean
    public AmazonKinesisVideoSignalingChannelsClientBuilder kinesisVideoSignalingChannelsClient() {
        return AmazonKinesisVideoSignalingChannelsClient.builder()
            .withCredentials(staticCredentialsProvider());
    }

    @Bean
    public AmazonKinesisVideo kinesisVideoClient() {
        return AmazonKinesisVideoClient.builder()
            .withRegion(Regions.fromName(region))
            .withCredentials(staticCredentialsProvider())
            .build();
    }
}