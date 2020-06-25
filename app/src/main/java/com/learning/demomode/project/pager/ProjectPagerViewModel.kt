package com.learning.demomode.project.pager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.learning.common.base.BaseViewModel
import com.learning.common.retrofit.VocError
import com.learning.common.bean.ArticleBean
import com.learning.common.bean.ArticleListBean
import com.learning.demomode.MyDiffUtilCallBack
import com.learning.demomode.REQUEST_PROJECT_PAGER

class ProjectPagerViewModel : BaseViewModel() {

    private var projectDetailPageNum = 1
    private val pagerModel = ProjectPagerModel(this)
    private var diffResult: MutableLiveData<DiffUtil.DiffResult> = MutableLiveData()
    private var requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()
    private var projectDetailData: MutableLiveData<ArticleListBean> = MutableLiveData()

    fun requestProjectArticle(cid: Int, page: Int = projectDetailPageNum) {
        vmScopeMainWithException(
            { pagerModel.onRequestProjectDetail(cid, page) },
            { requestError.value = mapOf(REQUEST_PROJECT_PAGER to it) }
        )
    }

    fun calculateDiff(
        oldData: ArrayList<ArticleBean>,
        newData: ArrayList<ArticleBean>
    ) {
        vmScopeIO {
            diffResult.postValue(
                DiffUtil.calculateDiff(
                    MyDiffUtilCallBack(oldData, newData)
                    , true
                )
            )
        }
    }

    fun getProjectDetailData(): LiveData<ArticleListBean> = projectDetailData

    fun getRequestError(): LiveData<Map<Int, VocError>> = requestError

    fun getDiffResult(): LiveData<DiffUtil.DiffResult> = diffResult

    fun setProjectDetailData(bean: ArticleListBean) {
        projectDetailData.postValue(bean)
    }

    fun setProjectPageNum(num: Int) {
        projectDetailPageNum = num
    }

}