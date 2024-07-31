package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import kotlinx.android.synthetic.main.dialog_edit.*

class DialogEdit(val activity:Context,val oldValue:String, val onClickSaveListener:ClickSaveListener, val inputType:Int?) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit)
        etContent.setText(oldValue)
        if(inputType!=null){
            etContent.inputType= inputType
            etContent.setSelection(etContent.text.length)
        }
        btnCancel.setOnClickListener { dismiss() }
        btnSave.setOnClickListener{
            if(etContent.text.isEmpty()){
                GlobalHelper.showMessage(activity,activity.resources.getString(R.string.call_waiter_enter_content_error),true)
            }else{
                onClickSaveListener.onCLick(etContent.text.toString())
                dismiss()
            }
        }
    }

    interface ClickSaveListener{
        fun onCLick(newValue:String)
    }
}