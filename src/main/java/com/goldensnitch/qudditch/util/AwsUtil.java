package com.goldensnitch.qudditch.util;

import com.amazonaws.services.rekognition.model.S3Object;
import org.springframework.stereotype.Component;

@Component
public class AwsUtil {
    public S3Object createS3Object(String bucketName, String key) {
        S3Object s3Object = new S3Object();
        s3Object.setBucket(bucketName);
        s3Object.setName(key);
        return s3Object;
    }
}
