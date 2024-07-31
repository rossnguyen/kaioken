package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_choose_number.*

class DialogChooseNumber(val mContext: Context,
                         val defaultValue: Int,
                         val callBack: OnNumberSelectedListener) : Dialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_choose_number)
        npChooseNumber.minValue = 1
        npChooseNumber.maxValue = 50
        npChooseNumber.value = defaultValue
        btnAccept.setOnClickListener {
            callBack.onNumberSelected(npChooseNumber.value)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    interface OnNumberSelectedListener {
        fun onNumberSelected(value: Int)
    }
}