package com.mitteloupe.notebook.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.mitteloupe.notebook.draw.Painter

class FrameDrawable(
    private val painter: Painter,
    private val paint: Paint,
    private val borderMargin: Float
) : Drawable() {
    private val canvasClipBounds = Rect()

    override fun draw(canvas: Canvas) {
        canvas.getClipBounds(canvasClipBounds)
        painter.drawRect(
            canvas,
            borderMargin + canvasClipBounds.left,
            borderMargin + canvasClipBounds.top,
            canvasClipBounds.width() - borderMargin * 2f,
            canvasClipBounds.height() - borderMargin * 2f,
            paint
        )

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}