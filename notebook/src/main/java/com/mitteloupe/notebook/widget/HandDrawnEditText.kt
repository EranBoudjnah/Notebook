package com.mitteloupe.notebook.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter
import com.mitteloupe.notebook.drawable.FrameDrawable

class HandDrawnEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = ResourcesCompat.getColor(resources, R.color.editTextFrame, context.theme)
        strokeWidth = 3f
    }

    private val painter: Painter = GeometryToolPainter(HandDrawingGeometryTool {
        left + top
    })

    private val borderMargin = resources.getDimension(R.dimen.handDrawnEditTextBorderMargin)

    init {
        ViewCompat.setBackground(this, FrameDrawable(
            painter, paint, borderMargin
        ))
        setTextAppearance(
            context, R.style.HandDrawnEditText
        )
    }
}
