package com.mitteloupe.notebook.draw

import android.graphics.Path

abstract class GeometryTool {
    abstract fun linePath(
        horizontal: Float,
        vertical: Float,
        path: Path = Path()
    ): Path

    open fun rectanglePath(
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ): Path {
        val path = Path().apply {
            moveTo(x, y)
        }
        linePath(width, 0f, path)
        linePath(0f, height, path)
        linePath(-width, 0f, path)
        linePath(0f, -height, path)
        path.close()
        return path
    }

    abstract fun circlePath(
        x: Float,
        y: Float,
        radius: Float
    ): Path

    class Fake : GeometryTool() {
        override fun linePath(horizontal: Float, vertical: Float, path: Path) = path

        override fun rectanglePath(x: Float, y: Float, width: Float, height: Float) = Path()

        override fun circlePath(x: Float, y: Float, radius: Float) = Path()
    }
}