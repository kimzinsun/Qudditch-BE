package com.goldensnitch.qudditch.vo.rekognotion;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FaceSearchResult(
    @JsonProperty("UserStoreId") Integer userStoreId,
    @JsonProperty("MatchedFaces") List<String> matchedFaces
) {
}
