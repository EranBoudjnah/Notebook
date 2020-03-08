package com.mitteloupe.notebook.draw

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.random.Random

class GeometryToolFiller(
    private val geometryTool: GeometryTool,
    private val filler: Filler = Filler.Horizontal,
    private val randomSeedProvider: () -> Int = { Random.nextInt() }
) : Painter {
    override fun drawCircle(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: Paint
    ) {
        val circlePath = geometryTool.circlePath(centerX, centerY, radius)
        canvas.save()
        canvas.clipPath(circlePath)
        val random = Random(randomSeedProvider())
        filler.draw(
            canvas,
            centerX - radius,
            centerY - radius,
            radius * 2f,
            radius * 2f,
            paint,
            random
        )
        canvas.restore()
    }

    override fun drawRect(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        paint: Paint
    ) {
        val rectanglePath = geometryTool.rectanglePath(x, y, width, height)
        canvas.save()
        canvas.clipPath(rectanglePath)
        val random = Random(randomSeedProvider())
        filler.draw(canvas, x, y, x + width, y + height, paint, random)
        canvas.restore()
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

sealed class Filler {
    object Horizontal : Filler() {
        override fun draw(
            canvas: Canvas,
            left: Float,
            top: Float,
            width: Float,
            height: Float,
            paint: Paint,
            random: Random
        ) = drawHorizontalFill(canvas, left, top, width, height, paint, random)
    }

    object Vertical : Filler() {
        override fun draw(
            canvas: Canvas,
            left: Float,
            top: Float,
            width: Float,
            height: Float,
            paint: Paint,
            random: Random
        ) = drawVerticalFill(canvas, left, top, width, height, paint, random)
    }

    object Both : Filler() {
        override fun draw(
            canvas: Canvas,
            left: Float,
            top: Float,
            width: Float,
            height: Float,
            paint: Paint,
            random: Random
        ) {
            drawHorizontalFill(canvas, left, top, width, height, paint, random)
            drawVerticalFill(canvas, left, top, width, height, paint, random)
        }
    }

    abstract fun draw(
        canvas: Canvas,
        left: Float,
        top: Float,
        width: Float,
        height: Float,
        paint: Paint,
        random: Random
    )

    internal fun drawHorizontalFill(
        canvas: Canvas,
        left: Float,
        top: Float,
        width: Float,
        height: Float,
        paint: Paint,
        random: Random
    ) {
        var currentY = top
        val right = left + width
        val maxYStep = paint.strokeWidth * .95f
        val minYStep = maxYStep * .25f
        val yStepMargin = maxYStep - minYStep
        while (currentY <= height) {
            val nextYRight = currentY + random.nextFloat() * yStepMargin + minYStep
            canvas.drawLine(left, currentY, right, nextYRight, paint)
            val nextYLeft = nextYRight + random.nextFloat() * yStepMargin + minYStep
            canvas.drawLine(right, nextYRight, left, nextYLeft, paint)
            currentY = nextYLeft
        }
    }

    internal fun drawVerticalFill(
        canvas: Canvas,
        left: Float,
        top: Float,
        width: Float,
        height: Float,
        paint: Paint,
        random: Random
    ) {
        var currentX = left
        val bottom = top + height
        val maxXStep = paint.strokeWidth * .95f
        val minXStep = maxXStep * .25f
        val xStepMargin = maxXStep - minXStep
        while (currentX <= width) {
            val nextXBottom = currentX + random.nextFloat() * xStepMargin + minXStep
            canvas.drawLine(currentX, top, nextXBottom, bottom, paint)
            val nextXTop = nextXBottom + random.nextFloat() * xStepMargin + minXStep
            canvas.drawLine(nextXBottom, bottom, nextXTop, top, paint)
            currentX = nextXTop
        }
    }
}