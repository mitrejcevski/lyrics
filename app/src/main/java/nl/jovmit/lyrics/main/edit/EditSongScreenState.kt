package nl.jovmit.lyrics.main.edit

data class EditSongScreenState(
  val isLoading: Boolean = false,
  val songTitle: String = "",
  val songPerformer: String = "",
  val songLyrics: String = "",
  val isEmptySongTitleError: Boolean = false,
  val isEmptyPerformerNameError: Boolean = false,
  val isEmptyLyricsError: Boolean = false,
  val isErrorSavingSong: Boolean = false,
  val songUpdated: Boolean = false
)