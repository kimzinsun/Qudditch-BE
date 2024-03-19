package com.goldensnitch.qudditch.controller;

import com.amazonaws.services.rekognition.model.*;
import com.goldensnitch.qudditch.service.RekognitionService;
import com.goldensnitch.qudditch.service.StreamManagerService;
import com.goldensnitch.qudditch.util.AwsUtil;
import com.goldensnitch.qudditch.vo.rekognotion.LivenessSessionId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rekognition")
public class RekognitionController {
    @Value("${aws.rekognition.collection-id}")
    private String REKOGNITION_COLLECTION_ID;
    private final RekognitionService rekognitionService;
    private final StreamManagerService streamManagerService;
    private final AwsUtil awsUtil;

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
    public LivenessSessionId createSession() {
        try {
            return new LivenessSessionId(rekognitionService.createSession());
        } catch (Exception e) {
            log.error("Error while creating liveness session", e);
            return null;
        }
    }

    @GetMapping("/liveness-session/{sessionId}")
    public GetFaceLivenessSessionResultsResult getSessionResult(@PathVariable String sessionId, @RequestParam Integer userId) {
        try {
            GetFaceLivenessSessionResultsResult flsrr = rekognitionService.getSessionResult(sessionId);
            log.info("getSessionResult: {}", flsrr);
            log.info("addFacesCollection: {}",
                rekognitionService.addFacesCollection(
                    String.valueOf(userId),
                    REKOGNITION_COLLECTION_ID,
                    flsrr
                )
            );
            List<IndexFacesResult> indexFacesResults = rekognitionService.addFacesCollection(
                String.valueOf(userId),
                REKOGNITION_COLLECTION_ID,
                flsrr
            );
            log.info("addFacesCollection: {}", indexFacesResults);
            log.info("createUserInCollection: {}",
                rekognitionService.createUserInCollection(REKOGNITION_COLLECTION_ID, String.valueOf(userId))
            );
            log.info("associateFaces: {}", rekognitionService.associateFaces(
                REKOGNITION_COLLECTION_ID,
                String.valueOf(userId),
                indexFacesResults
            ));
            return flsrr;
        } catch (Exception e) {
            log.error(
                "Error while getting liveness session result for session id: {}",
                sessionId,
                e
            );
            return null;
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
    public DescribeStreamProcessorResult getStreamProcessor() {
        try {
            return streamManagerService.describeStreamProcessor();
        } catch (Exception e) {
            log.error("Error while getting stream processor", e);
            return null;
        }
    }

    @GetMapping("/stream-processors")
    public ListStreamProcessorsResult getStreamProcessorStatus() {
        try {
            return streamManagerService.listStreamProcessors();
        } catch (Exception e) {
            log.error("Error while getting stream processor status", e);
            return null;
        }
    }

    @PostMapping("/customer-enter")
    public Map<String, Object> customerEnter(@RequestBody Map<String, Object> data) {
        log.info("records {}", data);
        List<Map<String, String>> records = (List<Map<String, String>>) data.get("records");
        log.info("customerEnter: {}", awsUtil.getFaceSearchResultFromBase64(records.get(0).get("data")));
        return Map.of("status", "success");
    }
}