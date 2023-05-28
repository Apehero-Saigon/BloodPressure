package com.blood.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.blood.utils.PrefUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class BaseViewModel @Inject internal constructor() : ViewModel(), LifecycleObserver,
    CoroutineScope {

    val isLoading = MutableLiveData<Boolean>().apply { value = false }

    @Suppress("unused")
    protected var viewModelJob = Job()

    @Suppress("unused")
    protected val ioContext = viewModelJob + Dispatchers.IO // background context

    @Suppress("unused")
    protected val uiContext = viewModelJob + Dispatchers.Main // ui context

    protected val ioScope = CoroutineScope(ioContext)

    @Suppress("unused")
    protected val uiScope = CoroutineScope(uiContext)

    @Inject
    lateinit var prefUtils: PrefUtils

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private val mLaunchManager: MutableList<Job> = mutableListOf()

    protected fun launchOnUITryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        cacheBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
        handleCancellationExceptionManually: Boolean = true
    ) {
        launchOnUI {
            tryCatch(tryBlock, cacheBlock, {
                finallyBlock?.invoke(this)
            }, handleCancellationExceptionManually)
        }
    }

    protected fun launchOnUITryCatchWithLoading(
        tryBlock: suspend CoroutineScope.() -> Unit,
        cacheBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
        handleCancellationExceptionManually: Boolean = true
    ) {
        launchOnUI {
            isLoading.postValue(true)
            tryCatch({
                tryBlock.invoke(this)
                isLoading.postValue(false)
            }, {
                isLoading.postValue(false)
                cacheBlock.invoke(this, it)
            }, {
                finallyBlock?.invoke(this)
            }, handleCancellationExceptionManually)
        }
    }

    /**
     * add launch task to [mLaunchManager]
     */
    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        val job = launch { block() }
        mLaunchManager.add(job)
        job.invokeOnCompletion { mLaunchManager.remove(job) }

    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        try {
            coroutineScope { tryBlock() }
        } catch (e: Throwable) {
            if (e !is CancellationException || handleCancellationExceptionManually) {
                coroutineScope { catchBlock(e) }
            } else {
                throw e
            }
        } finally {
            coroutineScope { finallyBlock() }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.i(BaseViewModel::class.java.simpleName, "onDestroy")
        clearLaunchTask()
    }

    private fun clearLaunchTask() {
        mLaunchManager.clear()
    }
}