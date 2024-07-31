package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_money_limit.*

/**
 * Created by shakutara on 28/12/2017.
 */
class DialogMoneyLimit(context: Context) : Dialog(context), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnConfirm -> saveMoneyLimit()
        }
    }

    private fun saveMoneyLimit() {
        val money = edtMoneyLimit.getContentText()
        if (!money.isNullOrEmpty()) {
            callback.getMoneyLimit(money)
            dismiss()
        } else
            GlobalHelper.showMessage(context, context.getString(R.string.all_must_to_enter),true)
    }

    private lateinit var callback: DialogMoneyLimitCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_money_limit)

        layoutHeader.tvTitle.text=context.getString(R.string.finance_money_limit_in_month)

//        views click
        btnConfirm.setOnClickListener(this)
    }


    interface DialogMoneyLimitCallback {
        fun getMoneyLimit(money: String?)
    }

    fun getCallbackDialogMoneyLimit(callback: DialogMoneyLimitCallback) {
        this.callback = callback
    }
}