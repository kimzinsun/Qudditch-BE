package com.goldensnitch.qudditch.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.goldensnitch.qudditch.util.AwsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RekognitionService {
    @Value("${aws.rekognition.liveness.output.config.s3-bucket-name}")
    private String LIVENESS_BUCKET_NAME;
    @Value("${aws.rekognition.liveness.setting.audit-images-limit}")
    private Integer AUDIT_IMAGES_LIMIT;
    @Value("${aws.rekognition.index-faces.max-faces}")
    private Integer MAX_FACES;
    @Value("${aws.rekognition.index-faces.detection-attribute}")
    private String DETECTION_ATTRIBUTE;
    private static final String EXTERNAL_IMAGE_ID_PREFIX = "user";

    private final AmazonRekognition rekognitionClient;
    private final AwsUtil awsUtil;

    @Autowired
    public RekognitionService(AmazonRekognition rekognitionClient, AwsUtil awsUtil) {
        this.rekognitionClient = rekognitionClient;
        this.awsUtil = awsUtil;
    }

    public String createSession() {
        CreateFaceLivenessSessionRequest request = new CreateFaceLivenessSessionRequest();
        request.setSettings(createFaceLivenessSessionRequestSettings());
        return rekognitionClient.createFaceLivenessSession(request).getSessionId();
    }

    private CreateFaceLivenessSessionRequestSettings createFaceLivenessSessionRequestSettings() {
        CreateFaceLivenessSessionRequestSettings settings = new CreateFaceLivenessSessionRequestSettings();
        settings.setAuditImagesLimit(AUDIT_IMAGES_LIMIT);
        settings.setOutputConfig(livenessOutputConfig());
        return settings;
    }

    private LivenessOutputConfig livenessOutputConfig() {
        LivenessOutputConfig livenessOutputConfig = new LivenessOutputConfig();
        livenessOutputConfig.setS3Bucket(LIVENESS_BUCKET_NAME);
        return livenessOutputConfig;
    }

    public GetFaceLivenessSessionResultsResult getSessionResult(String sessionId) {
        GetFaceLivenessSessionResultsRequest request =
            new GetFaceLivenessSessionResultsRequest().withSessionId(sessionId);
        return rekognitionClient.getFaceLivenessSessionResults(request);
    }

    public CreateCollectionResult createCollection(String collectionId) {
        CreateCollectionRequest request = new CreateCollectionRequest().withCollectionId(collectionId);
        return rekognitionClient.createCollection(request);
    }

    public DeleteCollectionResult deleteCollection(String collectionId) {
        DeleteCollectionRequest request = new DeleteCollectionRequest().withCollectionId(collectionId);
        return rekognitionClient.deleteCollection(request);
    }

    public List<IndexFacesResult> addFacesCollection(
        String userId,
        String collectionId,
        GetFaceLivenessSessionResultsResult livenessSessionResult
    ) {
        ArrayList<IndexFacesResult> indexFacesResults = new ArrayList<>();
        livenessSessionResult.getAuditImages()
            .stream()
            .map(auditImage ->
                createIndexFacesRequest(
                    userId,
                    auditImage.getS3Object().getName(),
                    collectionId
                )
            ).forEach((indexFacesRequest) ->
                indexFacesResults.add(rekognitionClient.indexFaces(indexFacesRequest))
            );
        indexFacesResults.add(
            rekognitionClient.indexFaces(
                createIndexFacesRequest(
                    userId,
                    livenessSessionResult.getReferenceImage()
                        .getS3Object()
                        .getName(),
                    collectionId
                )
            )
        );
        return indexFacesResults;
    }

    private IndexFacesRequest createIndexFacesRequest(String userId, String key, String collectionId) {
        return new IndexFacesRequest()
            .withImage(getImageFromS3(key))
            .withQualityFilter(QualityFilter.AUTO)
            .withMaxFaces(MAX_FACES)
            .withCollectionId(collectionId)
            .withExternalImageId(EXTERNAL_IMAGE_ID_PREFIX + "-" + userId + "-" + key.split("/")[1])
            .withDetectionAttributes(DETECTION_ATTRIBUTE);
    }

    public CreateUserResult createUserInCollection(String collectionId, String userId) {
        CreateUserRequest request = createUserRequest(collectionId, userId);
        return rekognitionClient.createUser(request);
    }

    private CreateUserRequest createUserRequest(String collectionId, String userId) {
        return new CreateUserRequest().withCollectionId(collectionId).withUserId(userId);
    }

    public DeleteUserResult deleteUserFromCollection(String collectionId, String userId) {
        DeleteUserRequest request = deleteUserRequest(collectionId, userId);
        return rekognitionClient.deleteUser(request);
    }

    private DeleteUserRequest deleteUserRequest(String collectionId, String userId) {
        return new DeleteUserRequest().withCollectionId(collectionId).withUserId(userId);
    }

    public AssociateFacesResult associateFaces(
        String collectionId,
        String userId,
        List<IndexFacesResult> indexFacesResults
    ) {
        AssociateFacesRequest request = createAssociateFacesRequest(collectionId, userId, indexFacesResults);
        return rekognitionClient.associateFaces(request);
    }

    private AssociateFacesRequest createAssociateFacesRequest(
        String collectionId,
        String userId,
        List<IndexFacesResult> indexFacesResults
    ) {
        return new AssociateFacesRequest()
            .withCollectionId(collectionId)
            .withUserId(userId)
            .withFaceIds(
                indexFacesResults
                    .stream()
                    .map(IndexFacesResult::getFaceRecords)
                    .flatMap(List::stream)
                    .map(FaceRecord::getFace)
                    .map(Face::getFaceId)
                    .toList()
            );
    }

    private Image getImageFromS3(String key) {
        return new Image().withS3Object(awsUtil.createS3Object(LIVENESS_BUCKET_NAME, key));
    }
}
