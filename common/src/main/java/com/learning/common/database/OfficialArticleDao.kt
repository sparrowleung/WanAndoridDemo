package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.OfficialArticleBean


@Dao
abstract class OfficialArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(article: OfficialArticleBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(article: ArrayList<OfficialArticleBean>)

    @Delete
    abstract fun delete(article: OfficialArticleBean)

    @Delete
    abstract fun deleteList(article: ArrayList<OfficialArticleBean>)

    @Query("DELETE FROM OfficialArticleBean")
    abstract fun deleteAll()

    @Update
    abstract fun update(article: OfficialArticleBean)

    @Query("SELECT * FROM OfficialArticleBean")
    abstract fun getArticleList(): LiveData<List<OfficialArticleBean>>

    /**
     * 因为文章是按时间远到近排序
     * 所以获取文章是通过降序方式获取最近的文章
     * */
    @Query("SELECT * FROM OfficialArticleBean ORDER BY publishTime DESC")
    abstract fun getArticles(): List<OfficialArticleBean>

    @Query("SELECT * FROM OfficialArticleBean ORDER BY publishTime DESC LIMIT :num ")
    abstract fun getArticlesForAdv(num:Int): List<OfficialArticleBean>
}