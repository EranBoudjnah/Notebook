package com.mitteloupe.notebook.draw.paper

import android.graphics.Canvas
import androidx.annotation.IntRange

interface Paper {
    @get:IntRange(from = 0L, to = 255L)
    @setparam:IntRange(from = 0L, to = 255L)
    var alpha: Int

    fun draw(canvas: Canvas)

    class Fake : Paper {
        override var alpha: Int = 255

        override fun draw(canvas: Canvas) = Unit
    }
}