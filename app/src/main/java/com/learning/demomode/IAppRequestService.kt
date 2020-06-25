package com.learning.demomode

import com.learning.common.base.BaseResult
import com.learning.common.bean.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val REQUEST_BANNER = 1
const val REQUEST_ARTICLE = 2
const val REQUEST_ARTICLE_TOP = 3
const val REQUEST_PROJECT = 4
const val REQUEST_PROJECT_PAGER = 5
const val REQUEST_SYSTEM = 6
const val REQUEST_SYSTEM_ARTICLE = 7
const val REQUEST_OFFICIAL_AUTHOR = 8
const val REQUEST_OFFICIAL_ARTICLE = 9
const val REQUEST_OFFICIAL_ALL = 10
const val REQUEST_SEARCH = 11

interface IAppRequestService {

    @GET("banner/json")
    suspend fun getHomeBanner(): BaseResult<List<BannerBean>>

    @GET("article/list/{page}/json")
    suspend fun getHomeArticle(@Path("page") page: Int): BaseResult<HomeArticleListBean>

    @GET("article/top/json")
    suspend fun getHomeArticleTop(): BaseResult<ArrayList<HomeTopArticleBean>>

    @GET("tree/json")
    suspend fun getSystemData(): BaseResult<ArrayList<SystemCategoryBean>>

    @GET("article/list/{page}/json")
    suspend fun getSystemArticle(@Path("page") page: Int, @Query("cid") id: Int): BaseResult<HomeArticleListBean>

    @GET("wxarticle/chapters/json")
    suspend fun getOfficialAuthor(): BaseResult<ArrayList<OfficialAuthorBean>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getOfficialArticle(@Path("id") id: Int, @Path("page") page: Int): BaseResult<OfficialArticleListBean>

    @GET("project/tree/json")
    suspend fun getProjectData(): BaseResult<ArrayList<ProjectCategoryBean>>

    @GET("project/list/{page}/json")
    suspend fun getProjectPagerData(@Path("page") page: Int, @Query("cid") id: Int): BaseResult<ArticleListBean>

    @POST("article/query/{page}/json")
    suspend fun getSearchData(@Path("page") page: Int, @Query("k") k: String): BaseResult<HomeArticleListBean>
}