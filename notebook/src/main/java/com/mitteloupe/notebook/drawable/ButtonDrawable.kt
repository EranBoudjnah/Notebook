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

sealed class ButtonDrawable(
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

    internal val canvasClipBounds = Rect()

    class Enabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val shadowPainter: Painter,
        private val paint: Paint,
        private val borderMargins: Margins,
        private val elevationHeight: Float,
        resources: Resources,
        theme: Theme
    ) : ButtonDrawable(paint, resources, theme) {

        override fun draw(canvas: Canvas) {
            canvas.getClipBounds(canvasClipBounds)

            val buttonWidth =
                canvasClipBounds.width() - borderMargins.marginStart - borderMargins.marginEnd
            val buttonHeight =
                canvasClipBounds.height() - borderMargins.marginTop - borderMargins.marginBottom

            shadowPainter.drawRect(
                canvas,
                borderMargins.marginStart + canvasClipBounds.left,
                borderMargins.marginTop + buttonHeight,
                buttonWidth,
                elevationHeight,
                paint.outlineMode()
            )

            fillPainter.drawRect(
                canvas,
                borderMargins.marginStart + canvasClipBounds.left,
                borderMargins.marginTop + canvasClipBounds.top,
                buttonWidth,
                buttonHeight,
                paint.fillMode()
            )

            outlinePainter.drawRect(
                canvas,
                borderMargins.marginStart + canvasClipBounds.left,
                borderMargins.marginTop + canvasClipBounds.top,
                buttonWidth,
                buttonHeight,
                paint.outlineMode()
            )
        }
    }

    class Pressed(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargins: Margins,
        private val elevationHeight: Float,
        resources: Resources,
        theme: Theme
    ) : ButtonDrawable(paint, resources, theme) {
        override fun draw(canvas: Canvas) {
            drawPressed(canvas, outlinePainter, fillPainter, paint, borderMargins, elevationHeight)
        }
    }

    class Disabled(
        private val outlinePainter: Painter,
        private val fillPainter: Painter,
        private val paint: Paint,
        private val borderMargins: Margins,
        private val elevationHeight: Float,
        resources: Resources,
        theme: Theme
    ) : ButtonDrawable(paint, resources, theme) {
        override fun draw(canvas: Canvas) {
            val grayScaleMatrix = ColorMatrix().apply { setSaturation(0f) }
            colorFilter = ColorMatrixColorFilter(grayScaleMatrix)

            drawPressed(canvas, outlinePainter, fillPainter, paint, borderMargins, elevationHeight)
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
        borderMargins: Margins,
        elevationHeight: Float
    ) {
        canvas.getClipBounds(canvasClipBounds)

        val buttonWidth =
            canvasClipBounds.width() - borderMargins.marginStart - borderMargins.marginEnd
        val buttonHeight =
            canvasClipBounds.height() - borderMargins.marginTop - borderMargins.marginBottom

        fillPainter.drawRect(
            canvas,
            borderMargins.marginStart + canvasClipBounds.left,
            borderMargins.marginTop + elevationHeight + canvasClipBounds.top,
            buttonWidth,
            buttonHeight,
            paint.fillMode()
        )

        outlinePainter.drawRect(
            canvas,
            borderMargins.marginStart + canvasClipBounds.left,
            borderMargins.marginTop + elevationHeight + canvasClipBounds.top,
            buttonWidth,
            buttonHeight,
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

    data class Margins(
        val marginTop: Float,
        val marginBottom: Float,
        val marginStart: Float,
        val marginEnd: Float
    )
}