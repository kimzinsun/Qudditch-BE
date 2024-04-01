package com.goldensnitch.qudditch.service;

import com.amazonaws.services.kinesisvideo.AmazonKinesisVideo;
import com.amazonaws.services.kinesisvideo.model.*;
import com.amazonaws.services.kinesisvideosignalingchannels.AmazonKinesisVideoSignalingChannels;
import com.amazonaws.services.kinesisvideosignalingchannels.model.GetIceServerConfigRequest;
import com.amazonaws.services.kinesisvideosignalingchannels.model.GetIceServerConfigResult;
import com.goldensnitch.qudditch.dto.StoreStream;
import com.goldensnitch.qudditch.mapper.StoreStreamMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KinesisService {
    private static final int DATA_RETENTION_IN_HOURS = 1;
    private static final int MESSAGE_TTL_SECONDS = 60;
    private static final String STREAM_PROCESSOR_NAME_PREFIX = "USER_STORE_ID_";
    private static final String VIDEO_STREAM_NAME_PREFIX = "VIDEO_USER_STORE_ID_";
    private static final String CHANNEL_NAME_PREFIX = "CHANNEL_USER_STORE_ID_";
    private final AmazonKinesisVideo kinesisVideoClient;
    private final AmazonKinesisVideoSignalingChannels kinesisVideoSignalingChannelsClient;
    private final StoreStreamMapper storeStreamMapper;

    @Autowired
    public KinesisService(AmazonKinesisVideo kinesisVideoClient, AmazonKinesisVideoSignalingChannels kinesisVideoSignalingChannelsClient, StoreStreamMapper storeStreamMapper) {
        this.kinesisVideoSignalingChannelsClient = kinesisVideoSignalingChannelsClient;
        this.kinesisVideoClient = kinesisVideoClient;
        this.storeStreamMapper = storeStreamMapper;
    }

    public Integer createStream(Integer userStoreId, Boolean mediaStorageEnabled) {
        StoreStream storeStream = new StoreStream();
        storeStream.setUserStoreId(userStoreId);

        CreateStreamResult createVideoStreamResult =
            kinesisVideoClient.createStream(
                createVideoStreamRequest(VIDEO_STREAM_NAME_PREFIX + userStoreId)
            );
        CreateSignalingChannelResult createSignalingChannelResult =
            kinesisVideoClient.createSignalingChannel(
                createSignalingChannelRequest(CHANNEL_NAME_PREFIX + userStoreId)
            );

        String channelARN = createSignalingChannelResult.getChannelARN();
        String videoStreamARN = createVideoStreamResult.getStreamARN();

        if (mediaStorageEnabled) {
            kinesisVideoClient.updateMediaStorageConfiguration(
                createMediaStorageConfigurationRequest(channelARN, videoStreamARN)
            );
        }

        storeStream.setVideoStreamArn(videoStreamARN);
        storeStream.setSignalingChannelArn(channelARN);
        storeStream.setStreamProcessorName(createStreamProcessorName(userStoreId));
        return storeStreamMapper.insertStoreStream(storeStream);
    }

    private String createStreamProcessorName(Integer userStoreId) {
        return STREAM_PROCESSOR_NAME_PREFIX + userStoreId;
    }

    private UpdateMediaStorageConfigurationRequest createMediaStorageConfigurationRequest(String channelARN, String streamARN) {
        return new UpdateMediaStorageConfigurationRequest().withChannelARN(channelARN)
            .withMediaStorageConfiguration(createMediaStorageConfiguration(streamARN));
    }

    private MediaStorageConfiguration createMediaStorageConfiguration(String streamARN) {
        return new MediaStorageConfiguration()
            .withStatus(MediaStorageConfigurationStatus.ENABLED)
            .withStreamARN(streamARN);
    }

    private CreateStreamRequest createVideoStreamRequest(String videoStreamName) {
        return new CreateStreamRequest().withStreamName(videoStreamName)
            .withDataRetentionInHours(DATA_RETENTION_IN_HOURS);
    }

    private CreateSignalingChannelRequest createSignalingChannelRequest(String channelName) {
        return new CreateSignalingChannelRequest().withChannelName(channelName)
            .withSingleMasterConfiguration(new SingleMasterConfiguration().withMessageTtlSeconds(MESSAGE_TTL_SECONDS));
    }

    public Integer deleteStream(Integer userStoreId) {
        StoreStream storeStream = storeStreamMapper.selectStoreStreamByUserStoreId(userStoreId);
        String videoStreamARN = storeStream.getVideoStreamArn();
        String channelARN = storeStream.getSignalingChannelArn();

        try {
            kinesisVideoClient.updateMediaStorageConfiguration(
                new UpdateMediaStorageConfigurationRequest().withChannelARN(channelARN)
                    .withMediaStorageConfiguration(
                        new MediaStorageConfiguration().withStatus(MediaStorageConfigurationStatus.DISABLED)
                    )
            );
        } catch (Exception e) {
            log.error("Error disabling media storage configuration", e);
        }

        kinesisVideoClient.deleteStream(new DeleteStreamRequest().withStreamARN(videoStreamARN));
        kinesisVideoClient.deleteSignalingChannel(new DeleteSignalingChannelRequest().withChannelARN(channelARN));

        return storeStreamMapper.deleteStoreStreamByUserStoreId(userStoreId);
    }

    public GetSignalingChannelEndpointResult getSignalingChannelEndpoint(Integer userStoreId) {
        StoreStream storeStream = storeStreamMapper.selectStoreStreamByUserStoreId(userStoreId);
        return kinesisVideoClient.getSignalingChannelEndpoint(
            createSignalingChannelEndpointRequest(storeStream.getSignalingChannelArn())
        );
    }

    private GetSignalingChannelEndpointRequest createSignalingChannelEndpointRequest(String channelARN) {
        return new GetSignalingChannelEndpointRequest()
            .withChannelARN(channelARN)
            .withSingleMasterChannelEndpointConfiguration(
                createSingleMasterChannelEndpointConfiguration()
            );
    }

    private SingleMasterChannelEndpointConfiguration createSingleMasterChannelEndpointConfiguration() {
        return new SingleMasterChannelEndpointConfiguration()
            .withProtocols(ChannelProtocol.WSS, ChannelProtocol.HTTPS)
            .withRole(ChannelRole.MASTER);
    }

    public GetIceServerConfigResult getIceServerConfig(Integer userStoreId) {
        StoreStream storeStream = storeStreamMapper.selectStoreStreamByUserStoreId(userStoreId);
        return kinesisVideoSignalingChannelsClient.getIceServerConfig(createIceServerConfigRequest(storeStream.getSignalingChannelArn()));
    }

    private GetIceServerConfigRequest createIceServerConfigRequest(String channelARN) {
        return new GetIceServerConfigRequest().withChannelARN(channelARN);
    }
}
