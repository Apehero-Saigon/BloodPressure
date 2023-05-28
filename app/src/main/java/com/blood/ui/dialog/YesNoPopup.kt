package com.blood.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.util.Consumer
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class YesNoPopup : DialogFragment() {

    var title: Int = 0
    var content: Int = 0
    var callBack: Consumer<Boolean>? = null

    companion object {
        fun showPopup(
            fragmentManager: FragmentManager,
            title: Int,
            content: Int,
            callBack: Consumer<Boolean>? = null
        ) {
            val popup = YesNoPopup()
            popup.title = title
            popup.content = content
            popup.callBack = callBack
            popup.show(fragmentManager, YesNoPopup::class.java.simpleName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_yes_no, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvTitle)?.setText(title)
        view.findViewById<TextView>(R.id.tvContent)?.setText(content)

        view.findViewById<Button>(R.id.btnCancel)?.clickWithDebounce {
            dismiss()
        }
        view.findViewById<Button>(R.id.btnYes)?.clickWithDebounce {
            dismiss()
            callBack?.accept(true)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}