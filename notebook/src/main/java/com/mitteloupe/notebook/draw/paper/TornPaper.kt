package com.mitteloupe.notebook.draw.paper

import android.content.res.Resources
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.core.content.res.ResourcesCompat
import com.mitteloupe.notebook.R
import kotlin.random.Random

class TornPaper(
    private val paper: Paper,
    private val topMarginPixels: Float = 16f,
    private val maxJitterPixels: Float = 8f,
    private val tearStrokeWidth: Float = 8f,
    resources: Resources,
    theme: Resources.Theme,
    private val random: Random = Random.Default
) : Paper {
    override var alpha: Int
        get() = paper.alpha
        set(value) {
            paper.alpha = value
        }

    private val offCanvasMargin = tearStrokeWidth / 2f - 1f

    private val tearPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = tearStrokeWidth
        color = ResourcesCompat.getColor(resources, R.color.tornPaper, theme)
        isAntiAlias = true
    }

    private val shadowPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = tearStrokeWidth
        color = Color.argb(50, 0, 0, 0)
        isAntiAlias = true
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.OUTER)
    }

    private val tearPath = Path()
    private val tearBounds = RectF()

    private var lastCanvasSize = CanvasSize(0, 0)

    override fun draw(canvas: Canvas) {
        val canvasSize = CanvasSize(canvas.width, canvas.height)
        if (canvasSize != lastCanvasSize) {
            updatePath(canvasSize.width, canvasSize.height)
            lastCanvasSize = canvasSize
        }

        canvas.drawPath(tearPath, shadowPaint)
        canvas.save()
        canvas.clipPath(tearPath)
        paper.draw(canvas)
        canvas.drawPath(tearPath, tearPaint)
        canvas.restore()
    }

    private fun updatePath(width: Int, height: Int) {
        tearPath.reset()
        tearPath.set(tearPath(width, topMarginPixels))
        tearPath.lineTo(width.toFloat() + offCanvasMargin, height.toFloat() + offCanvasMargin)
        tearPath.lineTo(-offCanvasMargin, height.toFloat() + offCanvasMargin)
        tearPath.close()
    }

    private fun tearPath(end: Int, margin: Float): Path {
        val pointsCount = (end + 1)
        val points = FloatArray(pointsCount)

        val initialTearPosition = margin - maxJitterPixels / 2f
        points[0] = initialTearPosition
        points[pointsCount - 1] = initialTearPosition
        tearAndDivide(points, 0, pointsCount - 1, maxJitterPixels)
        val path = Path()
        points.forEachIndexed { index, pointValue ->
            path.lineTo(index.toFloat(), pointValue)
        }
        return path
    }

    private fun tearAndDivide(
        points: FloatArray,
        left: Int,
        right: Int,
        jitterRange: Float
    ) {
        if (right - left <= 1) return

        val leftJitter = points[left]
        val rightJitter = points[right]
        val jitter = leftJitter - jitterRange / 2f +
                random.nextFloat() * (rightJitter - leftJitter + jitterRange)

        val middle = (right + left) / 2
        points[middle] = jitter
        tearAndDivide(points, left, middle, jitterRange / 1.5f)
        tearAndDivide(points, middle, right, jitterRange / 1.5f)
    }

    private data class CanvasSize(
        val width: Int,
        val height: Int
    )
}