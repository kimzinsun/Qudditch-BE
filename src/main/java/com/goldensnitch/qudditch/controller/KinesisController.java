package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.KinesisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, Object>> createStream(@RequestParam Boolean mediaStorageEnabled) {
        Integer userStoreId = 2;
        try {
            Integer rs = kinesisService.createStream(userStoreId, mediaStorageEnabled);
            return ResponseEntity.ok(Map.of("storeStreamId", rs));
        } catch (Exception e) {
            log.error("Error creating stream", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/stream")
    public ResponseEntity<Map<String, Object>> deleteStream() {
        Integer userStoreId = 2;
        try {
            Integer rs = kinesisService.deleteStream(userStoreId);
            return ResponseEntity.ok(Map.of("storeStreamId", rs));
        } catch (Exception e) {
            log.error("Error deleting stream", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
