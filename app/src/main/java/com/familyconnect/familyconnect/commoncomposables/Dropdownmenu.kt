package com.familyconnect.familyconnect.commoncomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.familyconnect.familyconnect.R

@Composable
fun DropDownFun(
    userList: List<String>,
    selectedUser: String,
    onValueChange: (String) -> Unit
) {
    var dropControl by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(userList.indexOf(selectedUser)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dropControl = true }
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (selectedIndex >= 0 && selectedIndex < userList.size) userList[selectedIndex] else "Select User",
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "Drop down icon"
                )
            }

            DropdownMenu(expanded = dropControl, onDismissRequest = { dropControl = false }) {
                userList.forEachIndexed { index, user ->
                    DropdownMenuItem(
                        text = { Text(text = user) },
                        onClick = {
                            dropControl = false
                            selectedIndex = index
                            onValueChange(userList[selectedIndex])
                        }
                    )
                }
            }
        }
    }
}
