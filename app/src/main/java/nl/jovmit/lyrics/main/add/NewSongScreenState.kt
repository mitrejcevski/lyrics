package nl.jovmit.lyrics.main.add

data class NewSongScreenState(
  val isLoading: Boolean = false,
  val songTitle: String = "",
  val performerName: String = "",
  val lyrics: String = "",
  val isEmptySongTitleError: Boolean = false,
  val isEmptyPerformerNameError: Boolean = false,
  val isEmptyLyricsError: Boolean = false,
  val songAdded: Boolean = false,
  val errorAddingSong: Boolean = false
)