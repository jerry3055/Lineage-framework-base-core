/*
 * Copyright (C) 2022 The Pixel Experience Project
 *               2021-2022 crDroid Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.internal.util.custom;

import android.app.Application;
import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PixelPropsUtils {

    private static final boolean DEBUG = false;

    private static final Map<String, Object> propsToChangeMeizu;

    private static final String[] meizuPropToChange = {
            "com.netease.cloudmusic",
            "com.tencent.qqmusic",
            "com.kugou.android",
            "cmccwm.mobilemusic",
            "cn.kuwo.player",
            "com.meizu.media.music"
    };

    static {

        propsToChangeMeizu = new HashMap<>();
        propsToChangeMeizu.put("BRAND", "meizu");
        propsToChangeMeizu.put("MANUFACTURER", "meizu");
        propsToChangeMeizu.put("DEVICE", "meizu18");
        propsToChangeMeizu.put("PRODUCT", "meizu_18_CN");
        propsToChangeMeizu.put("MODEL", "MEIZU 18");
        propsToChangeMeizu.put("FINGERPRINT",
                "meizu/meizu_18_CN/meizu18:11/RKQ1.201105.002/1607588916:user/release-keys");

    }

public static void setProps(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return;
        }
        if (Arrays.asList(packagesToKeep).contains(packageName)) {
            return;
        }

         // Set Props for StatusBar Lyric
    	if (Arrays.asList(meizuPropToChange).contains(packageName)) {
	        if (DEBUG) Log.d(TAG, "Defining props for: " + packageName);
	        for (Map.Entry<String, Object> prop : propsToChangeMeizu.entrySet()) {
    		String key = prop.getKey();
	    	Object value = prop.getValue();
    		if (DEBUG) Log.d(TAG, "Defining " + key + " prop for: " + packageName);
    		setPropValue(key, value);
	        }
	    }
    }
