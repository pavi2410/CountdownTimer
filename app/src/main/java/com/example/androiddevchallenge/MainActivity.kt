/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val totalTimeSec = 10

    val hasStarted = remember { mutableStateOf(false) }
    val timerSec = remember { mutableStateOf(totalTimeSec) }

    LaunchedEffect(hasStarted.value) {
        if (hasStarted.value) {
            fixedRateTimer("countdown timer", initialDelay = 1000, period = 1000) {
                timerSec.value--
                if (timerSec.value == 0) {
                    this.cancel()
                    timerSec.value-- // stopped state
                }
            }
        }
    }

    Surface(color = Color(0xff101245)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (hasStarted.value) {
                ProgressWheel(timerSec.value, totalTimeSec, onRestart = {
                    hasStarted.value = false
                    timerSec.value = totalTimeSec
                })
            } else {
                // TODO: Add a timer knob
                IconButton(
                    onClick = {
                        hasStarted.value = true
                    },
                    modifier = Modifier.size(128.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play button",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color(0xff02e4e9)
                    )
                }
                Text(
                    text = "Start ${totalTimeSec}s timer",
                    color = Color(0xff02e4e9),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun ProgressWheel(timerSec: Int, totalTimeSec: Int, onRestart: () -> Unit) {
    // timerSec == -1 : stopped state
    // otherwise      : running state

    val progress by animateFloatAsState(
        targetValue = if (timerSec == -1) 1f else timerSec.toFloat() / totalTimeSec
    )
    val color by animateColorAsState(
        targetValue = if (timerSec == -1) Color(0xFFFF073A) else Color(0xff02e4e9)
    )

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = progress,
            color = color,
            strokeWidth = 24.dp,
            modifier = Modifier.size(256.dp)
        )
        if (timerSec > -1) {
            Text(
                text = timerSec.toString(),
                color = Color(0xff02e4e9),
                fontFamily = FontFamily.Monospace,
                fontSize = 64.sp
            )
        } else {
            IconButton(
                onClick = {
                    onRestart()
                },
                modifier = Modifier.size(128.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Restart button",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(0xFFFF073A)
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}
