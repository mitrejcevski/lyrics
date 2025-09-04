package nl.jovmit.lyrics.main.overview

import nl.jovmit.lyrics.main.data.song.Song

data class SongsOverviewScreenState(
  val isLoading: Boolean = false,
  val songs: List<Song> = emptyList(),
  val searchQuery: String = "",
  val isErrorLoadingSongs: Boolean = false,
  val isErrorSearching: Boolean = false
)