package com.rickyputrah.pawquiz.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Phone / Portrait - Light Mode")
@Preview(name = "Phone / Portrait - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Small Phone / Portrait", widthDp = 480, heightDp = 640)
@Preview(name = "Tablet / Portrait", widthDp = 800, heightDp = 1280, device = Devices.PIXEL_C)
@Preview(name = "Tablet / Landscape", widthDp = 1280, heightDp = 800, device = Devices.PIXEL_C)
annotation class ReferencePreviewDevicesLightDarkMode