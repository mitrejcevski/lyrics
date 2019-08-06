package nl.jovmit.lyrics.main.overview

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.extensions.inflate
import nl.jovmit.lyrics.main.data.Song

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    private val songs = mutableListOf<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.song_list_item)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    fun addSongs(newSongs: List<Song>) {
        val startPosition = songs.size
        songs.addAll(newSongs)
        notifyItemRangeInserted(startPosition, newSongs.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.songListItemTitle)
        private val singer: TextView = itemView.findViewById(R.id.songListItemSinger)

        fun bind(song: Song) {
            title.text = song.title
            singer.text = song.singer
        }
    }
}
