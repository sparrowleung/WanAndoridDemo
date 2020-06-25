package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.SearchHistoryBean

@Dao
abstract class SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(history: SearchHistoryBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(history: List<SearchHistoryBean>)

    @Delete
    abstract fun delete(history: SearchHistoryBean): Int

    @Delete
    abstract fun deleteList(history: ArrayList<SearchHistoryBean>): Int

    @Update
    abstract fun update(history: SearchHistoryBean): Int

    @Update
    abstract fun updateList(history: List<SearchHistoryBean>): Int

    @Query("SELECT * FROM SearchHistoryBean ORDER BY dateTime DESC")
    abstract fun getHistoryList(): LiveData<List<SearchHistoryBean>>

    @Query("SELECT * FROM SearchHistoryBean ORDER BY dateTime DESC")
    abstract fun getHistoryData(): List<SearchHistoryBean>
}