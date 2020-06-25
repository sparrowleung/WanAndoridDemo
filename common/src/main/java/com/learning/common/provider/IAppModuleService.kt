package com.learning.common.provider

import com.alibaba.android.arouter.facade.template.IProvider

interface IAppModuleService : IProvider, IRouter {
    fun initSelfService()
}