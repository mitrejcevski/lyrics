package nl.jovmit.lyrics.main.details

import nl.jovmit.lyrics.main.data.song.Song

data class SongDetailsScreenState(
  val isLoading: Boolean = false,
  val song: Song? = null,
  val isSongNotFoundError: Boolean = false,
  val isSongDeleted: Boolean = false,
  val isSongDeletingError: Boolean = false
)