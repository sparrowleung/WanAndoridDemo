package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.HomeArticleBean

@Dao
abstract class HomeArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(article: HomeArticleBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(article: ArrayList<HomeArticleBean>)

    @Delete
    abstract fun delete(article: HomeArticleBean)

    @Delete
    abstract fun deleteList(article: ArrayList<HomeArticleBean>)

    @Update
    abstract fun update(article: HomeArticleBean)

    @Query("SELECT * FROM HomeArticleBean")
    abstract fun getArticleList(): LiveData<List<HomeArticleBean>>

    /**
     * 因为文章是按时间远到近排序
     * 所以获取文章是通过降序方式获取最近的文章
     * */
    @Query("SELECT * FROM HomeArticleBean ORDER BY publishTime DESC")
    abstract fun getArticles(): List<HomeArticleBean>

    @Query("SELECT * FROM HomeArticleBean ORDER BY publishTime DESC LIMIT :num ")
    abstract fun getArticlesForAdv(num:Int): List<HomeArticleBean>
}