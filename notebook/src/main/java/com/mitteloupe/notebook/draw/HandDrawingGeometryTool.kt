package com.mitteloupe.notebook.draw

import android.graphics.Path
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

private const val SEGMENT_LENGTH = 8f
private const val SEGMENT_CIRCLE_STRAIGHT_GAP = SEGMENT_LENGTH * 1.5f
private const val SEGMENT_DISTANCE_SQUARED = SEGMENT_LENGTH * SEGMENT_LENGTH
private const val ERROR_THRESHOLD = SEGMENT_LENGTH * SEGMENT_LENGTH * 1.5

class HandDrawingGeometryTool(
    private val randomProvider: () -> Random = { Random.Default }
) : GeometryTool() {
    override fun linePath(
        horizontal: Float,
        vertical: Float,
        path: Path
    ): Path {
        if (horizontal * horizontal + vertical * vertical <= ERROR_THRESHOLD) {
            path.rLineTo(horizontal, vertical)
            return path
        }
        val angle = atan2(vertical, horizontal)
        var horizontalRemaining = horizontal
        var verticalRemaining = vertical
        val random = randomProvider()
        var errorOffset = random.nextFloat() * 256f
        while (horizontalRemaining * horizontalRemaining + verticalRemaining * verticalRemaining > SEGMENT_DISTANCE_SQUARED) {
            val error = sin(errorOffset / 5f * cos(errorOffset / 4f + 2f)) / 18f
            val angleWithError = angle + error
            val distanceX = cos(angleWithError) * SEGMENT_LENGTH
            val distanceY = sin(angleWithError) * SEGMENT_LENGTH
            path.rLineTo(distanceX, distanceY)
            horizontalRemaining -= distanceX
            verticalRemaining -= distanceY
            errorOffset++
        }

        path.rLineTo(horizontalRemaining, verticalRemaining)

        return path
    }

    override fun circlePath(
        centerX: Float,
        centerY: Float,
        radius: Float
    ) = arcPath(centerX, centerY, radius, 0f, 360f, Path()).apply {
        close()
    }

    override fun arcPath(
        centerX: Float,
        centerY: Float,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        path: Path
    ): Path {
        val partOfCircle = abs(endAngle - startAngle) / 360f
        val circumference = radius * 2f * Math.PI.toFloat()
        var distanceRemaining = circumference * partOfCircle
        val random = randomProvider()
        var errorOffset = random.nextFloat() * 256f
        path.apply {
            val startAngleRadians = startAngle.toRadians()
            val startX = centerX + sin(startAngleRadians) * radius
            val startY = centerY - cos(startAngleRadians) * radius
            if (isEmpty) {
                moveTo(startX, startY)
            } else {
                lineTo(startX, startY)
            }
        }
        val angleStep = Math.PI.toFloat() * 2f / (circumference / SEGMENT_LENGTH)
        var angle = startAngle.toRadians() + angleStep / 2f
        while (distanceRemaining > SEGMENT_CIRCLE_STRAIGHT_GAP) {
            val distanceMultiplier = min((distanceRemaining - SEGMENT_LENGTH) / 16f, 1f)
            val error = sin(errorOffset / 5f * cos(errorOffset / 4f + 2f)) / 8f * distanceMultiplier
            val angleWithError = angle + error
            val distanceX = cos(angleWithError) * SEGMENT_LENGTH
            val distanceY = sin(angleWithError) * SEGMENT_LENGTH
            path.rLineTo(distanceX, distanceY)
            distanceRemaining -= SEGMENT_LENGTH
            errorOffset++
            angle += angleStep
        }
        val endAngleRadians = endAngle.toRadians()
        path.lineTo(
            centerX + sin(endAngleRadians) * radius,
            centerY - cos(endAngleRadians) * radius
        )
        return path
    }

    private fun Float.toRadians() = this / 180f * Math.PI.toFloat()
}
