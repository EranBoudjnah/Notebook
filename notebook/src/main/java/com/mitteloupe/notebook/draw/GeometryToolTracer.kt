package com.mitteloupe.notebook.draw

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class GeometryToolTracer(
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

    override fun drawCapsule(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        paint: Paint
    ) {
        val radius = height / 2f
        val straightWidth = width - height
        val centerXStart = radius + x
        val centerXEnd = centerXStart + straightWidth
        val centerY = radius + y
        val capsulePath = geometryTool.arcPath(centerXStart, centerY, radius, -180f, 0f).apply {
            geometryTool.linePath(straightWidth, 0f, this)
            geometryTool.arcPath(centerXEnd, centerY, radius, 0f, 180f, this)
            geometryTool.linePath(-straightWidth, 0f, this)
            close()
        }
        canvas.drawPath(capsulePath, paint)
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
        paint: Paint,
        path: Path
    ) {
        path.apply {
            if (isEmpty) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
        }
        geometryTool.linePath(horizontal, vertical, path)
        canvas.drawPath(path, paint)
    }
}