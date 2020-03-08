package com.mitteloupe.notebook.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.Filler
import com.mitteloupe.notebook.draw.GeometryToolFiller
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter

class HandDrawnButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatButton(context, attrs, defStyleAttr) {
    private val randomSeed: Int
        get() = left + top

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.argb(200, 0, 0, 0)
        strokeWidth = 3f
    }

    private val geometryTool by lazy {
        HandDrawingGeometryTool { randomSeed }
    }

    private val painter: Painter = GeometryToolPainter(geometryTool)
    private val filler: Painter = GeometryToolFiller(geometryTool, Filler.Both) { randomSeed }
    private val shadowFiller: Painter =
        GeometryToolFiller(geometryTool, Filler.Horizontal) { randomSeed }

    private val canvasClipBounds = Rect()
    private val borderMargin by lazy {
        resources.getDimension(R.dimen.hand_drawn_edit_text_border_margin)
    }

    init {
        background = null
        setTextAppearance(context,
            R.style.HandDrawnEditText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.getClipBounds(canvasClipBounds)

        filler.drawRect(
            canvas,
            borderMargin + canvasClipBounds.left,
            borderMargin + canvasClipBounds.top,
            width - borderMargin * 2f,
            height - borderMargin * 2f,
            paint.fillMode()
        )

        painter.drawRect(
            canvas,
            borderMargin + canvasClipBounds.left,
            borderMargin + canvasClipBounds.top,
            width - borderMargin * 2f,
            height - borderMargin * 2f,
            paint.outlineMode()
        )

        shadowFiller.drawRect(
            canvas,
            borderMargin + canvasClipBounds.left,
            borderMargin + canvasClipBounds.top + height - borderMargin * 2f,
            width - borderMargin * 2f,
            borderMargin * 4f,
            paint.outlineMode()
        )
    }

    private fun Paint.fillMode(): Paint {
        color = Color.argb(64, 0, 180, 220)
        strokeWidth = 20f
        return this
    }

    private fun Paint.outlineMode(): Paint {
        color = Color.argb(200, 0, 0, 0)
        strokeWidth = 3f
        return this
    }
}
