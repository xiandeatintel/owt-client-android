/*
 * Copyright (C) 2018 Intel Corporation
 * SPDX-License-Identifier: Apache-2.0
 */
package oms.conference;

import static oms.conference.JsonUtils.getObj;
import static oms.conference.JsonUtils.getString;

import oms.base.Stream.StreamSourceInfo.AudioSourceInfo;
import oms.base.Stream.StreamSourceInfo.VideoSourceInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;

import java.util.HashMap;
import java.util.Iterator;

/**
 * RemoteStream represent the stream published by other endpoints in the conference.
 */
public class RemoteStream extends oms.base.RemoteStream {
    public SubscriptionCapabilities subscriptionCapability;
    public PublicationSettings publicationSettings;

    RemoteStream(JSONObject streamInfo) throws JSONException {
        super(JsonUtils.getString(streamInfo, "id"),
                JsonUtils.getString(streamInfo.getJSONObject("info"), "owner", "mixer"));
        updateStreamInfo(streamInfo, false);
    }

    void updateStreamInfo(JSONObject streamInfo, boolean triggerEvent) throws JSONException {
        JSONObject mediaInfo = JsonUtils.getObj(streamInfo, "media", true);
        publicationSettings = new PublicationSettings(mediaInfo);
        subscriptionCapability = new SubscriptionCapabilities(mediaInfo);

        JSONObject video = JsonUtils.getObj(mediaInfo, "video");
        VideoSourceInfo videoSourceInfo = null;
        if (video != null) {
            videoSourceInfo = VideoSourceInfo.get(JsonUtils.getString(video, "source", "mixed"));
        }

        JSONObject audio = JsonUtils.getObj(mediaInfo, "audio");
        AudioSourceInfo audioSourceInfo = null;
        if (audio != null) {
            audioSourceInfo = AudioSourceInfo.get(JsonUtils.getString(audio, "source", "mixed"));
        }

        setStreamSourceInfo(new StreamSourceInfo(videoSourceInfo, audioSourceInfo));
        setAttributes(JsonUtils.getObj(JsonUtils.getObj(streamInfo, "info"), "attributes"));

        if (triggerEvent) {
            triggerUpdatedEvent();
        }
    }

    MediaStream getMediaStream() {
        return mediaStream;
    }

    void setMediaStream(MediaStream mediaStream) {
        this.mediaStream = mediaStream;
    }

    private void setAttributes(JSONObject attributes) throws JSONException {
        if (attributes == null) {
            return;
        }
        HashMap<String, String> attr = new HashMap<>();
        Iterator<String> keyset = attributes.keys();

        while (keyset.hasNext()) {
            String key = keyset.next();
            String value = attributes.getString(key);
            attr.put(key, value);
        }

        setAttributes(attr);
    }

    void onEnded() {
        triggerEndedEvent();
    }
}
