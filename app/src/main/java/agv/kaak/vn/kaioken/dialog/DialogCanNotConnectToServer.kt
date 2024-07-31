package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_can_not_connect_to_service.*

class DialogCanNotConnectToServer(val activity:Activity) :Dialog(activity.applicationContext) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_can_not_connect_to_service)
        tvMessage.text="${context.resources.getString(R.string.dialog_cannot_connect_to_socket)}\n${context.resources.getString(R.string.sorry_message)}"
        btnOk.setOnClickListener{
            activity.finishAffinity()
        }
    }
}