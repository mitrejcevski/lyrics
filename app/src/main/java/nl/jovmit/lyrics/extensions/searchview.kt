package nl.jovmit.lyrics.extensions

import androidx.appcompat.widget.SearchView

fun SearchView.onQuerySubmit(callback: (String) -> Unit) {

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { callback.invoke(it) }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}