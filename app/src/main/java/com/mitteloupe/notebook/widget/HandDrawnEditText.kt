package com.mitteloupe.notebook.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.mitteloupe.notebook.drawable.FrameDrawable
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter

class HandDrawnEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 3f
    }

    private val painter: Painter = GeometryToolPainter(HandDrawingGeometryTool())

    private val borderMargin = resources.getDimension(R.dimen.hand_drawn_edit_text_border_margin)

    init {
        background = FrameDrawable(
            painter,
            paint,
            borderMargin
        ).apply {
            alpha = 200
        }
        setTextAppearance(context,
            R.style.HandDrawnEditText
        )
    }
}
