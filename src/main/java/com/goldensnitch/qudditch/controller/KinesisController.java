package com.goldensnitch.qudditch.controller;

import com.amazonaws.services.kinesisvideo.model.ResourceEndpointListItem;
import com.amazonaws.services.kinesisvideosignalingchannels.model.IceServer;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.KinesisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/kinesis")
public class KinesisController {
    private final KinesisService kinesisService;

    @Autowired
    public KinesisController(KinesisService kinesisService) {
        this.kinesisService = kinesisService;
    }

    @PostMapping("/stream")
    public ResponseEntity<Map<String, Object>> createStream(
        @AuthenticationPrincipal ExtendedUserDetails userDetails,
        @RequestParam Boolean mediaStorageEnabled
    ) {
        try {
            Integer rs = kinesisService.createStream(userDetails.getId(), mediaStorageEnabled);
            return ResponseEntity.ok(Map.of("storeStreamId", rs));
        } catch (Exception e) {
            log.error("Error creating stream", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/stream")
    public ResponseEntity<Map<String, Object>> deleteStream(
        @AuthenticationPrincipal ExtendedUserDetails userDetails
    ) {
        try {
            Integer rs = kinesisService.deleteStream(userDetails.getId());
            return ResponseEntity.ok(Map.of("storeStreamId", rs));
        } catch (Exception e) {
            log.error("Error deleting stream", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/signaling-channel-endpoint")
    public ResponseEntity<Map<String, Object>> getSignalingChannelEndpoint(
        @AuthenticationPrincipal ExtendedUserDetails userDetails
    ) {
        try {
            List<ResourceEndpointListItem> resourceEndpointListItems =
                kinesisService.getSignalingChannelEndpoint(userDetails.getId()).getResourceEndpointList();
            return ResponseEntity.ok(Map.of("signalingChannelEndpoints", resourceEndpointListItems));
        } catch (Exception e) {
            log.error("Error getting signaling channel endpoint", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/ice-server")
    public ResponseEntity<Map<String, Object>> getIceServerConfig(
        @AuthenticationPrincipal ExtendedUserDetails userDetails
    ) {
        try {
            List<IceServer> iceServers = kinesisService.getIceServerConfig(userDetails.getId()).getIceServerList();
            return ResponseEntity.ok(Map.of("iceServers", iceServers));
        } catch (Exception e) {
            log.error("Error getting ice server config", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
