package com.cricketpredictions.app.ui.fragments.objects

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SeparatorView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    val linePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#E6000000")
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null)
            return
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, linePaint)
    }

}