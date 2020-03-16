package com.mitteloupe.notebook.widget.style

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleableRes

private const val ATTRIBUTE_DIMENSION_DEFAULT = -1f

fun AttributeSet.applyAttributes(
    context: Context,
    @StyleableRes attributeIds: IntArray,
    defStyleAttr: Int,
    applyStyledAttributes: (TypedArray) -> Unit
) {
    val attributes =
        context.obtainStyledAttributes(this, attributeIds, defStyleAttr, 0)
    try {
        applyStyledAttributes(attributes)
    } finally {
        attributes.recycle()
    }
}

fun TypedArray.getDimensionAttribute(
    @StyleableRes resourceId: Int,
    defaultValue: () -> Float
) = if (hasValue(resourceId)) {
    val dimension = getDimension(resourceId, ATTRIBUTE_DIMENSION_DEFAULT)
    if (dimension == ATTRIBUTE_DIMENSION_DEFAULT) {
        defaultValue()
    } else {
        dimension
    }
} else {
    defaultValue()
}
