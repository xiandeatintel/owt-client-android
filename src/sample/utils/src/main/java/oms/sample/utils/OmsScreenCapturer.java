/*
 * Copyright (C) 2018 Intel Corporation
 * SPDX-License-Identifier: Apache-2.0
 */
package oms.sample.utils;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.os.Build;

import oms.base.Stream;
import oms.base.VideoCapturer;

import org.webrtc.ScreenCapturerAndroid;

public class OmsScreenCapturer extends ScreenCapturerAndroid implements VideoCapturer{
    private int width, height;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OmsScreenCapturer(Intent data, int width, int height) {
        super(data, new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
            }
        });
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getFps() {
        // ignored
        return 0;
    }

    @Override
    public Stream.StreamSourceInfo.VideoSourceInfo getVideoSource() {
        return Stream.StreamSourceInfo.VideoSourceInfo.SCREEN_CAST;
    }
}
