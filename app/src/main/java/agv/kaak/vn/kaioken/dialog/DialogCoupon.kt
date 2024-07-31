package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_coupon.*

class DialogCoupon(val context:Activity,val onClickCheckListener: ClickCheckListener ):Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_coupon)
        btnCheck.setOnClickListener {
            if(etContent.text.isEmpty())
                GlobalHelper.showMessage(context,context.resources.getString(R.string.call_waiter_enter_content_error),true)
            else{
                onClickCheckListener.onClick(etContent.text.toString())
                dismiss()
            }
        }
    }

    interface ClickCheckListener{
        fun onClick(value:String)
    }
}