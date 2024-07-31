package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.CheckVersionResult
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_require_update.*

class DialogRequireUpdate(val activityParent: Activity, val detailUpdate: CheckVersionResult, val callback: OnRequireUpdateResult) : Dialog(activityParent) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_require_update)
        addEvent()
        loadData()
    }

    private fun addEvent() {
        btnUpdateNow.setOnClickListener {
            //dismiss()
            callback.onUpdateClick()
        }

        btnUpdateNowCenter.setOnClickListener {
            //dismiss()
            callback.onUpdateClick()
        }

        btnLater.setOnClickListener {
            dismiss()
            callback.onSkipClick()
        }
    }

    private fun loadData() {
        if (detailUpdate.isRequired == 1) {
            btnLater.visibility = View.GONE
            btnUpdateNow.visibility = View.GONE
        } else {
            btnUpdateNowCenter.visibility = View.GONE
        }

        tvContent.text = detailUpdate.newFeatures
    }

    interface OnRequireUpdateResult {
        fun onUpdateClick()
        fun onSkipClick()
    }
}