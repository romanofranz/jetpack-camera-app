/*
 * Copyright (C) 2023 The Android Open Source Project
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
package com.google.jetpackcamera.feature.quicksettings.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.jetpackcamera.feature.quicksettings.CameraAspectRatio
import com.google.jetpackcamera.feature.quicksettings.CameraCaptureMode
import com.google.jetpackcamera.feature.quicksettings.CameraFlashMode
import com.google.jetpackcamera.feature.quicksettings.CameraLensFace
import com.google.jetpackcamera.feature.quicksettings.QuickSettingsEnum
import com.google.jetpackcamera.quicksettings.R
import com.google.jetpackcamera.settings.model.AspectRatio
import com.google.jetpackcamera.settings.model.CaptureMode
import com.google.jetpackcamera.settings.model.FlashMode
import com.google.jetpackcamera.settings.model.LensFacing
import kotlin.math.min

// completed components ready to go into preview screen

@Composable
fun ExpandedQuickSetRatio(setRatio: (aspectRatio: AspectRatio) -> Unit, currentRatio: AspectRatio) {
    val buttons: Array<@Composable () -> Unit> =
        arrayOf(
            {
                QuickSetRatio(
                    onClick = { setRatio(AspectRatio.THREE_FOUR) },
                    ratio = AspectRatio.THREE_FOUR,
                    currentRatio = currentRatio,
                    isHighlightEnabled = true
                )
            },
            {
                QuickSetRatio(
                    onClick = { setRatio(AspectRatio.NINE_SIXTEEN) },
                    ratio = AspectRatio.NINE_SIXTEEN,
                    currentRatio = currentRatio,
                    isHighlightEnabled = true
                )
            },
            {
                QuickSetRatio(
                    modifier = Modifier.testTag("QuickSetAspectRatio1:1"),
                    onClick = { setRatio(AspectRatio.ONE_ONE) },
                    ratio = AspectRatio.ONE_ONE,
                    currentRatio = currentRatio,
                    isHighlightEnabled = true
                )
            }
        )
    ExpandedQuickSetting(quickSettingButtons = buttons)
}

@Composable
fun QuickSetRatio(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    ratio: AspectRatio,
    currentRatio: AspectRatio,
    isHighlightEnabled: Boolean = false
) {
    val enum =
        when (ratio) {
            AspectRatio.THREE_FOUR -> CameraAspectRatio.THREE_FOUR
            AspectRatio.NINE_SIXTEEN -> CameraAspectRatio.NINE_SIXTEEN
            AspectRatio.ONE_ONE -> CameraAspectRatio.ONE_ONE
            else -> CameraAspectRatio.ONE_ONE
        }
    QuickSettingUiItem(
        modifier = modifier,
        enum = enum,
        onClick = { onClick() },
        isHighLighted = isHighlightEnabled && (ratio == currentRatio)
    )
}

@Composable
fun QuickSetFlash(
    modifier: Modifier = Modifier,
    onClick: (FlashMode) -> Unit,
    currentFlashMode: FlashMode
) {
    val enum = when (currentFlashMode) {
        FlashMode.OFF -> CameraFlashMode.OFF
        FlashMode.AUTO -> CameraFlashMode.AUTO
        FlashMode.ON -> CameraFlashMode.ON
    }
    QuickSettingUiItem(
        modifier = modifier
            .semantics {
                contentDescription =
                    when (enum) {
                        CameraFlashMode.OFF -> "QUICK SETTINGS FLASH IS OFF"
                        CameraFlashMode.AUTO -> "QUICK SETTINGS FLASH IS AUTO"
                        CameraFlashMode.ON -> "QUICK SETTINGS FLASH IS ON"
                    }
            },
        enum = enum,
        isHighLighted = currentFlashMode == FlashMode.ON,
        onClick =
        {
            onClick(currentFlashMode.getNextFlashMode())
        }
    )
}

@Composable
fun QuickFlipCamera(
    modifier: Modifier = Modifier,
    setLensFacing: (LensFacing) -> Unit,
    currentLensFacing: LensFacing
) {
    val enum =
        when (currentLensFacing) {
            LensFacing.FRONT -> CameraLensFace.FRONT
            LensFacing.BACK -> CameraLensFace.BACK
        }
    QuickSettingUiItem(
        modifier = modifier,
        enum = enum,
        onClick = { setLensFacing(currentLensFacing.flip()) }
    )
}

@Composable
fun QuickSetCaptureMode(
    modifier: Modifier = Modifier,
    setCaptureMode: (CaptureMode) -> Unit,
    currentCaptureMode: CaptureMode
) {
    val enum: CameraCaptureMode =
        when (currentCaptureMode) {
            CaptureMode.MULTI_STREAM -> CameraCaptureMode.MULTI_STREAM
            CaptureMode.SINGLE_STREAM -> CameraCaptureMode.SINGLE_STREAM
        }
    QuickSettingUiItem(
        modifier = modifier,
        enum = enum,
        onClick = {
            when (currentCaptureMode) {
                CaptureMode.MULTI_STREAM -> setCaptureMode(CaptureMode.SINGLE_STREAM)
                CaptureMode.SINGLE_STREAM -> setCaptureMode(CaptureMode.MULTI_STREAM)
            }
        }
    )
}

/**
 * Button to toggle quick settings
 */
