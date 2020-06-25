package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.OfficialAuthorBean

@Dao
abstract class OfficialAuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(bean: OfficialAuthorBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(bean: List<OfficialAuthorBean>)

    @Delete
    abstract fun delete(bean: OfficialAuthorBean): Int

    @Delete
    abstract fun deleteList(bean: ArrayList<OfficialAuthorBean>): Int

    @Update
    abstract fun update(bean: OfficialAuthorBean): Int

    @Update
    abstract fun updateList(bean: List<OfficialAuthorBean>): Int

    @Query("SELECT * FROM OfficialAuthorBean")
    abstract fun getCategoryAuthorList(): LiveData<List<OfficialAuthorBean>>

    @Query("SELECT * FROM OfficialAuthorBean")
    abstract fun getCategoryAuthors(): List<OfficialAuthorBean>
}