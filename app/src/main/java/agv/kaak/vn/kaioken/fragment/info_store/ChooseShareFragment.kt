package agv.kaak.vn.kaioken.fragment.info_store

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.MessageDialog
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.model_bottom_sheet_choose_share.*

class ChooseShareFragment : BottomSheetDialogFragment() {
    lateinit var callbackManager: CallbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.model_bottom_sheet_choose_share, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(context?.resources?.getString(R.string.about_website)))
                .setShareHashtag(ShareHashtag.Builder()
                        .setHashtag("#KaiokenApp")
                        .build())
                .build()

        btnShareFacebook.setOnClickListener {
            /*if (onButtonShareClickListener != null)
                onButtonShareClickListener!!.onClickFacebook()*/

            val shareDialog = ShareDialog(this)
            shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
                override fun onSuccess(result: Sharer.Result?) {
                    GlobalHelper.showMessage(context,context?.resources?.getString(R.string.detail_store_share_success),true)
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                    GlobalHelper.showMessage(context, context?.resources?.getString(R.string.detail_store_share_fail), true)
                    Log.e("${Constraint.TAG_LOG}ShareFacebook", error?.message)
                }
            })
            if (ShareDialog.canShow(ShareLinkContent::class.java))
                shareDialog.show(content)

            dismiss()
        }

        btnShareMessenger.setOnClickListener {
            /*if (onButtonShareClickListener != null)
                onButtonShareClickListener!!.onClickMessenger()*/
            MessageDialog.show(this, content)
            dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}