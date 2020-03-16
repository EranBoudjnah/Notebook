package com.mitteloupe.notebook.widget

import android.content.Context
import android.content.res.TypedArray
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
import com.mitteloupe.notebook.drawable.FrameDrawable.Margins
import com.mitteloupe.notebook.widget.style.applyAttributes
import com.mitteloupe.notebook.widget.style.getDimensionAttribute

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

    private val defaultBorderMargin = resources.getDimension(R.dimen.handDrawnEditTextBorderMargin)

    private var isInitialized = false

    var borderMarginTop = 0f
        set(value) {
            field = value
            if (isInitialized) {
                setBackground()
            }
        }
    var borderMarginBottom = 0f
        set(value) {
            field = value
            if (isInitialized) {
                setBackground()
            }
        }
    var borderMarginStart = 0f
        set(value) {
            field = value
            if (isInitialized) {
                setBackground()
            }
        }
    var borderMarginEnd = 0f
        set(value) {
            field = value
            if (isInitialized) {
                setBackground()
            }
        }

    init {
        attrs?.applyAttributes(context, R.styleable.HandDrawnEditText, defStyleAttr) { attributes ->
            attributes.applyStyledAttributes()
        }

        setBackground()
        setTextAppearance(
            context, R.style.HandDrawnEditText
        )

        isInitialized = true
    }

    private fun TypedArray.applyStyledAttributes() {
        borderMarginTop =
            getDimensionAttribute(R.styleable.HandDrawnEditText_borderMarginTop) { defaultBorderMargin }
        borderMarginBottom =
            getDimensionAttribute(R.styleable.HandDrawnEditText_borderMarginBottom) { defaultBorderMargin }
        borderMarginStart =
            getDimensionAttribute(R.styleable.HandDrawnEditText_borderMarginStart) { defaultBorderMargin }
        borderMarginEnd =
            getDimensionAttribute(R.styleable.HandDrawnEditText_borderMarginEnd) { defaultBorderMargin }
    }

    private fun setBackground() {
        ViewCompat.setBackground(
            this, FrameDrawable(
                painter,
                paint,
                Margins(borderMarginTop, borderMarginBottom, borderMarginStart, borderMarginEnd)
            )
        )
    }
}
