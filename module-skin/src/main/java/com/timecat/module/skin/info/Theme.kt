package com.timecat.module.skin.info

import androidx.appcompat.app.AppCompatDelegate
import com.timecat.page.base.theme.ThemeSkin

enum class Theme(val value: Int) {
    LIGHT(ThemeSkin.LIGHT),
    DARK(ThemeSkin.DARK);

    fun apply() {
        val mode = when (this) {
            LIGHT -> {
                ThemeSkin.setTheme(ThemeSkin.LIGHT)
                AppCompatDelegate.MODE_NIGHT_NO
            }
            DARK -> {
                ThemeSkin.setTheme(ThemeSkin.DARK)
                AppCompatDelegate.MODE_NIGHT_YES
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {
        fun find(): Theme {
            val value = ThemeSkin.getThemeType()
            return checkNotNull(values().find { it.value == value })
        }
    }
}