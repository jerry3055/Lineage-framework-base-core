/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.wm;

import static org.mockito.Mockito.mock;

import android.view.SurfaceControl;

import org.mockito.Mockito;

/**
 * Stubbed {@link SurfaceControl.Builder} class that returns a mocked SurfaceControl instance
 * that can be used for unit testing.
 */
class MockSurfaceControlBuilder extends SurfaceControl.Builder {
    @Override
    public SurfaceControl.Builder setParent(SurfaceControl sc) {
        return this;
    }

    @Override
    public SurfaceControl build() {
        SurfaceControl mockSurfaceControl = mock(SurfaceControl.class);
        Mockito.doReturn(true).when(mockSurfaceControl).isValid();
        return mockSurfaceControl;
    }
}
