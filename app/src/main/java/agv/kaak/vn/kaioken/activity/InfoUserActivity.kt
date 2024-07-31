package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_info_user.*

class InfoUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_user)

        layoutBack.setOnClickListener{
            onBackPressed()
        }
    }
}
