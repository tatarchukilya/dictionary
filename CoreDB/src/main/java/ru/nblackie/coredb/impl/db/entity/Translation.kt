package ru.nblackie.coredb.impl.db.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author tatarchukilya@gmail.com
 */
@Entity
data class Translation(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @NonNull val word: Int,
    @NonNull val translation: Int
)
