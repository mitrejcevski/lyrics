package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.result.SongsResult

interface SongsRepository {

    suspend fun fetchAllSongs(): SongsResult
}
