package com.blood.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.blood.App
import com.blood.ui.dialog.PopupExit
import com.blood.utils.LanguageUtils
import com.blood.utils.NavigationUtils.safeNavigateAction
import com.blood.utils.NavigationUtils.safeNavigationUp
import com.blood.utils.NavigationUtils.safePopBackStack
import com.blood.utils.PrefUtils
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import dagger.android.AndroidInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> @Inject constructor(
    @LayoutRes val layout: Int, viewModelClass: Class<VM>
) : DaggerFragment(), IBaseUI {

    private val TAG = this::class.java.name

    private var iBase: IBaseUI? = null
    var adsUtils = App.adsUtils

    @Inject
    lateinit var prefUtils: PrefUtils

    open lateinit var binding: DB

    open fun backPressedWithExitPopup(): Boolean = false
    open fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.fragment, this)
        binding.setVariable(BR.prefUtils, prefUtils)
    }

    open fun initView() {}

    open fun initData() {}

    open fun initAds() {}

    open fun loadingText(): String = getString(R.string.loading)

    open fun initListener() {
        viewModel.isLoading.observe(this.viewLifecycleOwner) {
            if (it) {
                showLoading(loadingText())
            } else {
                hideLoading()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    open fun onInject() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(activity)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        LanguageUtils.loadLocale(requireContext(), prefUtils.defaultLanguage)
        init(inflater, container!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
        initView()
        initData()
        initListener()

        if (backPressedWithExitPopup()) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        showPopupExit()
                    }
                })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            iBase = context
        }
    }

    fun safeNav(action: NavDirections) {
        safeNavigateAction(action)
    }

    fun safeBackNav() {
        safeNavigationUp()
    }

    fun safePopBackStackNav(id: Int, inclusive: Boolean, saveState: Boolean) {
        safePopBackStack(id, inclusive, saveState)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        hideSystemNavigationBar()
    }

    open fun refresh() {}

    open fun navigate(action: Int) {
        view?.let { _view ->
            Navigation.findNavController(_view).navigate(action)
        }
    }

    open fun goBack() {
        safeBackNav()
    }

    override fun showLoading(content: String) {
        iBase?.showLoading(content)
    }

    override fun hideLoading() {
        iBase?.hideLoading()
    }

    override fun isNetworkConnected(): Boolean {
        return iBase?.isNetworkConnected() ?: false
    }

    override fun hideSystemNavigationBar() {
        iBase?.hideSystemNavigationBar()
    }

    override fun toast(msg: String) {
        iBase?.toast(msg)
    }

    fun showPopupExit() {
        PopupExit().show(childFragmentManager, PopupExit::class.java.simpleName)
    }
}