package com.goldensnitch.qudditch.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.goldensnitch.qudditch.dto.StoreStream;
import com.goldensnitch.qudditch.mapper.StoreStreamMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamManagerService {
    @Value("${aws.rekognition.kinesis-data-stream-arn}")
    private String KINESIS_DATA_STREAM_ARN;
    @Value("${aws.rekognition.role-arn}")
    private String ROLE_ARN;
    @Value("${aws.rekognition.collection-id}")
    private String COLLECTION_ID;
    @Value("${aws.rekognition.match-threshold}")
    private float MATCH_THRESHOLD;
    private static final int LIST_STREAM_PROCESSORS_MAX_RESULTS = 100;
    private final AmazonRekognition rekognitionClient;
    private final StoreStreamMapper storeStreamMapper;

    @Autowired
    public StreamManagerService(AmazonRekognition rekognitionClient, StoreStreamMapper storeStreamMapper) {
        this.rekognitionClient = rekognitionClient;
        this.storeStreamMapper = storeStreamMapper;
    }

    private int getUserStoreId() {
        ExtendedUserDetails userDetails = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return userDetails.getId();
    }

    private StoreStream getCurrentStoreStream() {
        return storeStreamMapper.selectStoreStreamByUserStoreId(getUserStoreId());
    }

    public CreateStreamProcessorResult createStreamProcessor() {
        //Setup input parameters
        KinesisVideoStream kinesisVideoStream = new KinesisVideoStream()
            .withArn(getCurrentStoreStream().getVideoStreamArn());
        StreamProcessorInput streamProcessorInput =
            new StreamProcessorInput().withKinesisVideoStream(kinesisVideoStream);
        KinesisDataStream kinesisDataStream = new KinesisDataStream().withArn(KINESIS_DATA_STREAM_ARN);
        StreamProcessorOutput streamProcessorOutput =
            new StreamProcessorOutput().withKinesisDataStream(kinesisDataStream);
        FaceSearchSettings faceSearchSettings =
            new FaceSearchSettings().withCollectionId(COLLECTION_ID).withFaceMatchThreshold(MATCH_THRESHOLD);
        StreamProcessorSettings streamProcessorSettings =
            new StreamProcessorSettings().withFaceSearch(faceSearchSettings);

        //Create the stream processor
        return rekognitionClient.createStreamProcessor(
            new CreateStreamProcessorRequest().withInput(streamProcessorInput).withOutput(streamProcessorOutput)
                .withSettings(streamProcessorSettings).withRoleArn(ROLE_ARN)
                .withName(getCurrentStoreStream().getStreamProcessorName()));
    }

    public StartStreamProcessorResult startStreamProcessor() {
        return rekognitionClient.startStreamProcessor(
            new StartStreamProcessorRequest().withName(getCurrentStoreStream().getStreamProcessorName())
        );
    }

    public StopStreamProcessorResult stopStreamProcessor() {
        return rekognitionClient.stopStreamProcessor(
            new StopStreamProcessorRequest().withName(getCurrentStoreStream().getStreamProcessorName())
        );
    }

    public DeleteStreamProcessorResult deleteStreamProcessor() {
        return rekognitionClient.deleteStreamProcessor(
            new DeleteStreamProcessorRequest().withName(getCurrentStoreStream().getStreamProcessorName())
        );
    }

    public DescribeStreamProcessorResult describeStreamProcessor() {
        return rekognitionClient.describeStreamProcessor(
            new DescribeStreamProcessorRequest().withName(getCurrentStoreStream().getStreamProcessorName())
        );
    }

    public ListStreamProcessorsResult listStreamProcessors() {
        return
            rekognitionClient.listStreamProcessors(
                new ListStreamProcessorsRequest().withMaxResults(LIST_STREAM_PROCESSORS_MAX_RESULTS)
            );

    }
}