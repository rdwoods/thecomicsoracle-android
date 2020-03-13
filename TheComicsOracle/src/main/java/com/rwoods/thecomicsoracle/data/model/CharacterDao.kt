package com.rwoods.thecomicsoracle.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterDao {
    @Query("SELECT * FROM comicCharacter")
    fun getAll(): List<ComicCharacter>

    @Query("SELECT * FROM comicCharacter WHERE id IN (:characterIds)")
    fun loadAllByIds(characterIds: IntArray): List<ComicCharacter>

    @Insert
    fun insertAll(vararg characters: ComicCharacter)

    @Delete
    fun delete(character: ComicCharacter)
}