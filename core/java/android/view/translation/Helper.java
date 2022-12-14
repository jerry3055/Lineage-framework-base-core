/*
 * Copyright (C) 2021 The Android Open Source Project
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

package android.view.translation;

/** @hide */
public class Helper {

    // Debug-level flags are defined when service is bound.
    public static boolean sDebug = false;
    public static boolean sVerbose = false;

    // TODO: Use a device config value.
    public static final int ANIMATION_DURATION_MILLIS = 250;

    private Helper() {
        throw new UnsupportedOperationException("contains static members only");
    }
}
