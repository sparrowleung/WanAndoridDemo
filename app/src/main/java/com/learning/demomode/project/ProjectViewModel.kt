package com.learning.demomode.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.common.base.BaseViewModel
import com.learning.common.retrofit.VocError
import com.learning.demomode.REQUEST_PROJECT
import com.learning.common.bean.ProjectCategoryBean
import com.learning.common.database.DemoDataBase

class ProjectViewModel : BaseViewModel() {
    private val projectModel = ProjectModel(this)
    private var requestError: MutableLiveData<Map<Int, VocError>> = MutableLiveData()

    private lateinit var projectDB: DemoDataBase

    fun setProjectDB(db: DemoDataBase) {
        projectDB = db
    }

    fun requestProjectCategory() {
        vmScopeMainWithException(
            { projectModel.onRequestProject() },
            { requestError.value = mapOf(REQUEST_PROJECT to it) }
        )
    }

    fun getProjectCategory(): LiveData<List<ProjectCategoryBean>> = projectDB.projectCategoryDao().getProjectCategoryLiveData()

    fun getRequestError(): LiveData<Map<Int, VocError>> = requestError

    fun setProjectCategory(categoryBean: ArrayList<ProjectCategoryBean>) {
        projectDB.projectCategoryDao().insertList(categoryBean)
    }
}