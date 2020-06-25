package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.ProjectCategoryBean

@Dao
abstract class ProjectCategoryDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(bean: ProjectCategoryBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(bean: List<ProjectCategoryBean>)

    @Delete
    abstract fun delete(bean: ProjectCategoryBean): Int

    @Delete
    abstract fun deleteList(bean: ArrayList<ProjectCategoryBean>): Int

    @Update
    abstract fun update(bean: ProjectCategoryBean): Int

    @Update
    abstract fun updateList(bean: List<ProjectCategoryBean>): Int

    @Query("SELECT * FROM ProjectCategoryBean")
    abstract fun getProjectCategoryData(): List<ProjectCategoryBean>

    @Query("SELECT * FROM ProjectCategoryBean")
    abstract fun getProjectCategoryLiveData(): LiveData<List<ProjectCategoryBean>>
}