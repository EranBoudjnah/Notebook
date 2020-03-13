package com.mitteloupe.notebook.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.GridPaper
import com.mitteloupe.notebook.draw.Paper

class NotebookPageView : View {
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    private val backgroundPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.notebook_page_background, context.theme)
        style = Paint.Style.FILL
    }

    private val paperPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.notebook_page_foreground, context.theme)
        style = Paint.Style.STROKE
    }

    private val paper: Paper =
        GridPaper(resources.getDimensionPixelSize(R.dimen.gridSize), backgroundPaint, paperPaint)

    override fun onDraw(canvas: Canvas) {
        paper.draw(canvas)
    }
}