package com.mitteloupe.notebook.widget

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.Filler
import com.mitteloupe.notebook.draw.GeometryToolFiller
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter
import com.mitteloupe.notebook.drawable.ButtonDrawable

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

    private val borderMargin = resources.getDimension(R.dimen.handDrawnButtonBorderMargin)

    private val pressPadding by lazy {
        resources.getDimensionPixelOffset(R.dimen.handDrawnButtonPressedTextOffset)
    }

    private var isTextPressed = false
    private var isInitialized = false

    init {
        setBackgroundDrawable(
            StateListDrawable().apply {
                addState(
                    intArrayOf(-android.R.attr.state_enabled),
                    ButtonDrawable.Disabled(
                        outlinePainter, fillPainter, paint, borderMargin, resources, context.theme
                    )
                )
                addState(
                    intArrayOf(android.R.attr.state_pressed),
                    ButtonDrawable.Pressed(
                        outlinePainter, fillPainter, paint, borderMargin, resources, context.theme
                    )
                )
                addState(
                    intArrayOf(),
                    ButtonDrawable.Enabled(
                        outlinePainter,
                        fillPainter,
                        shadowPainter,
                        paint,
                        borderMargin,
                        resources,
                        context.theme
                    )
                )
            }
        )

        setTextAppearance(
            context,
            R.style.HandDrawnEditText
        )

        isInitialized = true
        toggleTextPressed(!isEnabled)
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
