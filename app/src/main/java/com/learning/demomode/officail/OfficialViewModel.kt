package com.learning.demomode.officail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.learning.common.base.BaseViewModel
import com.learning.common.bean.*
import com.learning.common.database.DemoDataBase
import com.learning.common.retrofit.RetrofitClient
import com.learning.common.retrofit.VocError
import com.learning.demomode.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn

class OfficialViewModel : BaseViewModel() {
    private lateinit var officialDb: DemoDataBase

    private var articlePageNum: Int = 1
    private var selectAccountId: Int = 0
    private val officialModel = OfficialModel(this)
    private var requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()
    private var diffResult: MutableLiveData<DiffUtil.DiffResult> = MutableLiveData()
    private var officialArticleList: MutableLiveData<List<OfficialArticleBean>> = MutableLiveData()

    fun setCategoryDao(db: DemoDataBase) {
        this.officialDb = db
    }

    fun onRequestArticle(id: Int, page: Int = articlePageNum) {
        vmScopeMainWithException(
            {
                officialModel.onRequestArticle(id, page)
            },
            { requestError.value = mapOf(REQUEST_OFFICIAL_ARTICLE to it) }
        )
    }

    fun onCalculateDiff(oldData: ArrayList<ArticleBean>, newData: ArrayList<ArticleBean>) {
        vmScopeIO {
            diffResult.postValue(DiffUtil.calculateDiff(MyDiffUtilCallBack(oldData, newData), true))
        }
    }

    fun onRequestFirstPage() {
        vmScopeIOWithException(
            {
                vmScopeWithFlow {
                    RetrofitClient.create(IAppRequestService::class.java)
                        .getOfficialAuthor()
                }.flatMapConcat {
                    return@flatMapConcat if (it.isSuccess()) {
                        val id = it.data[0].id
                        setOfficialAuthorList(it.data)
                        articlePageNum += 1
                        vmScopeWithFlow {
                            RetrofitClient.create(IAppRequestService::class.java)
                                .getOfficialArticle(id, 1)
                        }
                    } else throw Exception(it.errorMsg)
                }.flowOn(Dispatchers.IO)
                    .collect {
                        setOfficialArticleList(it.data.datas)
                    }
            },
            { requestError.postValue(mapOf(REQUEST_OFFICIAL_ALL to it)) }
        )
    }

    fun getOfficialAuthorList(): LiveData<List<OfficialAuthorBean>> =
        officialDb.officialAuthorDao().getCategoryAuthorList()

    fun getOfficialArticleList(): LiveData<List<OfficialArticleBean>> = officialArticleList

    fun getSelectAccountId(): Int = selectAccountId

    fun getRequestError(): LiveData<Map<Int, VocError>> = requestError

    fun getDiffResult(): LiveData<DiffUtil.DiffResult> = diffResult

    private fun setOfficialAuthorList(bean: ArrayList<OfficialAuthorBean>) {
        officialDb.officialAuthorDao().insertList(bean.toList())
    }

    fun setOfficialArticleList(bean: ArrayList<OfficialArticleBean>) {
        officialArticleList.postValue(bean)
    }

    fun setSelectAccountId(id: Int) {
        selectAccountId = id
    }

    fun setArticlePageNum(page: Int) {
        articlePageNum = page
    }
}