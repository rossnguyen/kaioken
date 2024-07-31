package agv.kaak.vn.kaioken.fragment.base

import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_dialog_header_with_back_button.*

abstract class BaseDialogFragment_ :DialogFragment(){

    fun showHeaderTitle(title: String?) {
        tvTitle.text = title
        tvBack.setOnClickListener {
            dismiss()
        }
    }

    fun showMessage(msg: String?) {
        GlobalHelper.showMessage(context, msg,true)
    }

    fun routeToNewActivity(activity: Class<*>) {
        val intent = Intent(getActivity(), activity)
        startActivity(intent)
    }

    fun routeToNewActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(getActivity(), activity)
        intent.putExtra(Constraint.TAG_BUNDLE, bundle)
        startActivity(intent)
    }

    abstract fun showLoading()
    abstract fun hideLoading()


}