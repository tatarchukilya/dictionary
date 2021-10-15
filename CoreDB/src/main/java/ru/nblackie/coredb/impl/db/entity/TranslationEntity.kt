package ru.nblackie.coredb.impl.db.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author tatarchukilya@gmail.com
 */
@Entity(
    tableName = "translation",
    foreignKeys = [ForeignKey(
        entity = WordEntity::class,
        parentColumns = ["id"],
        childColumns = ["word"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["word", "translation"], unique = true)],
)
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @NonNull val word: Int,
    @NonNull val translation: Int
)
