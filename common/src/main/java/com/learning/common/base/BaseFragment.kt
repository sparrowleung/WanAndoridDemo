package com.learning.common.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<ViewModel : BaseViewModel, ViewBinding : ViewDataBinding> : Fragment() {

    protected lateinit var safeContext: Context
    protected lateinit var TAG: String

    private var loadingState: Boolean = false
    private var viewBinding: ViewDataBinding? = null

    protected var setTitle: String? = null
    protected lateinit var baseViewModel: ViewModel

    abstract fun layoutId(): Int
    /**
     *  在onCreate()的时候进行viewModel的创建和绑定生命周期，避免pager+fragment模式下多次调用
     *
     *  因为在ViewPager+Fragment模式下，切换fragment会多次执行onCreateView以后的生命周期
     *  所以viewModel的liveData新增多个Observer，造成更新数据时会多次执行observe中的逻辑导致错误
     * */
    abstract fun initWithCreate()

    abstract fun initWithViewCreate(savedInstanceState: Bundle?)
    abstract fun initWithResume()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this.javaClass.simpleName
        createViewModel()
        lifecycle.addObserver(baseViewModel)
        initWithCreate()
        Log.e(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView")
        val clazz =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != clazz && ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            viewBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            return viewBinding?.root
        }

        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")
        initWithViewCreate(savedInstanceState)
    }

    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tempClazz = tp as? Class<ViewModel> ?: BaseViewModel::class.java
            baseViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(tempClazz) as ViewModel
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        if (!loadingState && !isHidden) {
            loadingState = true
            initWithResume()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView")
        loadingState = false
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }

    fun onUpdateToolbar() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            actionBar.title = setTitle ?: "DemoMode"
        } ?: return
    }
}