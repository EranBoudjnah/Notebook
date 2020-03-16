package com.mitteloupe.notebook.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.Filler
import com.mitteloupe.notebook.draw.GeometryToolFiller
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter
import com.mitteloupe.notebook.drawable.ButtonDrawable
import com.mitteloupe.notebook.drawable.ButtonDrawable.Margins
import com.mitteloupe.notebook.widget.style.applyAttributes
import com.mitteloupe.notebook.widget.style.getDimensionAttribute

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
    }

    private val geometryTool by lazy {
        HandDrawingGeometryTool { randomSeed }
    }

    private val outlinePainter: Painter = GeometryToolPainter(geometryTool)
    private val fillPainter: Painter = GeometryToolFiller(geometryTool, Filler.Both) { randomSeed }
    private val shadowPainter: Painter =
        GeometryToolFiller(geometryTool, Filler.Horizontal) { randomSeed }

    private val defaultBorderMargin = resources.getDimension(R.dimen.handDrawnButtonBorderMargin)
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
    var buttonElevation = 0f
        set(value) {
            field = value
            if (isInitialized) {
                setBackground()
            }
        }

    private val pressPadding by lazy {
        resources.getDimensionPixelOffset(R.dimen.handDrawnButtonPressedTextOffset)
    }

    private var isTextPressed = false
    private var isInitialized = false

    init {
        attrs?.applyAttributes(context, R.styleable.HandDrawnButton, defStyleAttr) { attributes ->
            attributes.applyStyledAttributes()
        }

        setBackground()

        setTextAppearance(
            context, R.style.HandDrawnEditText
        )

        isInitialized = true
        toggleTextPressed(!isEnabled)
    }

    private fun TypedArray.applyStyledAttributes() {
        borderMarginTop =
            getDimensionAttribute(R.styleable.HandDrawnButton_borderMarginTop) { defaultBorderMargin }
        borderMarginBottom =
            getDimensionAttribute(R.styleable.HandDrawnButton_borderMarginBottom) { defaultBorderMargin }
        borderMarginStart =
            getDimensionAttribute(R.styleable.HandDrawnButton_borderMarginStart) { defaultBorderMargin }
        borderMarginEnd =
            getDimensionAttribute(R.styleable.HandDrawnButton_borderMarginEnd) { defaultBorderMargin }

        buttonElevation =
            getDimensionAttribute(R.styleable.HandDrawnButton_buttonElevation) { borderMarginBottom * 2f }
    }

    private fun setBackground() {
        val borderMargins = Margins(
            borderMarginTop, borderMarginBottom, borderMarginStart, borderMarginEnd
        )
        ViewCompat.setBackground(
            this,
            StateListDrawable().apply {
                addState(
                    intArrayOf(-android.R.attr.state_enabled),
                    ButtonDrawable.Disabled(
                        outlinePainter,
                        fillPainter,
                        paint,
                        borderMargins,
                        buttonElevation,
                        resources,
                        context.theme
                    )
                )
                addState(
                    intArrayOf(android.R.attr.state_pressed),
                    ButtonDrawable.Pressed(
                        outlinePainter,
                        fillPainter,
                        paint,
                        borderMargins,
                        buttonElevation,
                        resources,
                        context.theme
                    )
                )
                addState(
                    intArrayOf(),
                    ButtonDrawable.Enabled(
                        outlinePainter,
                        fillPainter,
                        shadowPainter,
                        paint,
                        borderMargins,
                        buttonElevation,
                        resources,
                        context.theme
                    )
                )
            }
        )
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)

        if (!isInitialized) return
        toggleTextPressed(pressed)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        if (!isInitialized) return
        toggleTextPressed(!enabled)
    }

    private fun toggleTextPressed(pressed: Boolean) {
        if (pressed) {
            setTextPressed()
        } else {
            setTextUnpressed()
        }
    }

    private fun setTextPressed() {
        if (isTextPressed) return
        setPadding(
            paddingLeft,
            paddingTop + pressPadding,
            paddingRight,
            paddingBottom - pressPadding
        )
        isTextPressed = true
    }

    private fun setTextUnpressed() {
        if (!isTextPressed) return
        setPadding(
            paddingLeft,
            paddingTop - pressPadding,
            paddingRight,
            paddingBottom + pressPadding
        )
        isTextPressed = false
    }
}
