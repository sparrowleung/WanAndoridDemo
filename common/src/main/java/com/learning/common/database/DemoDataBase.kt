package com.learning.common.database

import android.content.Context
import androidx.room.*
import com.learning.common.bean.*

@Database(
    entities = [HomeArticleBean::class, BannerBean::class, HomeTopArticleBean::class, OfficialAuthorBean::class,
        SystemCategoryBean::class, SystemChildBean::class, ProjectCategoryBean::class, SearchHistoryBean::class],
    version = 1
)
abstract class DemoDataBase : RoomDatabase() {
    abstract fun homeArticleTopDao(): HomeTopArticleDao
    abstract fun homeArticleDao(): HomeArticleDao
    abstract fun homeBannerDao(): BannerDao
    abstract fun officialAuthorDao(): OfficialAuthorDao
    abstract fun systemCategoryDao(): SystemCategoryDao
    abstract fun systemChildDao(): SystemChildDao
    abstract fun projectCategoryDao(): ProjectCategoryDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        fun getDataBase(context: Context): DemoDataBase = Room.databaseBuilder(
            context.applicationContext,
            DemoDataBase::class.java, "db_test"
        ).allowMainThreadQueries().build()
    }
}