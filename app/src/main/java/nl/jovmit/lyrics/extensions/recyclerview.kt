package nl.jovmit.lyrics.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setupWithLinearLayoutManager(
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView.LayoutManager {
    val layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
    this.layoutManager = layoutManager
    avoidInvalidatingOnAdapterContentChanges()
    return layoutManager
}

fun RecyclerView.avoidInvalidatingOnAdapterContentChanges() {
    setHasFixedSize(true)
}