package com.mitteloupe.notebook.drawable

import android.content.res.Resources
import android.content.res.Resources.Theme
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
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.Painter
import kotlin.math.min

sealed class CircleButtonDrawable(
    private val paint: Paint,
    resources: Resources,
    theme: Theme
) : Drawable() {
    @ColorInt
    var outlineColor = ResourcesCompat.getColor(resources, R.color.buttonOutline, theme)
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
    var fillColor = ResourcesCompat.getColor(resources, R.color.buttonFill, theme)
        set(value) {
            field = value
            invalidateSelf()
        }
    var fillStrokeWidth = 20f
        set(value) {
            field = value
            invalidateSelf()
        }

    var width: Int = 0
    var height: Int = 0

    override fun getIntrinsicWidth() =
        if (width > 0) width else canvasClipBounds.width()

    override fun getIntrinsicHeight() =
        if (height > 0) height else canvasClipBounds.width()

    internal val canvasClipBounds = Rect()

    class Enabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val shadowPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float,
        private val layerTypeSet: Boolean = false,
        resources: Resources,
        theme: Theme
    ) : CircleButtonDrawable(paint, resources, theme) {

        override fun draw(canvas: Canvas) {
            canvas.getClipBounds(canvasClipBounds)

            val width = intrinsicWidth
            val height = intrinsicHeight
            val centerX = width / 2f
            val centerY = height / 2f
            val radius = min(width, height) / 2f - borderMargin

            if (!layerTypeSet) {
                canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
            }
            shadowPainter.drawCircle(
                canvas, centerX, centerY + borderMargin, radius, paint.outlineMode()
            )

            outlinePainter.drawCircle(
                canvas, centerX, centerY, radius, paint.clearMode()
            )

            if (!layerTypeSet) {
                canvas.restore()
            }

            outlinePainter.drawCircle(
                canvas, centerX, centerY, radius, paint.outlineMode()
            )

            fillPainter.drawCircle(
                canvas, centerX, centerY, radius, paint.fillMode()
            )
        }
    }

    class Pressed(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float,
        resources: Resources,
        theme: Theme
    ) : CircleButtonDrawable(paint, resources, theme) {
        override fun draw(canvas: Canvas) {
            drawPressed(canvas, outlinePainter, fillPainter, paint, borderMargin)
        }
    }

    class Disabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float,
        resources: Resources,
        theme: Theme
    ) : CircleButtonDrawable(paint, resources, theme) {
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

        val width = intrinsicWidth
        val height = intrinsicHeight
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) / 2f - borderMargin

        outlinePainter.drawCircle(
            canvas, centerX, centerY + borderMargin, radius, paint.outlineMode()
        )

        fillPainter.drawCircle(
            canvas, centerX, centerY + borderMargin, radius, paint.fillMode()
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

    companion object {
        fun stateListDrawable(
            outlinePainter: Painter,
            fillPainter: Painter,
            shadowPainter: Painter,
            paint: Paint,
            borderMargin: Float,
            layerTypeSet: Boolean,
            resources: Resources,
            theme: Theme,
            width: Int = 0,
            height: Int = 0
        ) = StateListDrawable().apply {
            addState(
                intArrayOf(-android.R.attr.state_enabled),
                Disabled(
                    outlinePainter, fillPainter, paint, borderMargin, resources, theme
                ).apply {
                    this.width = width
                    this.height = height
                }
            )
            addState(
                intArrayOf(android.R.attr.state_pressed),
                Pressed(
                    outlinePainter, fillPainter, paint, borderMargin, resources, theme
                ).apply {
                    this.width = width
                    this.height = height
                }
            )
            addState(
                intArrayOf(),
                Enabled(
                    outlinePainter,
                    fillPainter,
                    shadowPainter,
                    paint,
                    borderMargin,
                    layerTypeSet,
                    resources,
                    theme
                ).apply {
                    this.width = width
                    this.height = height
                }
            )
        }
    }
}