package com.mitteloupe.notebook.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.paper.GridPaper
import com.mitteloupe.notebook.draw.paper.Paper
import com.mitteloupe.notebook.drawable.PaperDrawable

class NotebookPageView : View {
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    private val backgroundPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.notebookPageBackground, context.theme)
        style = Paint.Style.FILL
    }

    private val paperPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.notebookPageForeground, context.theme)
        style = Paint.Style.STROKE
    }

    var paper: Paper =
        GridPaper(resources.getDimensionPixelSize(R.dimen.gridSize), backgroundPaint, paperPaint)
        set(value) {
            field = value
            ViewCompat.setBackground(this, PaperDrawable(paper))
        }

    init {
        ViewCompat.setBackground(this, PaperDrawable(paper))
    }
}