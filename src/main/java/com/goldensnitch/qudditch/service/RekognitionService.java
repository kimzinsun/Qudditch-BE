package com.goldensnitch.qudditch.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.goldensnitch.qudditch.dto.StoreVisitorLog;
import com.goldensnitch.qudditch.mapper.AccessMapper;
import com.goldensnitch.qudditch.util.AwsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
    @Value("${aws.rekognition.collection-id}")
    private String COLLECTION_ID;
    private static final int MAX_USERS = 1;
    private static final int USER_ENTER_TIMEOUT = 1;
    private final RedisService redisService;
    private final AmazonRekognition rekognitionClient;
    private final AwsUtil awsUtil;
    private final AccessMapper accessMapper;

    @Autowired
    public RekognitionService(
        RedisService redisService,
        AmazonRekognition rekognitionClient,
        AwsUtil awsUtil,
        AccessMapper accessMapper
    ) {
        this.redisService = redisService;
        this.rekognitionClient = rekognitionClient;
        this.awsUtil = awsUtil;
        this.accessMapper = accessMapper;
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
        String collectionId,
        GetFaceLivenessSessionResultsResult livenessSessionResult
    ) {
        ArrayList<IndexFacesResult> indexFacesResults = new ArrayList<>();
        livenessSessionResult.getAuditImages()
            .stream()
            .map(auditImage ->
                createIndexFacesRequest(
                    auditImage.getS3Object().getName(),
                    collectionId
                )
            ).forEach((indexFacesRequest) ->
                indexFacesResults.add(rekognitionClient.indexFaces(indexFacesRequest))
            );
        indexFacesResults.add(
            rekognitionClient.indexFaces(
                createIndexFacesRequest(
                    livenessSessionResult.getReferenceImage()
                        .getS3Object()
                        .getName(),
                    collectionId
                )
            )
        );
        return indexFacesResults;
    }

    private IndexFacesRequest createIndexFacesRequest(String key, String collectionId) {
        return new IndexFacesRequest()
            .withImage(getImageFromS3(key))
            .withQualityFilter(QualityFilter.AUTO)
            .withMaxFaces(MAX_FACES)
            .withCollectionId(collectionId)
            .withDetectionAttributes(DETECTION_ATTRIBUTE);
    }

    public void createUserInCollection(String collectionId, String userId) {
        CreateUserRequest request = createUserRequest(collectionId, userId);
        rekognitionClient.createUser(request);
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

    public void associateFaces(
        String collectionId,
        String userId,
        List<IndexFacesResult> indexFacesResults
    ) {
        AssociateFacesRequest request = createAssociateFacesRequest(collectionId, userId, indexFacesResults);
        rekognitionClient.associateFaces(request);
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

    public SearchUsersResult searchUsers(String faceId) {
        SearchUsersRequest request = createSearchUsersRequest(faceId);
        return rekognitionClient.searchUsers(request);
    }

    private SearchUsersRequest createSearchUsersRequest(String faceId) {
        return new SearchUsersRequest()
            .withCollectionId(COLLECTION_ID)
            .withFaceId(faceId)
            .withMaxUsers(MAX_USERS);
    }

    public void enteredCustomers(Integer userStoreId, List<Integer> userIds) {
        for (Integer userId : userIds) {
            if (redisService.checkExistsKey(userId.toString())) {
                continue;
            } else {
                redisService.setValues(
                    userId.toString(), userStoreId.toString(), Duration.ofMinutes(USER_ENTER_TIMEOUT)
                );
            }
            accessMapper.insertVisitLog(
                new StoreVisitorLog(userStoreId, userId)
            );
        }
    }

    private Image getImageFromS3(String key) {
        return new Image().withS3Object(awsUtil.createS3Object(LIVENESS_BUCKET_NAME, key));
    }
}
