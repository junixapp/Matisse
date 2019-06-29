/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lxj.matisse.internal.entity;

import android.content.pm.ActivityInfo;
import android.support.annotation.StyleRes;

import com.lxj.matisse.CaptureMode;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.R;
import com.lxj.matisse.engine.ImageEngine;
import com.lxj.matisse.engine.impl.GlideEngine;
import com.lxj.matisse.filter.Filter;
import com.lxj.matisse.listener.OnCheckedListener;
import com.lxj.matisse.listener.OnSelectedListener;

import java.util.List;
import java.util.Set;

public final class SelectionSpec {

    public Set<MimeType> mimeTypeSet;
    public boolean mediaTypeExclusive;
    public boolean showSingleMediaType = true;
    @StyleRes
    public int themeId;
    public int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public boolean countable = true;
    public int maxSelectable = 9;
    public int maxImageSelectable;
    public int maxVideoSelectable;
    public List<Filter> filters;
    public boolean capture;
    public CaptureStrategy captureStrategy = new CaptureStrategy();
    public int spanCount;
    public int gridExpectedSize;
    public float thumbnailScale;
    public ImageEngine imageEngine;
    public boolean hasInited;
    public OnSelectedListener onSelectedListener;
    public boolean originalable;
    public boolean autoHideToobar = true;
    public int originalMaxSize = Integer.MAX_VALUE;
    public OnCheckedListener onCheckedListener;
    public boolean isCrop;//是否进行裁剪
    public CaptureMode captureMode = CaptureMode.Image;//默认可以

    private SelectionSpec() {
    }

    public static SelectionSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectionSpec getCleanInstance() {
        SelectionSpec selectionSpec = getInstance();
        selectionSpec.reset();
        return selectionSpec;
    }

    private void reset() {
        mimeTypeSet = null;
        mediaTypeExclusive = true;
        showSingleMediaType = true;
        themeId = R.style.Matisse_Zhihu;
        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        countable = true;
        maxSelectable = 9;
        maxImageSelectable = 0;
        maxVideoSelectable = 0;
        filters = null;
        capture = false;
        captureStrategy = new CaptureStrategy();
        spanCount = 3;
        gridExpectedSize = 0;
        thumbnailScale = 0.5f;
        imageEngine = new GlideEngine();
        hasInited = true;
        originalable = false;
        autoHideToobar = true;
        originalMaxSize = Integer.MAX_VALUE;
        isCrop = false;
        captureMode =  CaptureMode.Image;
    }

    public boolean singleSelectionModeEnabled() {
        return !countable && (maxSelectable == 1 || (maxImageSelectable == 1 && maxVideoSelectable == 1));
    }

    public boolean needOrientationRestriction() {
        return orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    public boolean onlyShowImages() {
        return showSingleMediaType && MimeType.ofImage().containsAll(mimeTypeSet);
    }

    public boolean onlyShowVideos() {
        return showSingleMediaType && MimeType.ofVideo().containsAll(mimeTypeSet);
    }

    private static final class InstanceHolder {
        private static final SelectionSpec INSTANCE = new SelectionSpec();
    }
}
