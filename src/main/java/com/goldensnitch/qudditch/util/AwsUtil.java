package com.goldensnitch.qudditch.util;

import com.amazonaws.services.rekognition.model.GetFaceSearchResult;
import com.amazonaws.services.rekognition.model.S3Object;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AwsUtil {
    public S3Object createS3Object(String bucketName, String key) {
        S3Object s3Object = new S3Object();
        s3Object.setBucket(bucketName);
        s3Object.setName(key);
        return s3Object;
    }

    public String getFaceSearchResultFromBase64(String base64) {
        // base64 decoding
        String decoded = new String(java.util.Base64.getDecoder().decode(base64));

        return decoded;
    }
}
