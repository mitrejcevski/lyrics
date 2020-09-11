package nl.jovmit.lyrics.extensions

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import nl.jovmit.lyrics.R

fun SwipeRefreshLayout.applyDefaultColors() {
    setColorSchemeResources(R.color.colorPrimary, R.color.colorSecondary)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService<InputMethodManager>()
    inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}