@Composable
fun ToggleQuickSettingsButton(toggleDropDown: () -> Unit, isOpen: Boolean) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // dropdown icon
        Icon(
            painter = painterResource(R.drawable.baseline_expand_more_72),
            contentDescription = if (isOpen) {
                stringResource(R.string.quick_settings_dropdown_open_description)
            } else {
                stringResource(R.string.quick_settings_dropdown_closed_description)
            },
            modifier = Modifier
                .testTag("QuickSettingDropDown")
                .size(72.dp)
                .clickable {
                    toggleDropDown()
                }
                .scale(1f, if (isOpen) -1f else 1f)
        )
    }
}

// subcomponents used to build completed components

@Composable
fun QuickSettingUiItem(
    modifier: Modifier = Modifier,
    enum: QuickSettingsEnum,
    onClick: () -> Unit,
    isHighLighted: Boolean = false
) {
    QuickSettingUiItem(
        modifier = modifier,
        drawableResId = enum.getDrawableResId(),
        text = stringResource(id = enum.getTextResId()),
        accessibilityText = stringResource(id = enum.getDescriptionResId()),
        onClick = { onClick() },
        isHighLighted = isHighLighted
    )
}

/**
 * The itemized UI component representing each button in quick settings.
 */
@Composable
fun QuickSettingUiItem(
    modifier: Modifier = Modifier,
    @DrawableRes drawableResId: Int,
    text: String,
    accessibilityText: String,
    onClick: () -> Unit,
    isHighLighted: Boolean = false
) {
    Column(
        modifier =
        modifier
            .wrapContentSize()
            .padding(dimensionResource(id = R.dimen.quick_settings_ui_item_padding))
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tint = if (isHighLighted) Color.Yellow else Color.White
        Icon(
            painter = painterResource(drawableResId),
            contentDescription = accessibilityText,
            tint = tint,
            modifier =
            Modifier
                .size(dimensionResource(id = R.dimen.quick_settings_ui_item_icon_size))
        )

        Text(text = text, color = tint, textAlign = TextAlign.Center)
    }
}

/**
 * Should you want to have an expanded view of a single quick setting
 */
@Composable
fun ExpandedQuickSetting(
    modifier: Modifier = Modifier,
    vararg quickSettingButtons: @Composable () -> Unit
) {
    val expandedNumOfColumns =
        min(
            quickSettingButtons.size,
            (
                (
                    LocalConfiguration.current.screenWidthDp.dp - (
                        dimensionResource(
                            id = R.dimen.quick_settings_ui_horizontal_padding
                        ) * 2
                        )
                    ) /
                    (
                        dimensionResource(id = R.dimen.quick_settings_ui_item_icon_size) +
                            (dimensionResource(id = R.dimen.quick_settings_ui_item_padding) * 2)
                        )
                ).toInt()
        )
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(count = expandedNumOfColumns)
    ) {
        items(quickSettingButtons.size) { i ->
            quickSettingButtons[i]()
        }
    }
}

/**
 * Algorithm to determine dimensions of QuickSettings Icon layout
 */
@Composable
fun QuickSettingsGrid(
    modifier: Modifier = Modifier,
    vararg quickSettingsButtons: @Composable () -> Unit
) {
    val initialNumOfColumns =
        min(
            quickSettingsButtons.size,
            (
                (
                    LocalConfiguration.current.screenWidthDp.dp - (
                        dimensionResource(
                            id = R.dimen.quick_settings_ui_horizontal_padding
                        ) * 2
                        )
                    ) /
                    (
                        dimensionResource(id = R.dimen.quick_settings_ui_item_icon_size) +
                            (dimensionResource(id = R.dimen.quick_settings_ui_item_padding) * 2)
                        )
                ).toInt()
        )

    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(count = initialNumOfColumns)
    ) {
        items(quickSettingsButtons.size) { i ->
            quickSettingsButtons[i]()
        }
    }
}

/**
 * The top bar indicators for quick settings items.
 */
@Composable
fun Indicator(enum: QuickSettingsEnum, onClick: () -> Unit) {
    Icon(
        painter = painterResource(enum.getDrawableResId()),
        contentDescription = stringResource(id = enum.getDescriptionResId()),
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.quick_settings_indicator_size))
            .clickable { onClick() }
    )
}

@Composable
fun FlashModeIndicator(currentFlashMode: FlashMode, onClick: (flashMode: FlashMode) -> Unit) {
    val enum = when (currentFlashMode) {
        FlashMode.OFF -> CameraFlashMode.OFF
        FlashMode.AUTO -> CameraFlashMode.AUTO
        FlashMode.ON -> CameraFlashMode.ON
    }
    Indicator(
        enum = enum,
        onClick = {
            onClick(currentFlashMode.getNextFlashMode())
        }
    )
}

@Composable
fun QuickSettingsIndicators(
    currentFlashMode: FlashMode,
    onFlashModeClick: (flashMode: FlashMode) -> Unit
) {
    Row {
        FlashModeIndicator(currentFlashMode, onFlashModeClick)
    }
}

fun FlashMode.getNextFlashMode(): FlashMode {
    return when (this) {
        FlashMode.OFF -> FlashMode.ON
        FlashMode.ON -> FlashMode.AUTO
        FlashMode.AUTO -> FlashMode.OFF
    }
}
