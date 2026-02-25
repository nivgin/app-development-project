package com.example.recipease.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipease.model.Tags

@Dao
interface TagsDao {

    @Query("SELECT * FROM Tags LIMIT 1")
    fun getTags(): LiveData<Tags?>

    @Query("SELECT * FROM Tags WHERE id = :id")
    fun getTagsById(id: Int): Tags?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTags(tags: Tags)

    @Delete
    fun deleteTags(tags: Tags)
}

