package com.mitteloupe.notebook.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.GridPaper
import com.mitteloupe.notebook.draw.Paper

class NotebookView : View {
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    private val paperPaint = Paint().apply {
        color = Color.argb(30, 0, 0, 128)
        style = Paint.Style.STROKE
    }

    private val paper: Paper =
        GridPaper(resources.getDimensionPixelSize(R.dimen.gridSize), paperPaint)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paper.draw(canvas)
    }
}