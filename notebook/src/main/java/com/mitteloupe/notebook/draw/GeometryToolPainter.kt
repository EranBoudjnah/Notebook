package com.mitteloupe.notebook.draw

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class GeometryToolPainter(
    private val geometryTool: GeometryTool
) : Painter {
    override fun drawCircle(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: Paint
    ) {
        val path = geometryTool.circlePath(centerX, centerY, radius)
        canvas.drawPath(path, paint)
    }

    override fun drawRect(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        paint: Paint
    ) {
        val path = geometryTool.rectanglePath(x, y, width, height)
        canvas.drawPath(path, paint)
    }

    override fun drawLine(
        canvas: Canvas,
        x: Float,
        y: Float,
        horizontal: Float,
        vertical: Float,
        paint: Paint
    ) {
        val path = Path().apply {
            moveTo(x, y)
        }
        geometryTool.linePath(horizontal, vertical, path)
        canvas.drawPath(path, paint)
    }
}