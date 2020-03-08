package com.mitteloupe.notebook.draw

import android.graphics.Canvas
import android.graphics.Paint

class GridPaper(
    private val size: Int,
    private val paint: Paint
) : Paper {
    override fun draw(canvas: Canvas) {
        val canvasHeight = canvas.height.toFloat()
        for (x in size - 1 until canvas.width step size) {
            val xOnCanvas = x.toFloat()
            canvas.drawLine(xOnCanvas, 0f, xOnCanvas, canvasHeight, paint)
        }

        val canvasWidth = canvas.height.toFloat()
        for (y in size - 1 until canvas.height step size) {
            val yOnCanvas = y.toFloat()
            canvas.drawLine(0f, yOnCanvas, canvasWidth, yOnCanvas, paint)
        }
    }
}