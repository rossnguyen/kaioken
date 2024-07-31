package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_about)
        setFinishOnTouchOutside(true)

        val versionName = this.packageManager
                .getPackageInfo(packageName, 0)
                .versionName
        tvVersion.text=resources.getString(R.string.about_version,versionName)
        tvHotline.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", resources.getString(R.string.about_hotline), null))
            startActivity(intent)
        }
    }
}
