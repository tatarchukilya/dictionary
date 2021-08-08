package ru.nblackie.coredb.impl.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author tatarchukilya@gmail.com
 */
@Entity(tableName = "dictionary")
data class WordEntity(
    @PrimaryKey(autoGenerate = false)
    var word: String,
    var transcription: String,
    val translations: String,
    val examples: String
)

