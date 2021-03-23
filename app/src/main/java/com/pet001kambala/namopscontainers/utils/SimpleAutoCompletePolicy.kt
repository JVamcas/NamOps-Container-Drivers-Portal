package com.pet001kambala.namopscontainers.utils

import android.text.Spannable
import com.otaliastudios.autocomplete.AutocompletePolicy

class SimpleAutoCompletePolicy : AutocompletePolicy {
    override fun shouldShowPopup(text: Spannable?, cursorPos: Int): Boolean {
        return text?.length ?: -1 > 0
    }

    override fun shouldDismissPopup(text: Spannable?, cursorPos: Int): Boolean {
        return text?.length == 0
    }

    override fun getQuery(text: Spannable): CharSequence {
        return text
    }

    override fun onDismiss(text: Spannable?) {}
}