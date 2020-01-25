package nl.jovmit.lyrics.utils

import java.util.*

class IdGenerator {

    fun next(): String = UUID.randomUUID().toString()
}
