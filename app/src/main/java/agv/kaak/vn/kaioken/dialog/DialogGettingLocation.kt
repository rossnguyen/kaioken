package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import android.app.Activity
import android.app.Dialog
import android.os.Bundle

class DialogGettingLocation(context:Activity) :Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_getting_location)
    }
}