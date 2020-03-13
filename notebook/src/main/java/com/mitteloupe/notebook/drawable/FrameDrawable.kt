package com.mitteloupe.notebook.drawable

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.mitteloupe.notebook.draw.Painter

class FrameDrawable(
    private val painter: Painter,
    private val paint: Paint,
    private val borderMargin: Float
) : Drawable() {
    private val canvasClipBounds = Rect()
    private var tintBlendMode: PorterDuff.Mode? = PorterDuff.Mode.SRC_IN

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

    override fun setTintMode(tintMode: PorterDuff.Mode?) {
        tintBlendMode = tintMode
        super.setTintMode(tintBlendMode)
    }

    override fun setTintList(tint: ColorStateList?) {
        tintBlendMode?.let { tintBlendMode ->
            tint?.let {
                colorFilter = PorterDuffColorFilter(tint.defaultColor, tintBlendMode)
            }
        }
    }
}