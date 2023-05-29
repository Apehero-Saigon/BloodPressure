package com.blood.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.util.Consumer
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.textTrim
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class SaveBloodPressurePopup : DialogFragment() {

    var sys: Int = 0
    var dia: Int = 0
    var purse: Int = 0
    var note: String = ""
    var callBack: Consumer<String>? = null

    companion object {
        fun showPopup(
            fragmentManager: FragmentManager,
            sys: Int,
            dia: Int,
            pures: Int,
            note: String,
            callBack: Consumer<String>? = null
        ) {
            val popup = SaveBloodPressurePopup()
            popup.sys = sys
            popup.dia = dia
            popup.purse = pures
            popup.note = note
            popup.callBack = callBack
            popup.show(fragmentManager, SaveBloodPressurePopup::class.java.simpleName)
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
        return inflater.inflate(R.layout.dialog_save_blood_pressure, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvSys)?.text = sys.toString()
        view.findViewById<TextView>(R.id.tvDia)?.text = dia.toString()
        view.findViewById<TextView>(R.id.tvPurse)?.text = purse.toString()
        val edtNote = view.findViewById<EditText>(R.id.edtNote)
        edtNote.setText(note)

        view.findViewById<Button>(R.id.btnCancel)?.clickWithDebounce {
            dismiss()
        }
        view.findViewById<Button>(R.id.btnSave)?.clickWithDebounce {
            callBack?.accept(edtNote.textTrim())
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}