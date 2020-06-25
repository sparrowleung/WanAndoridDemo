package com.learning.demomode.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.common.base.BaseViewModel
import com.learning.common.retrofit.VocError
import com.learning.demomode.REQUEST_SYSTEM
import com.learning.common.bean.SystemCategoryBean
import com.learning.common.database.DemoDataBase
import kotlinx.coroutines.delay

class SystemViewModel : BaseViewModel() {
    private var systemData: MutableLiveData<ArrayList<SystemCategoryBean>> = MutableLiveData()
    private var isClickByLeft: Boolean = false
    private var clickPosition: MutableLiveData<Int> = MutableLiveData()
    private var requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()
    private val systemModel by lazy { SystemModel(this) }
    private lateinit var systemDB: DemoDataBase

    fun setSystemDB(db: DemoDataBase) {
        systemDB = db
    }

    fun onClickOfLeft(pos: Int) {
        isClickByLeft = true
        clickPosition.value = pos
    }

    fun onRequestSystemData() {
        vmScopeMainWithException(
            { systemModel.onRequestSystemData() },
            { requestError.value = mapOf(REQUEST_SYSTEM to it) }
        )
    }

    fun getSystemData(): LiveData<ArrayList<SystemCategoryBean>> = systemData

    fun getCurrentPosition(): LiveData<Int> = clickPosition

    fun getRequestError(): LiveData<Map<Int, VocError>> = requestError

    fun getListVisitableText(index: Int): String {
        return if (systemDB.systemCategoryDao().getSysCategoryData().isNotEmpty()) {
            systemDB.systemCategoryDao().getSysCategoryData()[index].name
        }else{
            systemData.value?.get(index)?.name ?:""
        }
    }

    fun isClickByLeft(): Boolean = isClickByLeft

    fun setClickByLeft(state: Boolean) {
        isClickByLeft = state
    }

    fun setSystemData(systemBean: ArrayList<SystemCategoryBean>) {
        vmScopeIO {
            systemDB.systemCategoryDao().insertList(systemBean)

            for (category in systemBean) {
                systemDB.systemChildDao().insertList(category.children)
            }

            delay(100)
            systemData.postValue(systemBean)
        }
    }
}