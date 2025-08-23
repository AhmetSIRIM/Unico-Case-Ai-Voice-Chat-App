package com.ahmetsirim.designsystem.utility

import androidx.compose.ui.tooling.preview.Preview

/**
 * [ResponsivenessCheckerPreview] annotation is designed to preview a Composable function
 * across selected device screen sizes to ensure responsive UI design.
 *
 * This annotation includes:
 *
 * - **Tablet - Small**: 600dp x 960dp, high DPI (320).
 * - **Tablet - Designer Perspective**: 744dp x 1133dp, high DPI (320).
 * - **Tablet - Medium**: 800dp x 1280dp, high DPI (320).
 * - **Tablet - Large**: 1000dp x 1600dp, high DPI (320).
 *
 * Use this annotation to check how your Composables behave on different devices,
 * ensuring adaptability and responsiveness across a range of screen sizes.
 */
@Preview(name = "Tablet - Designer Perspective", device = "spec:width=744dp,height=1133dp,dpi=320", showBackground = true, apiLevel = 35)
@Preview(name = "Tablet - Small", device = "spec:width=600dp,height=960dp,dpi=320", showBackground = true, apiLevel = 36)
@Preview(name = "Tablet - Medium", device = "spec:width=800dp,height=1280dp,dpi=320", showBackground = true, apiLevel = 35)
@Preview(name = "Tablet - Large", device = "spec:width=1000dp,height=1600dp,dpi=320", showBackground = true, apiLevel = 36)
annotation class ResponsivenessCheckerPreview