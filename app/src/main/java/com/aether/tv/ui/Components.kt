package com.aether.tv.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text

@Composable
fun AetherButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    GlassFocus(
        modifier,
        shape = RoundedCornerShape(24.dp),
        fillColor = Color.White.copy(alpha = 0.16f),
        onClick = onClick,
    ) {
        Text(
            label,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp, vertical = 12.dp),
        )
    }
}
