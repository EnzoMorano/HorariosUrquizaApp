package com.example.horariosurquiza.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Icono vectorial de flecha hacia atrás (ArrowBack)
private var cachedArrowBack: ImageVector? = null
val ArrowBackVector: ImageVector
    get() {
        if (cachedArrowBack != null) return cachedArrowBack!!
        cachedArrowBack = ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.0f, 11.0f)
                horizontalLineTo(7.83f)
                lineTo(13.42f, 5.41f)
                lineTo(12.0f, 4.0f)
                lineTo(4.0f, 12.0f)
                lineTo(12.0f, 20.0f)
                lineTo(13.41f, 18.59f)
                lineTo(7.83f, 13.0f)
                horizontalLineTo(20.0f)
                verticalLineTo(11.0f)
                close()
            }
        }.build()
        return cachedArrowBack!!
    }

// Icono vectorial de flecha hacia la derecha (ArrowForward)
private var cachedArrowForward: ImageVector? = null
val ArrowForwardVector: ImageVector
    get() {
        if (cachedArrowForward != null) return cachedArrowForward!!
        cachedArrowForward = ImageVector.Builder(
            name = "ArrowForward",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12.0f, 4.0f)
                lineTo(10.59f, 5.41f)
                lineTo(16.17f, 11.0f)
                horizontalLineTo(4.0f)
                verticalLineTo(13.0f)
                horizontalLineTo(16.17f)
                lineTo(10.59f, 18.59f)
                lineTo(12.0f, 20.0f)
                lineTo(20.0f, 12.0f)
                lineTo(12.0f, 4.0f)
                close()
            }
        }.build()
        return cachedArrowForward!!
    }

// Icono vectorial de calendario
private var cachedCalendarIcon: ImageVector? = null
val CalendarVector: ImageVector
    get() {
        if (cachedCalendarIcon != null) return cachedCalendarIcon!!
        cachedCalendarIcon = ImageVector.Builder(
            name = "CalendarIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(19.0f, 4.0f)
                horizontalLineTo(18.0f)
                verticalLineTo(2.0f)
                horizontalLineTo(16.0f)
                verticalLineTo(4.0f)
                horizontalLineTo(8.0f)
                verticalLineTo(2.0f)
                horizontalLineTo(6.0f)
                verticalLineTo(4.0f)
                horizontalLineTo(5.0f)
                lineTo(3.0f, 6.0f)
                lineTo(3.0f, 20.0f)
                lineTo(5.0f, 22.0f)
                horizontalLineTo(19.0f)
                lineTo(21.0f, 20.0f)
                verticalLineTo(6.0f)
                close()
                moveTo(19.0f, 20.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(10.0f)
                horizontalLineTo(19.0f)
                verticalLineTo(20.0f)
                close()
            }
        }.build()
        return cachedCalendarIcon!!
    }

// Icono vectorial de reloj
private var cachedTimeIcon: ImageVector? = null
val TimeVector: ImageVector
    get() {
        if (cachedTimeIcon != null) return cachedTimeIcon!!
        cachedTimeIcon = ImageVector.Builder(
            name = "TimeIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(11.99f, 2.0f)
                lineTo(2.0f, 12.0f)
                lineTo(11.99f, 22.0f)
                lineTo(22.0f, 12.0f)
                close()
                moveTo(12.0f, 20.0f)
                lineTo(4.0f, 12.0f)
                lineTo(12.0f, 4.0f)
                lineTo(20.0f, 12.0f)
                close()
                moveTo(12.5f, 7.0f)
                horizontalLineTo(11.0f)
                verticalLineTo(13.0f)
                lineTo(16.25f, 16.15f)
                lineTo(17.0f, 14.92f)
                lineTo(12.5f, 12.25f)
                verticalLineTo(7.0f)
                close()
            }
        }.build()
        return cachedTimeIcon!!
    }

