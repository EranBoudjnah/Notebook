package com.mitteloupe.notebook.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import com.mitteloupe.notebook.draw.Painter

sealed class ButtonDrawable(
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
        private val borderMargin: Float
    ) : ButtonDrawable(paint) {

        override fun draw(canvas: Canvas) {
            canvas.getClipBounds(canvasClipBounds)

            val width = canvasClipBounds.width()
            val height = canvasClipBounds.height()

            shadowPainter.drawRect(
                canvas,
                borderMargin + canvasClipBounds.left,
                borderMargin + canvasClipBounds.top + height - borderMargin * 2f,
                width - borderMargin * 2f,
                borderMargin * 4f,
                paint.outlineMode()
            )

            fillPainter.drawRect(
                canvas,
                borderMargin + canvasClipBounds.left,
                borderMargin + canvasClipBounds.top,
                width - borderMargin * 2f,
                height - borderMargin * 2f,
                paint.fillMode()
            )

            outlinePainter.drawRect(
                canvas,
                borderMargin + canvasClipBounds.left,
                borderMargin + canvasClipBounds.top,
                width - borderMargin * 2f,
                height - borderMargin * 2f,
                paint.outlineMode()
            )
        }
    }

    class Pressed(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float
    ) : ButtonDrawable(paint) {
        override fun draw(canvas: Canvas) {
            drawPressed(canvas, outlinePainter, fillPainter, paint, borderMargin)
        }
    }

    class Disabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float
    ) : ButtonDrawable(paint) {
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

        fillPainter.drawRect(
            canvas,
            borderMargin + canvasClipBounds.left,
            borderMargin * 2f + canvasClipBounds.top,
            width - borderMargin * 2f,
            height - borderMargin * 2f,
            paint.fillMode()
        )

        outlinePainter.drawRect(
            canvas,
            borderMargin + canvasClipBounds.left,
            borderMargin * 2f + canvasClipBounds.top,
            width - borderMargin * 2f,
            height - borderMargin * 2f,
            paint.outlineMode()
        )
    }

    internal fun Paint.fillMode(): Paint {
        color = fillColor
        strokeWidth = fillStrokeWidth
        return this
    }

    internal fun Paint.outlineMode(): Paint {
        color = outlineColor
        strokeWidth = outlineStrokeWidth
        return this
    }

    companion object {
        fun stateListDrawable(
            outlinePainter: Painter,
            fillPainter: Painter,
            shadowPainter: Painter,
            paint: Paint,
            borderMargin: Float,
            layerTypeSet: Boolean
        ) = StateListDrawable().apply {
            addState(
                intArrayOf(-android.R.attr.state_enabled),
                CircleButtonDrawable.Disabled(
                    outlinePainter, fillPainter, paint, borderMargin
                )
            )
            addState(
                intArrayOf(android.R.attr.state_pressed),
                CircleButtonDrawable.Pressed(
                    outlinePainter, fillPainter, paint, borderMargin
                )
            )
            addState(
                intArrayOf(),
                CircleButtonDrawable.Enabled(
                    outlinePainter, fillPainter, shadowPainter, paint, borderMargin, layerTypeSet
                )
            )
        }
    }
}