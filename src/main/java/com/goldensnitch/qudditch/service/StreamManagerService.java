//Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//PDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-rekognition-developer-guide/blob/master/LICENSE-SAMPLECODE.)

// Stream manager class. Provides methods for calling
// Stream Processor operations.
package com.goldensnitch.qudditch.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamManagerService {
    @Value("${aws.rekognition.stream-processor-name}")
    private String streamProcessorName;
    @Value("${aws.rekognition.kinesis-video-stream-arn}")
    private String kinesisVideoStreamArn;
    @Value("${aws.rekognition.kinesis-data-stream-arn}")
    private String kinesisDataStreamArn;
    @Value("${aws.rekognition.role-arn}")
    private String roleArn;
    @Value("${aws.rekognition.collection-id}")
    private String collectionId;
    @Value("${aws.rekognition.match-threshold}")
    private float matchThreshold;
    private static final int LIST_STREAM_PROCESSORS_MAX_RESULTS = 100;

    private final AmazonRekognition rekognitionClient;

    @Autowired
    public StreamManagerService(AmazonRekognition rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    public void createStreamProcessor() {
        //Setup input parameters
        KinesisVideoStream kinesisVideoStream = new KinesisVideoStream().withArn(kinesisVideoStreamArn);
        StreamProcessorInput streamProcessorInput =
            new StreamProcessorInput().withKinesisVideoStream(kinesisVideoStream);
        KinesisDataStream kinesisDataStream = new KinesisDataStream().withArn(kinesisDataStreamArn);
        StreamProcessorOutput streamProcessorOutput =
            new StreamProcessorOutput().withKinesisDataStream(kinesisDataStream);
        FaceSearchSettings faceSearchSettings =
            new FaceSearchSettings().withCollectionId(collectionId).withFaceMatchThreshold(matchThreshold);
        StreamProcessorSettings streamProcessorSettings =
            new StreamProcessorSettings().withFaceSearch(faceSearchSettings);

        //Create the stream processor
        CreateStreamProcessorResult createStreamProcessorResult = rekognitionClient.createStreamProcessor(
            new CreateStreamProcessorRequest().withInput(streamProcessorInput).withOutput(streamProcessorOutput)
                .withSettings(streamProcessorSettings).withRoleArn(roleArn).withName(streamProcessorName));

        //Display result
        log.info("Stream Processor " + streamProcessorName + " created.");
        log.info("StreamProcessorArn - " + createStreamProcessorResult.getStreamProcessorArn());
    }

    public void startStreamProcessor() {
        StartStreamProcessorResult startStreamProcessorResult =
            rekognitionClient.startStreamProcessor(new StartStreamProcessorRequest().withName(streamProcessorName));
        log.info("Stream Processor " + streamProcessorName + " started.");
    }

    public void stopStreamProcessor() {
        StopStreamProcessorResult stopStreamProcessorResult =
            rekognitionClient.stopStreamProcessor(new StopStreamProcessorRequest().withName(streamProcessorName));
        log.info("Stream Processor " + streamProcessorName + " stopped.");
    }

    public void deleteStreamProcessor() {
        DeleteStreamProcessorResult deleteStreamProcessorResult = rekognitionClient
            .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(streamProcessorName));
        log.info("Stream Processor " + streamProcessorName + " deleted.");
    }

    public void describeStreamProcessor() {
        DescribeStreamProcessorResult describeStreamProcessorResult = rekognitionClient
            .describeStreamProcessor(new DescribeStreamProcessorRequest().withName(streamProcessorName));

        //Display various stream processor attributes.
        log.info("Arn - " + describeStreamProcessorResult.getStreamProcessorArn());
        log.info("Input kinesisVideo stream - "
            + describeStreamProcessorResult.getInput().getKinesisVideoStream().getArn());
        log.info("Output kinesisData stream - "
            + describeStreamProcessorResult.getOutput().getKinesisDataStream().getArn());
        log.info("RoleArn - " + describeStreamProcessorResult.getRoleArn());
        log.info(
            "CollectionId - " + describeStreamProcessorResult.getSettings().getFaceSearch().getCollectionId());
        log.info("Status - " + describeStreamProcessorResult.getStatus());
        log.info("Status message - " + describeStreamProcessorResult.getStatusMessage());
        log.info("Creation timestamp - " + describeStreamProcessorResult.getCreationTimestamp());
        log.info("Last update timestamp - " + describeStreamProcessorResult.getLastUpdateTimestamp());
    }

    public void listStreamProcessors() {
        ListStreamProcessorsResult listStreamProcessorsResult =
            rekognitionClient.listStreamProcessors(
                new ListStreamProcessorsRequest().withMaxResults(LIST_STREAM_PROCESSORS_MAX_RESULTS)
            );

        //List all stream processors (and state) returned from Rekognition
        for (StreamProcessor streamProcessor : listStreamProcessorsResult.getStreamProcessors()) {
            log.info("StreamProcessor name - " + streamProcessor.getName());
            log.info("Status - " + streamProcessor.getStatus());
        }
    }
}