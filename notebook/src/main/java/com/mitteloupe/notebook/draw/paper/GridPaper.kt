package com.mitteloupe.notebook.draw.paper

import android.graphics.Canvas
import android.graphics.Paint

class GridPaper(
    private val sizePixels: Int,
    private val backgroundPaint: Paint,
    private val drawPaint: Paint
) : Paper {
    override var alpha: Int = 255

    override fun draw(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        canvas.saveLayerAlpha(
            0f, 0f, canvasWidth, canvasHeight, alpha, Canvas.ALL_SAVE_FLAG
        )
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
}