package com.github.didahdx.githubapp.common.extension

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.Chip

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}


fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }

    return this
}

fun View.setVisibility(isVisible: Boolean?): View {
    if (isVisible != null) {
        if (isVisible) show() else hide()
    } else {
        hide()
    }
    return this
}

fun Chip.displayDataIfNotNull(value: String?): View {
    if (value.isNullOrEmpty()) {
        hide()
    } else {
        show()
        text = value
    }
    return this
}

fun TextView.displayDataIfNotNull(value: String?, onClick: View.OnClickListener?): View {
    if (value.isNullOrEmpty()) {
        hide()
    } else {
        show()
        text = value
        onClick?.let { viewClick ->
            setOnClickListener(viewClick)
        }
    }
    return this
}

fun TextView.displayDataIfNotNull(
    value: String?,
    text: String,
    onClick: View.OnClickListener?
): View {
    if (value.isNullOrEmpty()) {
        hide()
    } else {
        show()
        this.text = text
        onClick?.let { viewClick ->
            setOnClickListener(viewClick)
        }
    }
    return this
}

inline fun SearchView.onQueryTextSubmit(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrBlank()) {
                listener(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    })
}

