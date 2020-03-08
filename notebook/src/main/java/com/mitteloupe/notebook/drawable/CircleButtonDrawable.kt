package com.mitteloupe.notebook.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.mitteloupe.notebook.draw.Painter
import kotlin.math.min

sealed class CircleButtonDrawable(
    private val paint: Paint
) : Drawable() {
    @ColorInt
    var outlineColor = Color.argb(200, 0, 0, 0)
        set(value) {
            field = value
            invalidateSelf()
        }
    var outlineStrokeWidth = 3f
        set(value) {
            field = value
            invalidateSelf()
        }

    @ColorInt
    var fillColor = Color.argb(64, 0, 180, 220)
        set(value) {
            field = value
            invalidateSelf()
        }
    var fillStrokeWidth = 20f
        set(value) {
            field = value
            invalidateSelf()
        }

    internal val canvasClipBounds = Rect()

    class Enabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val shadowPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float,
        private val layerTypeSet: Boolean = false
    ) : CircleButtonDrawable(paint) {

        override fun draw(canvas: Canvas) {
            canvas.getClipBounds(canvasClipBounds)

            val width = canvasClipBounds.width()
            val height = canvasClipBounds.height()
            val centerX = width / 2f
            val centerY = height / 2f
            val radius = min(width, height) / 2f - borderMargin

            if (!layerTypeSet) {
                canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
            }
            shadowPainter.drawCircle(
                canvas,
                centerX,
                centerY + borderMargin,
                radius,
                paint.outlineMode()
            )

            outlinePainter.drawCircle(
                canvas,
                centerX,
                centerY,
                radius,
                paint.clearMode()
            )

            if (!layerTypeSet) {
                canvas.restore()
            }

            outlinePainter.drawCircle(
                canvas,
                centerX,
                centerY,
                radius,
                paint.outlineMode()
            )

            fillPainter.drawCircle(
                canvas,
                centerX,
                centerY,
                radius,
                paint.fillMode()
            )
        }
    }

    class Pressed(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float
    ) : CircleButtonDrawable(paint) {
        override fun draw(canvas: Canvas) {
            drawPressed(canvas, outlinePainter, fillPainter, paint, borderMargin)
        }
    }

    class Disabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float
    ) : CircleButtonDrawable(paint) {
        override fun draw(canvas: Canvas) {
            val grayScaleMatrix = ColorMatrix().apply { setSaturation(0f) }
            colorFilter = ColorMatrixColorFilter(grayScaleMatrix)

            drawPressed(canvas, outlinePainter, fillPainter, paint, borderMargin)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    internal fun drawPressed(
        canvas: Canvas,
        outlinePainter: Painter,
        fillPainter: Painter,
        paint: Paint,
        borderMargin: Float
    ) {
        canvas.getClipBounds(canvasClipBounds)

        val width = canvasClipBounds.width()
        val height = canvasClipBounds.height()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) / 2f - borderMargin

        outlinePainter.drawCircle(
            canvas,
            centerX,
            centerY + borderMargin,
            radius,
            paint.outlineMode()
        )

        fillPainter.drawCircle(
            canvas,
            centerX,
            centerY + borderMargin,
            radius,
            paint.fillMode()
        )
    }

    internal fun Paint.fillMode(): Paint {
        style = Paint.Style.STROKE
        color = fillColor
        strokeWidth = fillStrokeWidth
        xfermode = null
        return this
    }

    internal fun Paint.clearMode(): Paint {
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        return this
    }

    internal fun Paint.outlineMode(): Paint {
        style = Paint.Style.STROKE
        color = outlineColor
        strokeWidth = outlineStrokeWidth
        xfermode = null
        return this
    }
}