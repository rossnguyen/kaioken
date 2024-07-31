package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import kotlinx.android.synthetic.main.dialog_change_password.*
import kotlinx.android.synthetic.main.item_input_text.view.*

class DialogChangePassword(val context: Activity, val onClickChangePasswordListener: CLickChangePasswordListener) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_change_password)
        tvOldPassword.etContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        tvNewPassword.etContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        btnCancel.setOnClickListener { dismiss() }
        btnSave.setOnClickListener {
            onClickChangePasswordListener.onClickSave(tvOldPassword.getContentText(),tvNewPassword.getContentText())
            dismiss()
        }
    }

    interface CLickChangePasswordListener{
        fun onClickSave(oldPassword:String, newPassword:String)
    }
}