package com.learning.common.database

import com.learning.common.bean.SystemChildBean
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class SystemChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(bean: SystemChildBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(bean: List<SystemChildBean>)

    @Delete
    abstract fun delete(bean: SystemChildBean): Int

    @Delete
    abstract fun deleteList(bean: ArrayList<SystemChildBean>): Int

    @Update
    abstract fun update(bean: SystemChildBean): Int

    @Update
    abstract fun updateList(bean: List<SystemChildBean>): Int


    @Query("SELECT * FROM SystemChildBean WHERE parentChapterId = :id")
    abstract fun getSysCategoryData(id:Int): List<SystemChildBean>
}