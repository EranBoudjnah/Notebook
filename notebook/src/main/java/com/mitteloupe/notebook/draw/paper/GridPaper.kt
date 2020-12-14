package com.mitteloupe.notebook.draw.paper

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build

class GridPaper(
    private val sizePixels: Int,
    private val backgroundPaint: Paint,
    private val drawPaint: Paint
) : Paper {
    override var alpha: Int = 255

    override fun draw(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        canvas.saveLayerAlphaCompat(0f, 0f, canvasWidth, canvasHeight, alpha)
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backgroundPaint)

        for (x in sizePixels - 1 until canvas.width step sizePixels) {
            val xOnCanvas = x.toFloat()
            canvas.drawLine(xOnCanvas, 0f, xOnCanvas, canvasHeight, drawPaint)
        }

        for (y in sizePixels - 1 until canvas.height step sizePixels) {
            val yOnCanvas = y.toFloat()
            canvas.drawLine(0f, yOnCanvas, canvasWidth, yOnCanvas, drawPaint)
        }
        canvas.restore()
    }

    private fun Canvas.saveLayerAlphaCompat(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        alpha: Int
    ) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        saveLayerAlpha(left, top, right, bottom, alpha)
    } else {
        @Suppress("DEPRECATION")
        saveLayerAlpha(left, top, right, bottom, alpha, Canvas.ALL_SAVE_FLAG)
    }
}
