package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.HomeTopArticleBean

@Dao
abstract class HomeTopArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(article: HomeTopArticleBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(article: ArrayList<HomeTopArticleBean>)

    @Delete
    abstract fun delete(article: HomeTopArticleBean)

    @Delete
    abstract fun deleteList(article: ArrayList<HomeTopArticleBean>)

    @Update
    abstract fun update(article: HomeTopArticleBean)

    @Query("SELECT * FROM HomeTopArticleBean")
    abstract fun getTopArticleList(): LiveData<List<HomeTopArticleBean>>


    @Query("SELECT * FROM HomeTopArticleBean")
    abstract fun getTopArticle(): List<HomeTopArticleBean>
}