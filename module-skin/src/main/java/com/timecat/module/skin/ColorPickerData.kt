package com.timecat.module.skin

class ColorPickerData {
    var name = ""
    private var value = "000000"
    private var alpha = "ff"
    var colorRes = 0
    fun setAlpha(alpha: String) {
        if (alpha.length == 2) {
            this.alpha = alpha
        } else if (alpha.length == 1) {
            this.alpha = "0$alpha"
        } else {
            this.alpha = "ff"
        }
    }

    fun getAlpha(): String = alpha

    fun setValue(value: String) {
        var value = value
        if (value.length == 6) {
            this.value = value
        } else if (value.length < 6) {
            val valueLen = value.length
            for (i in 0 until 6 - valueLen) {
                value = "0$value"
            }
            this.value = value
        } else {
            this.value = "000000"
        }
    }

    fun getValue(): String = value

    val color: String
        get() = "#$alpha$value"
}