package nl.jovmit.lyrics.extensions

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import nl.jovmit.lyrics.R

fun SwipeRefreshLayout.applyDefaultColors() {
    setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent)
}
