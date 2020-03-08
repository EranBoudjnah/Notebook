package com.mitteloupe.notebook.widget.style

import android.view.View

class TextOffsetter(
    private val view: View,
    private val pressPadding: Int
) {
    private var isTextPressed = false
    private var isInitialized = false

    fun initialize() {
        isInitialized = true
    }

    fun toggleTextPressed(pressed: Boolean) {
        if (!isInitialized) return
        if (pressed) {
            setTextPressed()
        } else {
            setTextUnpressed()
        }
    }

    private fun setTextPressed() {
        if (isTextPressed) return
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + pressPadding,
            view.paddingRight,
            view.paddingBottom - pressPadding
        )
        isTextPressed = true
    }

    private fun setTextUnpressed() {
        if (!isTextPressed) return
        view.setPadding(
            view.paddingLeft,
            view.paddingTop - pressPadding,
            view.paddingRight,
            view.paddingBottom + pressPadding
        )
        isTextPressed = false
    }
}