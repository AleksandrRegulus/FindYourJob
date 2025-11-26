package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )

    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val YsDisplayRegularFont = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal)
)

val YsDisplayMediumFont = FontFamily(
    Font(R.font.ys_display_medium, FontWeight.Normal)
)

val Typography.mediumBold32: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = YsDisplayMediumFont,
            fontWeight = FontWeight.Bold,
            lineHeight = 38.sp,
            fontSize = 32.sp
        )
    }

val Typography.medium22: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = YsDisplayMediumFont,
            lineHeight = 26.sp,
            fontSize = 22.sp
        )
    }

val Typography.medium16: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = YsDisplayMediumFont,
            lineHeight = 19.sp,
            fontSize = 16.sp
        )
    }

val Typography.regular16: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = YsDisplayRegularFont,
            lineHeight = 19.sp,
            fontSize = 16.sp
        )
    }

val Typography.regular12: TextStyle
    @Composable
    get() {
        return TextStyle(
            fontFamily = YsDisplayRegularFont,
            lineHeight = 16.sp,
            fontSize = 12.sp
        )
    }
