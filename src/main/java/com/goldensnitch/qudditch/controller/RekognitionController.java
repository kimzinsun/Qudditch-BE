package com.goldensnitch.qudditch.controller;

import com.amazonaws.services.rekognition.model.*;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.RekognitionService;
import com.goldensnitch.qudditch.service.StreamManagerService;
import com.goldensnitch.qudditch.util.AwsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rekognition")
public class RekognitionController {
    private final RekognitionService rekognitionService;
    private final StreamManagerService streamManagerService;
    private final AwsUtil awsUtil;
    @Value("${aws.rekognition.collection-id}")
    private String REKOGNITION_COLLECTION_ID;

    @Autowired
    public RekognitionController(
        RekognitionService rekognitionService,
        StreamManagerService streamManagerService, AwsUtil awsUtil
    ) {
        this.rekognitionService = rekognitionService;
        this.streamManagerService = streamManagerService;
        this.awsUtil = awsUtil;
    }

    @PostMapping("/liveness-session")
    public ResponseEntity<Map<String, Object>> createSession() {
        try {
            String sessionId = rekognitionService.createSession();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("sessionId", sessionId));
        } catch (Exception e) {
            log.error("Error while creating liveness session", e);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/liveness-session/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSessionResult(
        @AuthenticationPrincipal ExtendedUserDetails userDetails,
        @PathVariable String sessionId
    ) {
        try {
            GetFaceLivenessSessionResultsResult flsrr = rekognitionService.getSessionResult(sessionId);
            List<IndexFacesResult> indexFacesResults = rekognitionService.addFacesCollection(
                REKOGNITION_COLLECTION_ID,
                flsrr
            );
            String userId = String.valueOf(userDetails.getId());
            rekognitionService.createUserInCollection(REKOGNITION_COLLECTION_ID, userId);
            rekognitionService.associateFaces(
                REKOGNITION_COLLECTION_ID,
                userId,
                indexFacesResults
            );
            rekognitionService.deleteFaceObjectsFromS3(flsrr);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", flsrr));
        } catch (Exception e) {
            log.error(
                "Error while getting liveness session result for session id: {}",
                sessionId,
                e
            );
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/stream-processor")
    public Map<String, Object> createStreamProcessor() {
        try {
            CreateStreamProcessorResult streamProcessorResult = streamManagerService.createStreamProcessor();
            StartStreamProcessorResult startStreamProcessorResult = streamManagerService.startStreamProcessor();
            return Map.of(
                "createStreamProcessorResult", streamProcessorResult,
                "startStreamProcessorResult", startStreamProcessorResult
            );
        } catch (Exception e) {
            log.error("Error while creating stream processor", e);
            return Map.of("error", e.getMessage());
        }
    }

    @DeleteMapping("/stream-processor")
    public Map<String, Object> stopStreamProcessor() {
        try {
            StopStreamProcessorResult stopStreamProcessorResult = streamManagerService.stopStreamProcessor();
            DeleteStreamProcessorResult deleteStreamProcessorResult = streamManagerService.deleteStreamProcessor();
            return Map.of(
                "stopStreamProcessorResult", stopStreamProcessorResult,
                "deleteStreamProcessorResult", deleteStreamProcessorResult
            );
        } catch (Exception e) {
            log.error("Error while stopping stream processor", e);
            return Map.of("error", e.getMessage());
        }
    }

    @GetMapping("/stream-processor")
    public ResponseEntity<Map<String, Object>> getStreamProcessor() {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("streamProcessor", streamManagerService.describeStreamProcessor()));
        } catch (Exception e) {
            log.error("Error while getting stream processor", e);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stream-processors")
    public ResponseEntity<Map<String, Object>> getStreamProcessorStatus() {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("streamProcessorStatus", streamManagerService.listStreamProcessors()));
        } catch (Exception e) {
            log.error("Error while getting stream processor status", e);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/check-face")
    public ResponseEntity<Map<String, Object>> checkFace(@AuthenticationPrincipal ExtendedUserDetails userDetails, @RequestParam("file") MultipartFile file) {
        try {
            SearchUsersByImageResult searchUsersByImageResult = rekognitionService.searchUsersByImage(file);
            if (!searchUsersByImageResult.getUserMatches().isEmpty()) {
                List<Integer> userIds = rekognitionService.enteredCustomers(
                    userDetails.getId(),
                    searchUsersByImageResult.getUserMatches().stream()
                        .map(UserMatch::getUser)
                        .map(MatchedUser::getUserId)
                        .map(Integer::parseInt).distinct().toList()
                );
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("userIds", userIds));
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "No user found"));
        } catch (Exception e) {
            log.error("Error while checking face", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/customer-enter")
    public ResponseEntity<Map<String, Object>> customerEnter(@RequestBody Map<String, Object> data) {
        try {
            awsUtil.getFaceSearchResult((List<Map<String, String>>) data.get("records"))
                .forEach((userStoreId, matchedFaces) -> {
                        try {
                            rekognitionService.enteredCustomers(userStoreId, awsUtil.getUserIdsFromSearchUsersResult(
                                matchedFaces.stream().map(rekognitionService::searchUsers)
                            ));
                        } catch (Exception e) {
                            log.error("Error while entering customers", e);
                            throw new RuntimeException(e);
                        }
                    }
                );
            return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("requestId", data.get("requestId"), "timestamp", data.get("timestamp")));
        } catch (Exception e) {
            log.error("Error while entering customers", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", e.getMessage(),
                "requestId", data.get("requestId"),
                "timestamp", data.get("timestamp")
            ));
        }
    }
}
