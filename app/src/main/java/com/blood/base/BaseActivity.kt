package com.blood.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.blood.ui.dialog.LoadingDialog
import com.blood.utils.AdsMediationUtils
import com.blood.utils.LanguageUtils
import com.blood.utils.PrefUtils
import com.ironsource.mediationsdk.IronSource
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseActivity<VM : BaseViewModel, VB : ViewBinding> @Inject constructor(
    @LayoutRes val layoutId: Int, private val mViewModelClass: Class<VM>
) : DaggerAppCompatActivity(), IBaseUI {

    var isOnResume: Boolean = false
    private var loadingDialog: LoadingDialog? = null

    @Inject
    lateinit var prefUtils: PrefUtils

    val isFinished: Boolean
        get() {
            if (isDestroyed || isFinishing) {
                return true
            }
            return false
        }

    lateinit var binding: VB

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory)[mViewModelClass]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        LanguageUtils.loadLocale(this, prefUtils.defaultLanguage)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId) as VB
        setContentView(binding.root)
        AdsMediationUtils.IronSourceUtils.init(this)
    }

    override fun showLoading(content: String) {
        if (!isFinished && (loadingDialog == null || loadingDialog?.dialog?.isShowing == false)) {
            loadingDialog = LoadingDialog()
            loadingDialog?.show(supportFragmentManager, "loading")
        }
    }

    override fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    @Suppress("DEPRECATION")
    override fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    override fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    override fun hideSystemNavigationBar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                run {
                    val windowInsetController =
                        ViewCompat.getWindowInsetsController(window.decorView)
                    if (windowInsetController != null) {
                        windowInsetController.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        windowInsetController.hide(WindowInsetsCompat.Type.navigationBars())
                        if (window.decorView.rootWindowInsets != null) {
                            window.decorView.rootWindowInsets.getInsetsIgnoringVisibility(
                                WindowInsetsCompat.Type.navigationBars()
                            )
                        }
                        window.setDecorFitsSystemWindows(true)
                    }
                }
            } else {
                window.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemNavigationBar()
    }

    override fun onPause() {
        super.onPause()
        isOnResume = false
        IronSource.onPause(this)
    }

    override fun onResume() {
        super.onResume()
        isOnResume = true
        IronSource.onResume(this)
    }
}