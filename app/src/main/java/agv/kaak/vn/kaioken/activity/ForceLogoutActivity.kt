package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_force_logout.*

class ForceLogoutActivity : AppCompatActivity() {

    companion object {
        val DEVICE_NAME="DEVICE_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_force_logout)
        setFinishOnTouchOutside(false)
        addEvent()
        loadContent()
    }

    fun addEvent(){
        btnOk.setOnClickListener {
            GlobalHelper.doLogout(this)
        }
    }

    fun loadContent(){
        val deviceName=intent.getStringExtra(DEVICE_NAME)
        tvMessage.text="\t\t\t${getString(R.string.force_logout_message_1)} [$deviceName].\n\n\t\t\t${getString(R.string.force_logout_message_2)}"
    }
}
