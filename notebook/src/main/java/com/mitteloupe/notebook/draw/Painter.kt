package com.mitteloupe.notebook.draw

import android.graphics.Canvas
import android.graphics.Paint

interface Painter {
    fun drawCircle(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: Paint
    )

    fun drawRect(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        paint: Paint
    )

    fun drawLine(
        canvas: Canvas,
        x: Float,
        y: Float,
        horizontal: Float,
        vertical: Float,
        paint: Paint
    )

    class Fake : Painter {
        override fun drawCircle(
            canvas: Canvas,
            centerX: Float,
            centerY: Float,
            radius: Float,
            paint: Paint
        ) = Unit

        override fun drawRect(
            canvas: Canvas,
            x: Float,
            y: Float,
            width: Float,
            height: Float,
            paint: Paint
        ) = Unit

        override fun drawLine(
            canvas: Canvas,
            x: Float,
            y: Float,
            horizontal: Float,
            vertical: Float,
            paint: Paint
        ) = Unit
    }
}