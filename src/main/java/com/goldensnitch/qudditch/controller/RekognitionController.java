package com.goldensnitch.qudditch.controller;

import com.amazonaws.services.rekognition.model.AssociateFacesResult;
import com.amazonaws.services.rekognition.model.CreateUserResult;
import com.amazonaws.services.rekognition.model.GetFaceLivenessSessionResultsResult;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.goldensnitch.qudditch.service.RekognitionService;
import com.goldensnitch.qudditch.service.StreamManagerService;
import com.goldensnitch.qudditch.vo.rekognotion.LivenessSessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rekognition")
public class RekognitionController {
    @Value("${aws.rekognition.collection-id}")
    private String REKOGNITION_COLLECTION_ID;
    private final RekognitionService rekognitionService;
    private final StreamManagerService streamManagerService;

    @Autowired
    public RekognitionController(
        RekognitionService rekognitionService,
        StreamManagerService streamManagerService
    ) {
        this.rekognitionService = rekognitionService;
        this.streamManagerService = streamManagerService;
    }

    @PostMapping("/liveness-session")
    public LivenessSessionId createSession() {
        return new LivenessSessionId(rekognitionService.createSession());
    }

    @GetMapping("/liveness-session/{sessionId}")
    public GetFaceLivenessSessionResultsResult getSessionResult(@PathVariable String sessionId) {
        return rekognitionService.getSessionResult(sessionId);
    }

    @PostMapping("/index-faces")
    public List<IndexFacesResult> indexFaces(
        @RequestBody GetFaceLivenessSessionResultsResult faceLivenessSessionResultsResult
    ) {
        int userId = 1;

        return rekognitionService.addFacesCollection(
            String.valueOf(userId),
            REKOGNITION_COLLECTION_ID,
            faceLivenessSessionResultsResult
        );
    }

    @PostMapping("/user-in-collection")
    public CreateUserResult createUserInCollection() {
        int userId = 1;

        return rekognitionService.createUserInCollection(REKOGNITION_COLLECTION_ID, String.valueOf(userId));
    }

    @PostMapping("/associate-faces")
    public AssociateFacesResult associateFaces(
        @RequestBody List<IndexFacesResult> indexFacesResults
    ) {
        int userId = 1;

        return rekognitionService.associateFaces(
            REKOGNITION_COLLECTION_ID,
            String.valueOf(userId),
            indexFacesResults
        );
    }

}