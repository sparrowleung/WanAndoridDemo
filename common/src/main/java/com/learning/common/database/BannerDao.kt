package com.learning.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learning.common.bean.BannerBean

@Dao
abstract class BannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(banner: BannerBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertList(banner: List<BannerBean>)

    @Delete
    abstract fun delete(banner: BannerBean): Int

    @Delete
    abstract fun deleteList(banner: ArrayList<BannerBean>): Int

    @Update
    abstract fun update(banner: BannerBean): Int

    @Update
    abstract fun updateList(banner: List<BannerBean>): Int

    @Query("SELECT * FROM BannerBean")
    abstract fun getBannerList(): LiveData<List<BannerBean>>

    @Query("SELECT * FROM BannerBean")
    abstract fun getBanners(): List<BannerBean>
}