package com.mitteloupe.notebook.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.mitteloupe.notebook.R
import com.mitteloupe.notebook.draw.GeometryToolFiller
import com.mitteloupe.notebook.draw.GeometryToolPainter
import com.mitteloupe.notebook.draw.HandDrawingGeometryTool
import com.mitteloupe.notebook.draw.Painter
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class HandDrawnPieView : View {
    private val randomSeed: Int
        get() = left + top

    private val values = mutableListOf(4, 7, 13)

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val geometryTool by lazy {
        HandDrawingGeometryTool { randomSeed }
    }

    private val painter: Painter = GeometryToolPainter(geometryTool)
    private val filler: Painter = GeometryToolFiller(geometryTool) { randomSeed }

    private val circleMargin by lazy {
        resources.getDimension(R.dimen.handDrawnPieViewCircleMargin)
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = min(width / 2f, height / 2f) - circleMargin
        val centerX = width / 2f
        val centerY = height / 2f

        filler.drawCircle(canvas, centerX, centerY, radius + 2f, paint.fillMode())
        painter.drawCircle(canvas, centerX, centerY, radius, paint.outlineMode())

        if (values.isEmpty()) return

        painter.drawLine(
            canvas, centerX, centerY, 0f, -radius, paint
        )

        val valuesSum = values.sum().toFloat()
        var currentAngle = 0f
        values.forEach { value ->
            currentAngle += (value.toFloat() / valuesSum * 360f).toRadians()
            painter.drawLine(
                canvas,
                centerX,
                centerY,
                sin(currentAngle) * radius,
                -cos(currentAngle) * radius,
                paint
            )
        }
    }

    private fun Paint.fillMode(): Paint {
        color = Color.argb(64, 0, 16, 64)
        strokeWidth = 9f
        return this
    }

    private fun Paint.outlineMode(): Paint {
        color = Color.argb(200, 0, 0, 0)
        strokeWidth = 3f
        return this
    }
}

private fun Float.toRadians(): Float = this * Math.PI.toFloat() / 180f
