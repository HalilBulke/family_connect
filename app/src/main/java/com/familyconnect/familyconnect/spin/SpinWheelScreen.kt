package com.familyconnect.familyconnect.spin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.spin.SpinWheelComponent
import com.familyconnect.familyconnect.spin.SpinWheelItem
import com.familyconnect.familyconnect.spin.rememberSpinWheelState
import com.familyconnect.familyconnect.util.toColor
import kotlinx.collections.immutable.toPersistentList
import kotlin.random.Random

@Composable
fun SpinWheelScreen() {
    val colors1 = remember {
        listOf(
            "380048",
            "2B003D",
            "40004A",
            "590058",
            "730067"
        ).map { it.toColor() }
    }

    val colors2 = remember {
        listOf(
            "F9A114",
            "FD7D1B",
            "F9901A",
            "F6A019",
            "EFC017"
        ).map { it.toColor() }
    }

    val items = remember {
        List(8) { index ->
            val colors = if (index % 2 == 0) colors1 else colors2

            SpinWheelItem(
                colors = colors.toPersistentList()
            ) {
                Text(
                    text = "$index",
                    style = TextStyle(color = Color(0xFF4CAF50), fontSize = 20.sp)
                )
            }

        }.toPersistentList()
    }
    var pickerValue by remember { mutableIntStateOf(Random.nextInt(0, items.size)) }
    Log.d("picker",pickerValue.toString() )
    val spinState = rememberSpinWheelState(
        items = items,
        backgroundImage = R.drawable.spin_wheel,
        centerImage = R.drawable.ic_family_connect,
        indicatorImage = R.drawable.spin_wheel_tick,
        onSpinningFinished = null,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        Box(modifier = Modifier.size(300.dp)) {
            SpinWheelComponent(spinState)
        }
        Spacer(modifier = Modifier.size(80.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                spinState.goto(0)
            }) {
                Text(text = "RESET")
            }
            Button(onClick = {
                val randomNumber = Random.nextInt(0, items.size)
                Log.d("randomNumber", randomNumber.toString())
                spinState.stoppingWheel(randomNumber)
            }) {
                Text(text = "SPIN")
            }
        }
        /*
        Button(onClick = {
            spinState.goto(0)
        }) {
            Text(text = "RESET")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = {
            spinState.launchInfinite()
        }) {
            Text(text = "Infinite")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = {
            spinState.stoppingWheel(pickerValue)
        }) {
            Text(text = "SPIN")
        }
        Spacer(modifier = Modifier.size(20.dp))

        NumberPicker(
            value = pickerValue,
            range = items.indices,
            onValueChange = {
                pickerValue = it
            }
        )
        */
    }
}

@Preview
@Composable
fun SpinWheelScreenPreview() {
    SpinWheelScreen()
}
