package com.rickyputrah.pawquiz.ui.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rickyputrah.pawquiz.R
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import com.rickyputrah.pawquiz.util.ReferencePreviewDevicesLightDarkMode


@Preview(showBackground = true)
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        InfiniteProgress(tint = MaterialTheme.colorScheme.primary, size = 256.dp)
        content?.invoke(this)
    }
}

@Composable
fun InfiniteProgress(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color = MaterialTheme.colorScheme.primary,
    duration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Icon(
        painter = painterResource(id = R.drawable.loader_icon),
        contentDescription = null,
        modifier = modifier.then(
            Modifier
                .rotate(rotation)
                .size(size)

        ),
        tint = tint
    )
}

@ReferencePreviewDevicesLightDarkMode
@Composable
private fun PreviewLoadingScreen() {
    PawQuizTheme {
        LoadingScreen()
    }
}