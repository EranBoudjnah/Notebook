package com.mitteloupe.notebook.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.SwitchCompat
import com.mitteloupe.notebook.draw.Filler
import com.mitteloupe.notebook.draw.GeometryToolFiller
import com.mitteloupe.notebook.draw.GeometryToolTracer
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter
import com.mitteloupe.notebook.drawable.SwitchThumbDrawable
import com.mitteloupe.notebook.drawable.SwitchTrackDrawable
import kotlin.random.Random

class HandDrawnSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.switchStyle
) : SwitchCompat(context, attrs, defStyleAttr) {
    private val randomSeed: Int
        get() = left + top

    private val geometryTool by lazy {
        HandDrawingGeometryTool { Random(randomSeed) }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val outlinePainter: Painter = GeometryToolTracer(geometryTool)
    private val fillPainter: Painter = GeometryToolFiller(geometryTool, Filler.Both) { randomSeed }
    private val shadowPainter: Painter =
        GeometryToolFiller(geometryTool, Filler.Horizontal) { randomSeed }

    private val circleMargin by lazy {
        resources.getDimension(com.mitteloupe.notebook.R.dimen.handDrawnSwitchCircleMargin)
    }

    init {
        thumbDrawable = SwitchThumbDrawable.stateListDrawable(
            outlinePainter, fillPainter, shadowPainter, paint, circleMargin, true, resources,
            context.theme, 64, 64
        )
        trackDrawable = SwitchTrackDrawable.stateListDrawable(
            outlinePainter, paint, resources, context.theme, 64, 64
        )
    }
}
