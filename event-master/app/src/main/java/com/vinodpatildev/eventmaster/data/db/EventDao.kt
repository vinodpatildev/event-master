package com.vinodpatildev.eventmaster.data.db

import androidx.room.*
import com.vinodpatildev.eventmaster.data.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Event)

    @Delete()
    suspend fun delete(article:Event)

    @Query("SELECT * FROM events")
    fun getAllArticles() : Flow<List<Event>>

    @Query("DELETE FROM events")
    fun deleteAllArticles()
}