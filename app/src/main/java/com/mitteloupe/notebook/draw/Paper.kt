package com.mitteloupe.notebook.draw

import android.graphics.Canvas

interface Paper {
    fun draw(canvas: Canvas)

    class Fake : Paper {
        override fun draw(canvas: Canvas) = Unit
    }
}