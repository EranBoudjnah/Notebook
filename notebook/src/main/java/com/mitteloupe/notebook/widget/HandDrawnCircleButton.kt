package com.mitteloupe.notebook.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.Filler
import com.mitteloupe.notebook.draw.GeometryToolFiller
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter
import com.mitteloupe.notebook.drawable.ButtonDrawable
import com.mitteloupe.notebook.widget.style.TextOffsetter

class HandDrawnCircleButton @JvmOverloads constructor(
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

    private val geometryTool = HandDrawingGeometryTool { randomSeed }

    private val outlinePainter: Painter = GeometryToolPainter(geometryTool)
    private val fillPainter: Painter = GeometryToolFiller(geometryTool, Filler.Both) { randomSeed }
    private val shadowPainter: Painter =
        GeometryToolFiller(geometryTool, Filler.Horizontal) { randomSeed }

    private val borderMargin = resources.getDimension(R.dimen.handDrawnButtonBorderMargin)

    private val pressPadding by lazy {
        resources.getDimensionPixelOffset(R.dimen.handDrawnButtonPressedTextOffset)
    }

    private val textOffsetter = TextOffsetter(this, pressPadding)

    init {
        setBackgroundDrawable(
            ButtonDrawable.stateListDrawable(
                outlinePainter, fillPainter, shadowPainter, paint, borderMargin, true
            )
        )

        setTextAppearance(
            context,
            R.style.HandDrawnEditText
        )

        textOffsetter.initialize()
        textOffsetter.toggleTextPressed(!isEnabled)
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)

        textOffsetter.toggleTextPressed(pressed)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        textOffsetter.toggleTextPressed(!enabled)
    }
}