// Icono de corazón lleno con curva Bézier suave (Favorito activo)
private var cachedHeartFilled: ImageVector? = null
val HeartFilledVector: ImageVector
    get() {
        if (cachedHeartFilled != null) return cachedHeartFilled!!
        cachedHeartFilled = ImageVector.Builder(
            name = "HeartFilled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12.0f, 21.35f)
                lineTo(10.55f, 20.03f)
                curveTo(5.4f, 15.36f, 2.0f, 12.28f, 2.0f, 8.5f)
                curveTo(2.0f, 5.42f, 4.42f, 3.0f, 7.5f, 3.0f)
                curveTo(9.24f, 3.0f, 10.91f, 3.81f, 12.0f, 5.09f)
                curveTo(13.09f, 3.81f, 14.76f, 3.0f, 16.5f, 3.0f)
                curveTo(19.58f, 3.0f, 22.0f, 5.42f, 22.0f, 8.5f)
                curveTo(22.0f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f)
                lineTo(12.0f, 21.35f)
                close()
            }
        }.build()
        return cachedHeartFilled!!
    }

// Icono de corazón borde estilizado (Favorito inactivo)
private var cachedHeartBorder: ImageVector? = null
val HeartBorderVector: ImageVector
    get() {
        if (cachedHeartBorder != null) return cachedHeartBorder!!
        cachedHeartBorder = ImageVector.Builder(
            name = "HeartBorder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(16.5f, 3.0f)
                curveTo(14.76f, 3.0f, 13.09f, 3.81f, 12.0f, 5.09f)
                curveTo(10.91f, 3.81f, 9.24f, 3.0f, 7.5f, 3.0f)
                curveTo(4.42f, 3.0f, 2.0f, 5.42f, 2.0f, 8.5f)
                curveTo(2.0f, 12.28f, 5.4f, 15.36f, 10.55f, 20.03f)
                lineTo(12.0f, 21.35f)
                lineTo(13.45f, 20.03f)
                curveTo(18.6f, 15.36f, 22.0f, 12.28f, 22.0f, 8.5f)
                curveTo(22.0f, 5.42f, 19.58f, 3.0f, 16.5f, 3.0f)
                close()
                moveTo(12.1f, 18.55f)
                lineTo(12.0f, 18.65f)
                lineTo(11.9f, 18.55f)
                curveTo(7.14f, 14.24f, 4.0f, 11.39f, 4.0f, 8.5f)
                curveTo(4.0f, 6.5f, 5.5f, 5.0f, 7.5f, 5.0f)
                curveTo(9.04f, 5.0f, 10.54f, 5.99f, 11.07f, 7.36f)
                horizontalLineTo(12.93f)
                curveTo(13.46f, 5.99f, 14.96f, 5.0f, 16.5f, 5.0f)
                curveTo(18.5f, 5.0f, 20.0f, 6.5f, 20.0f, 8.5f)
                curveTo(20.0f, 11.39f, 16.86f, 14.24f, 12.1f, 18.55f)
                close()
            }
        }.build()
        return cachedHeartBorder!!
    }

// Icono vectorial para intercambiar estación origen y destino (SwapVert)
private var cachedSwapVert: ImageVector? = null
val SwapVertVector: ImageVector
    get() {
        if (cachedSwapVert != null) return cachedSwapVert!!
        cachedSwapVert = ImageVector.Builder(
            name = "SwapVert",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(16.01f, 11.0f)
                horizontalLineTo(18.0f)
                lineTo(15.0f, 6.0f)
                lineTo(12.0f, 11.0f)
                horizontalLineTo(14.01f)
                verticalLineTo(18.0f)
                horizontalLineTo(16.01f)
                verticalLineTo(11.0f)
                close()
                moveTo(9.0f, 13.0f)
                horizontalLineTo(7.0f)
                verticalLineTo(6.0f)
                horizontalLineTo(5.0f)
                verticalLineTo(13.0f)
                horizontalLineTo(3.0f)
                lineTo(6.0f, 18.0f)
                lineTo(9.0f, 13.0f)
                close()
            }
        }.build()
        return cachedSwapVert!!
    }
