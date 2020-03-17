package com.mitteloupe.notebook.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import com.mitteloupe.notebook.draw.paper.Paper

class PaperDrawable(
    private val paper: Paper
) : Drawable() {
    override fun draw(canvas: Canvas) {
        paper.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        paper.alpha = alpha
    }

    override fun getOpacity() = paper.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}