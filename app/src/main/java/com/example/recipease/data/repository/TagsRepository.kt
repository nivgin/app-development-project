package com.example.recipease.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.recipease.dao.AppLocalDB
import com.example.recipease.dao.AppLocalDbRepository
import com.example.recipease.data.models.FirebaseModel
import com.example.recipease.model.Tags
import java.util.concurrent.Executors

class TagsRepository private constructor() {

    private val firebaseModel = FirebaseModel()
    private var executor = Executors.newSingleThreadExecutor()

    private val database: AppLocalDbRepository = AppLocalDB.db

    companion object {
        val shared = TagsRepository()
    }

    fun getAllTags(): LiveData<List<String>> {
        return database.tagsDao.getTags().map { tagsEntity ->
            tagsEntity?.tags ?: emptyList()
        }
    }

    fun refreshTags() {
        val lastUpdated = Tags.lastUpdated

        firebaseModel.getTags(lastUpdated) { fetchedTags ->
            executor.execute {
                fetchedTags?.let { tags ->
                    database.tagsDao.insertTags(tags)
                    tags.lastUpdated?.let { newLastUpdated ->
                        if (newLastUpdated > lastUpdated) {
                            Tags.lastUpdated = newLastUpdated
                        }
                    }
                }
            }
        }
    }
}


