package com.example.mallar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mallar.data.AStarDirection
import com.example.mallar.data.AStarPath

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutePreviewBottomSheet(
    destination: String,
    path: AStarPath,
    pxPerMetre: Float = 4.48f,
    onStartNavigation: () -> Unit,
    onCancel: () -> Unit
) {
    val distanceM = (path.totalDistancePx / pxPerMetre).toInt()
    val estimatedMinutes = (distanceM / 80f).coerceAtLeast(1f).toInt()

    ModalBottomSheet(
        onDismissRequest = onCancel,
        containerColor = Color(0xFF121829)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header
            Text(
                text = "Route to $destination",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatChip(
                    icon = Icons.Default.Straighten,
                    value = "${distanceM}m",
                    label = "Distance"
                )
                StatChip(
                    icon = Icons.Default.Timer,
                    value = "$estimatedMinutes min",
                    label = "Time"
                )
                StatChip(
                    icon = Icons.Default.ArrowForward,
                    value = "${path.steps.size}",
                    label = "Turns"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Turn-by-turn preview
            Text(
                text = "Turn-by-Turn Directions",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 250.dp)
            ) {
                items(path.steps) { step ->
                    DirectionRow(step, pxPerMetre)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Start Button
            Button(
                onClick = onStartNavigation,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E64FF)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Navigation,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Navigation", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = Color.White.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatChip(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFF1E1E2E), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1E64FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun DirectionRow(step: com.example.mallar.data.NavInstruction, pxPerMetre: Float) {
    val distM = (step.distancePx / pxPerMetre).toInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Direction icon
        Icon(
            imageVector = when (step.direction) {
                AStarDirection.LEFT -> Icons.Default.ArrowBack
                AStarDirection.RIGHT -> Icons.Default.ArrowForward
                AStarDirection.STRAIGHT -> Icons.Default.ArrowUpward
                AStarDirection.ARRIVED -> Icons.Default.Flag
            },
            contentDescription = null,
            tint = Color(0xFF00BCD4),
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Direction text
        Column {
            Text(
                text = when (step.direction) {
                    AStarDirection.LEFT -> "Turn left"
                    AStarDirection.RIGHT -> "Turn right"
                    AStarDirection.STRAIGHT -> "Continue straight"
                    AStarDirection.ARRIVED -> "Destination"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            if (step.direction != AStarDirection.ARRIVED) {
                Text(
                    text = "for ${distM} metres",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}
