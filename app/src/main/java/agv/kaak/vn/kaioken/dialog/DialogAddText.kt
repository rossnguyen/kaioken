package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_add_text.*

class DialogAddText(val mContext: Activity,
                    private val title: String,
                    private val positiveTitle: String,
                    private val negativeTitle: String,
                    private val callBack: OnDialogAddTextCallBack) : Dialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_text)
        setupDialog()
        addEvent()
    }

    override fun onStart() {
        super.onStart()
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setupDialog() {
        if (title.isNotEmpty())
            tvTitle.text = title
        else
            tvTitle.visibility = View.GONE
        btnPositive.text = positiveTitle
        btnNegative.text = negativeTitle
    }

    private fun addEvent() {
        btnPositive.setOnClickListener {
            callBack.onPositiveClick(etContent.text.toString())
            dismiss()
        }

        btnNegative.setOnClickListener {
            callBack.onNegativeClick()
            dismiss()
        }
    }

    interface OnDialogAddTextCallBack {
        fun onPositiveClick(value: String)
        fun onNegativeClick()
    }
}