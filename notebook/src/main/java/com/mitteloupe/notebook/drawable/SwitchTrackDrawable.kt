package com.mitteloupe.notebook.drawable

import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.Painter

sealed class SwitchTrackDrawable(
    private val paint: Paint,
    resources: Resources,
    theme: Theme
) : Drawable() {
    @ColorInt
    var outlineColor = ResourcesCompat.getColor(resources, R.color.switchOutline, theme)
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
    var offColor = ResourcesCompat.getColor(resources, R.color.switchOffFill, theme)
        set(value) {
            field = value
            invalidateSelf()
        }

    @ColorInt
    var onColor = ResourcesCompat.getColor(resources, R.color.switchOnFill, theme)
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

    private val canvasClipBounds = Rect()

    override fun getIntrinsicWidth() =
        if (width > 0) width else canvasClipBounds.width()

    override fun getIntrinsicHeight() =
        if (height > 0) height else canvasClipBounds.width()

    class On(
        private val outlinePainter: Painter,
        private val paint: Paint,
        resources: Resources,
        theme: Theme
    ) : SwitchTrackDrawable(paint, resources, theme) {

        override fun draw(canvas: Canvas) {
            colorFilter = null
            drawOn(canvas, outlinePainter, paint)
        }
    }

    class Off(
        private val outlinePainter: Painter,
        private val paint: Paint,
        resources: Resources,
        theme: Theme
    ) : SwitchTrackDrawable(paint, resources, theme) {

        override fun draw(canvas: Canvas) {
            colorFilter = null
            drawOn(canvas, outlinePainter, paint)
        }
    }

    class Disabled(
        private val outlinePainter: Painter,
        private val paint: Paint,
        resources: Resources,
        theme: Theme
    ) : SwitchTrackDrawable(paint, resources, theme) {
        override fun draw(canvas: Canvas) {
            val grayScaleMatrix = ColorMatrix().apply { setSaturation(0f) }
            colorFilter = ColorMatrixColorFilter(grayScaleMatrix)

            drawPressed(canvas, outlinePainter, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    internal fun drawOn(
        canvas: Canvas,
        outlinePainter: Painter,
        paint: Paint
    ) {
        canvas.getClipBounds(canvasClipBounds)

        drawTrack(
            outlinePainter,
            canvas,
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.width().toFloat(),
            bounds.height().toFloat(),
            paint
        )
    }

    internal fun drawPressed(
        canvas: Canvas,
        outlinePainter: Painter,
        paint: Paint
    ) {
        canvas.getClipBounds(canvasClipBounds)

        drawTrack(
            outlinePainter,
            canvas,
            bounds.left.toFloat(), bounds.top.toFloat(),
            bounds.width().toFloat(), bounds.height().toFloat(),
            paint
        )
    }

    private fun drawTrack(
        outlinePainter: Painter,
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        paint: Paint
    ) {
        outlinePainter.drawCapsule(canvas, x, y, width, height, paint.outlineMode())
    }

    private fun Paint.outlineMode(): Paint {
        style = Paint.Style.STROKE
        color = outlineColor
        strokeWidth = outlineStrokeWidth
        xfermode = null
        return this
    }

    companion object {
        fun stateListDrawable(
            outlinePainter: Painter,
            paint: Paint,
            resources: Resources,
            theme: Theme,
            width: Int = 0,
            height: Int = 0
        ) = StateListDrawable().apply {
            addState(
                intArrayOf(-android.R.attr.state_checked),
                Off(outlinePainter, paint, resources, theme).withSize(width, height)
            )
            addState(
                intArrayOf(android.R.attr.state_checked),
                On(outlinePainter, paint, resources, theme).withSize(width, height)
            )
        }

        private fun SwitchTrackDrawable.withSize(width: Int, height: Int) = apply {
            this.width = width
            this.height = height
        }
    }
}
