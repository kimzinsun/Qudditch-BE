package com.goldensnitch.qudditch.util;

import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchUsersResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldensnitch.qudditch.vo.rekognotion.FaceSearchResult;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AwsUtil {
    public S3Object createS3Object(String bucketName, String key) {
        S3Object s3Object = new S3Object();
        s3Object.setBucket(bucketName);
        s3Object.setName(key);
        return s3Object;
    }

    public Map<Integer, List<String>> getFaceSearchResult(List<Map<String, String>> encodedResult) {
        return encodedResult.stream()
            .map(result -> {
                String encodedData = result.get("data");
                String decoded = getFaceSearchResultFromBase64(encodedData);
                return parseToFaceSearchResult(decoded);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                FaceSearchResult::userStoreId,
                Collectors.mapping(
                    FaceSearchResult::matchedFaces,
                    Collectors.toList()
                )
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList())
            ));
    }


    private FaceSearchResult parseToFaceSearchResult(String decoded) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(decoded, FaceSearchResult.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String getFaceSearchResultFromBase64(String base64) {
        return new String(Base64.getDecoder().decode(base64));
    }

    public List<Integer> getUserIdsFromSearchUsersResult(Stream<SearchUsersResult> searchUsersResults) {
        return searchUsersResults
            .flatMap(searchUsersResult -> searchUsersResult.getUserMatches().stream())
            .map(userMatch -> Integer.parseInt(userMatch.getUser().getUserId()))
            .collect(Collectors.toList());
    }
}
