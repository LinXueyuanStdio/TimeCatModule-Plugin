package com.timecat.module.skin.info

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.timecat.module.skin.R

class SkinInfoBottomSheetDialog : BottomSheetDialogFragment() {

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.skin_fragment_theme_info, null)
        setupViews(view)
        dialog.setContentView(view)
    }

    private fun setupViews(view: View) {
        val toggleGroup: MaterialButtonToggleGroup = view.findViewById(R.id.selectThemeToggleGroup)
        toggleGroup.check(Theme.find().getToggleButtonId())
        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val theme = when (checkedId) {
                    R.id.lightThemeButton -> Theme.LIGHT
                    R.id.darkThemeButton -> Theme.DARK
                    else -> throw IllegalArgumentException()
                }
                theme.apply()
            }
        }
    }

    fun showIfNeed(fragmentManager: FragmentManager) {
        if (fragmentManager.findFragmentByTag(TAG) != null) {
            return
        }
        show(fragmentManager, TAG)
    }

    private fun Theme.getToggleButtonId(): Int {
        return when (this) {
            Theme.LIGHT -> R.id.lightThemeButton
            Theme.DARK -> R.id.darkThemeButton
        }
    }

    companion object {
        private const val TAG = "ThemeInfoBottomSheetDialog"
    }
}