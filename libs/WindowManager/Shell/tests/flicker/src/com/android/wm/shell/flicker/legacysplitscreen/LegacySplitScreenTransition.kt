/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wm.shell.flicker.legacysplitscreen

import android.app.Instrumentation
import android.content.Context
import android.support.test.launcherhelper.LauncherStrategyFactory
import android.view.Surface
import androidx.test.filters.FlakyTest
import androidx.test.platform.app.InstrumentationRegistry
import com.android.server.wm.flicker.FlickerBuilderProvider
import com.android.server.wm.flicker.FlickerTestParameter
import com.android.server.wm.flicker.dsl.FlickerBuilder
import com.android.server.wm.flicker.helpers.openQuickStepAndClearRecentAppsFromOverview
import com.android.server.wm.flicker.helpers.setRotation
import com.android.server.wm.flicker.helpers.wakeUpAndGoToHomeScreen
import com.android.server.wm.traces.common.FlickerComponentName
import com.android.wm.shell.flicker.helpers.BaseAppHelper.Companion.isShellTransitionsEnabled
import com.android.wm.shell.flicker.helpers.MultiWindowHelper.Companion.getDevEnableNonResizableMultiWindow
import com.android.wm.shell.flicker.helpers.MultiWindowHelper.Companion.setDevEnableNonResizableMultiWindow
import com.android.wm.shell.flicker.helpers.SplitScreenHelper
import org.junit.After
import org.junit.Assume.assumeFalse
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test

abstract class LegacySplitScreenTransition(protected val testSpec: FlickerTestParameter) {
    protected val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    protected val context: Context = instrumentation.context
    protected val splitScreenApp = SplitScreenHelper.getPrimary(instrumentation)
    protected val secondaryApp = SplitScreenHelper.getSecondary(instrumentation)
    protected val nonResizeableApp = SplitScreenHelper.getNonResizeable(instrumentation)
    protected val LAUNCHER_COMPONENT = FlickerComponentName("",
            LauncherStrategyFactory.getInstance(instrumentation)
                    .launcherStrategy.supportedLauncherPackage)
    private var prevDevEnableNonResizableMultiWindow = 0

    @Before
    open fun setup() {
        // Only run legacy split tests when the system is using legacy split screen.
        assumeTrue(SplitScreenHelper.isUsingLegacySplit())
        // Legacy split is having some issue with Shell transition, and will be deprecated soon.
        assumeFalse(isShellTransitionsEnabled())
        prevDevEnableNonResizableMultiWindow = getDevEnableNonResizableMultiWindow(context)
        if (prevDevEnableNonResizableMultiWindow != 0) {
            // Turn off the development option
            setDevEnableNonResizableMultiWindow(context, 0)
        }
    }

    @After
    open fun teardown() {
        setDevEnableNonResizableMultiWindow(context, prevDevEnableNonResizableMultiWindow)
    }

    /**
     * List of windows that are ignored when verifying that visible elements appear on 2
     * consecutive entries in the trace.
     *
     * b/182720234
     */
    open val ignoredWindows: List<FlickerComponentName> = listOf(
        FlickerComponentName.SPLASH_SCREEN,
        FlickerComponentName.SNAPSHOT)

    protected open val transition: FlickerBuilder.() -> Unit
        get() = {
            setup {
                eachRun {
                    device.wakeUpAndGoToHomeScreen()
                    device.openQuickStepAndClearRecentAppsFromOverview(wmHelper)
                    secondaryApp.launchViaIntent(wmHelper)
                    splitScreenApp.launchViaIntent(wmHelper)
                    this.setRotation(testSpec.startRotation)
                }
            }
            teardown {
                eachRun {
                    secondaryApp.exit(wmHelper)
                    splitScreenApp.exit(wmHelper)
                    this.setRotation(Surface.ROTATION_0)
                }
            }
        }

    @FlickerBuilderProvider
    fun buildFlicker(): FlickerBuilder {
        return FlickerBuilder(instrumentation).apply {
            transition(this)
        }
    }

    internal open val cleanSetup: FlickerBuilder.() -> Unit
        get() = {
            setup {
                eachRun {
                    device.wakeUpAndGoToHomeScreen()
                    device.openQuickStepAndClearRecentAppsFromOverview(wmHelper)
                    this.setRotation(testSpec.startRotation)
                }
            }
            teardown {
                eachRun {
                    nonResizeableApp.exit(wmHelper)
                    splitScreenApp.exit(wmHelper)
                    device.pressHome()
                    this.setRotation(Surface.ROTATION_0)
                }
            }
        }

    @FlakyTest(bugId = 178447631)
    @Test
    open fun visibleWindowsShownMoreThanOneConsecutiveEntry() {
        testSpec.assertWm {
            this.visibleWindowsShownMoreThanOneConsecutiveEntry(ignoredWindows)
        }
    }

    @FlakyTest(bugId = 178447631)
    @Test
    open fun visibleLayersShownMoreThanOneConsecutiveEntry() {
        testSpec.assertLayers {
            this.visibleLayersShownMoreThanOneConsecutiveEntry(ignoredWindows)
        }
    }

    companion object {
        internal val LIVE_WALLPAPER_COMPONENT = FlickerComponentName("",
            "com.breel.wallpapers18.soundviz.wallpaper.variations.SoundVizWallpaperV2")
        internal val LETTERBOX_COMPONENT = FlickerComponentName("", "Letterbox")
        internal val TOAST_COMPONENT = FlickerComponentName("", "Toast")
    }
}
