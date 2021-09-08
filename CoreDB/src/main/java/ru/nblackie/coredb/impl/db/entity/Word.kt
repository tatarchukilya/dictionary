package ru.nblackie.coredb.impl.db.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author tatarchukilya@gmail.com
 */
@Entity
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @NonNull val word: String,
    @Nullable val transcription: String?,
    @NonNull val lang: String
)

