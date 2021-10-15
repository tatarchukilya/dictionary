package ru.nblackie.coredb.impl.db.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author tatarchukilya@gmail.com
 */
@Entity(
    tableName = "word",
    indices = [Index(value = ["word"], unique = true)],
)
data class WordEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @NonNull val word: String,
    @Nullable val transcription: String? = null,
    @NonNull val lang: String
)

