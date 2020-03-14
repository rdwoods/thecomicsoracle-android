package com.rwoods.thecomicsoracle.ui.description

import android.widget.TextView

class TextViewImageStore {

        var textView: TextView? = null
        var intrinsicWidth: Int = 0
        var intrinsicHeight: Int = 0

        constructor(textView: TextView?, intrinsicWidth: Int, intrinsicHeight: Int) {
            this.textView = textView
            this.intrinsicWidth = intrinsicWidth
            this.intrinsicHeight = intrinsicHeight
        }
    }