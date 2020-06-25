package com.learning.common.database

import androidx.room.*
import com.learning.common.bean.SystemCategoryBean

@Dao
abstract class SystemCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(bean: SystemCategoryBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(bean: List<SystemCategoryBean>)

    @Delete
    abstract fun delete(bean: SystemCategoryBean): Int

    @Delete
    abstract fun deleteList(bean: ArrayList<SystemCategoryBean>): Int

    @Update
    abstract fun update(bean: SystemCategoryBean): Int

    @Update
    abstract fun updateList(bean: List<SystemCategoryBean>): Int

    @Query("SELECT * FROM SystemCategoryBean")
    abstract fun getSysCategoryData(): List<SystemCategoryBean>
}