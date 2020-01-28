package nl.jovmit.lyrics.extensions

import android.view.MenuItem

fun MenuItem.onCollapse(callback: () -> Unit) {

    setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            callback.invoke()
            return true
        }
    })
}