package nl.jovmit.lyrics.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.listen(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer<T> {
        it?.let(observer)
    })
}