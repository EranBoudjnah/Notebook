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

sealed class SwitchThumbDrawable(
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
        private val fillPainter: Painter,
        private val shadowPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float,
        private val layerTypeSet: Boolean = false,
        resources: Resources,
        theme: Theme
    ) : SwitchThumbDrawable(paint, resources, theme) {

        override fun draw(canvas: Canvas) {
            colorFilter = null
            drawEnabled(
                canvas,
                outlinePainter,
                fillPainter,
                shadowPainter,
                paint,
                borderMargin,
                true,
                layerTypeSet
            )
        }
    }

    class Off(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val shadowPainter: Painter,
        private val paint: Paint,
        private val borderMargin: Float,
        private val layerTypeSet: Boolean = false,
        resources: Resources,
        theme: Theme
    ) : SwitchThumbDrawable(paint, resources, theme) {

        override fun draw(canvas: Canvas) {
            colorFilter = null
            drawEnabled(
                canvas,
                outlinePainter,
                fillPainter,
                shadowPainter,
                paint,
                borderMargin,
                false,
                layerTypeSet
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
    ) : SwitchThumbDrawable(paint, resources, theme) {
        override fun draw(canvas: Canvas) {
            colorFilter = null
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
    ) : SwitchThumbDrawable(paint, resources, theme) {
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

    internal fun drawEnabled(
        canvas: Canvas,
        outlinePainter: Painter,
        fillPainter: Painter,
        shadowPainter: Painter,
        paint: Paint,
        borderMargin: Float,
        switchIsOn: Boolean,
        layerTypeSet: Boolean
    ) {
        canvas.getClipBounds(canvasClipBounds)

        val width = intrinsicWidth
        val height = intrinsicHeight
        val centerX = width / 2f + bounds.left
        val centerY = height / 2f + bounds.top
        val radius = min(width, height) / 2f - borderMargin

        drawThumbSide(
            layerTypeSet,
            canvas,
            shadowPainter,
            centerX,
            centerY,
            borderMargin,
            radius,
            paint,
            outlinePainter
        )

        drawThumb(outlinePainter, canvas, centerX, centerY, radius, paint, fillPainter, switchIsOn)
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
        val centerX = width / 2f + bounds.left
        val centerY = height / 2f + bounds.top + borderMargin
        val radius = min(width, height) / 2f - borderMargin

        drawThumb(outlinePainter, canvas, centerX, centerY, radius, paint, fillPainter, false)
    }

    private fun drawThumb(
        outlinePainter: Painter,
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: Paint,
        fillPainter: Painter,
        switchIsOn: Boolean
    ) {
        outlinePainter.drawCircle(
            canvas, centerX, centerY, radius, paint.outlineMode()
        )

        fillPainter.drawCircle(
            canvas, centerX, centerY, radius, paint.fillMode(switchIsOn)
        )
    }

    private fun drawThumbSide(
        layerTypeSet: Boolean,
        canvas: Canvas,
        shadowPainter: Painter,
        centerX: Float,
        centerY: Float,
        borderMargin: Float,
        radius: Float,
        paint: Paint,
        outlinePainter: Painter
    ) {
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
    }

    private fun Paint.fillMode(isOn: Boolean): Paint {
        style = Paint.Style.STROKE
        color = if (isOn) onColor else offColor
        strokeWidth = fillStrokeWidth
        xfermode = null
        return this
    }

    private fun Paint.clearMode(): Paint {
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        return this
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
                intArrayOf(android.R.attr.state_pressed),
                Pressed(
                    outlinePainter, fillPainter, paint, borderMargin, resources, theme
                ).withSize(width, height)
            )
            addState(
                intArrayOf(-android.R.attr.state_checked),
                Off(
                    outlinePainter,
                    fillPainter,
                    shadowPainter,
                    paint,
                    borderMargin,
                    layerTypeSet,
                    resources,
                    theme
                ).withSize(width, height)
            )
            addState(
                intArrayOf(android.R.attr.state_checked),
                On(
                    outlinePainter,
                    fillPainter,
                    shadowPainter,
                    paint,
                    borderMargin,
                    layerTypeSet,
                    resources,
                    theme
                ).withSize(width, height)
            )
        }

        private fun SwitchThumbDrawable.withSize(width: Int, height: Int) = apply {
            this.width = width
            this.height = height
        }
    }
}