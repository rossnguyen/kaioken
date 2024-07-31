package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.MessageDialog
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.activity_choose_share.*
import com.zing.zalo.zalosdk.oauth.OpenAPIService
import com.zing.zalo.zalosdk.oauth.FeedData

class ChooseShareActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_choose_share)
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.setFinishOnTouchOutside(true)

        callbackManager = CallbackManager.Factory.create()

        val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.kaioken.vn"))
                .setShareHashtag(ShareHashtag.Builder()
                        .setHashtag("#KaiokenApp")
                        .build())
                .build()

        ibtnFacebook.setOnClickListener {
            val shareDialog = ShareDialog(this)
            shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
                override fun onSuccess(result: Sharer.Result?) {
                    finish()
                }

                override fun onCancel() {
                    finish()
                }

                override fun onError(error: FacebookException?) {
                    GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
                    Log.e("${Constraint.TAG_LOG}ShareFacebook", error?.message)
                    finish()
                }
            })

            if (ShareDialog.canShow(ShareLinkContent::class.java))
                shareDialog.show(content)
        }

        ibtnMessenger.setOnClickListener {
            MessageDialog.show(this, content)
        }

        ibtnZalo.setOnClickListener {

            val feed = FeedData()
            feed.msg = "Hello Phuc dep trai"
            feed.link = "https://news.zing.vn"
            feed.linkTitle = "Zing News"
            feed.linkSource = "https://news.zing.vn"
            feed.linkThumb = arrayOf("http://sanctum-inle-resort.com/wp-content/uploads/2015/11/Sanctum_Inle_Resort_Myanmar_Flower_Macro_Cherry_Blossom.jpg")
            /*OpenAPIService.getInstance().shareFeed(this, feed,object :ZaloPluginCallback{
                override fun onResult(p0: Boolean, p1: Int, p2: String?, p3: String?) {
                    Log.d("${Constraint.TAG_LOG}Share", "$p2\n$p3")
                }
            })*/

            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_feature_is_developing), true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
