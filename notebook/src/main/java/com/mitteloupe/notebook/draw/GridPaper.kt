package com.mitteloupe.notebook.draw

import android.graphics.Canvas
import android.graphics.Paint

class GridPaper(
    private val size: Int,
    private val backgroundPaint: Paint,
    private val drawPaint: Paint
) : Paper {
    override fun draw(canvas: Canvas) {
        val canvasWidth = canvas.width.toFloat()
        val canvasHeight = canvas.height.toFloat()
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backgroundPaint)

        for (x in size - 1 until canvas.width step size) {
            val xOnCanvas = x.toFloat()
            canvas.drawLine(xOnCanvas, 0f, xOnCanvas, canvasHeight, drawPaint)
        }

        for (y in size - 1 until canvas.height step size) {
            val yOnCanvas = y.toFloat()
            canvas.drawLine(0f, yOnCanvas, canvasWidth, yOnCanvas, drawPaint)
        }
    }
}