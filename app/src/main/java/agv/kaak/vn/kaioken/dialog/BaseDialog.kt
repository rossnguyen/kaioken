package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_dialog_header_with_back_button.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by shakutara on 12/11/17.
 */
abstract class BaseDialog(context: Context) : Dialog(context) {

    fun showHeaderTitle(title: String?) {
        tvTitle.text = title
        tvBack.setOnClickListener {
            dismiss()
        }
    }

    fun showMessage(msg: String?) {
        GlobalHelper.showMessage(context,msg,true)
    }

    fun convertStringToDateShort(dateString: String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy")
        try {
            return format.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun convertDateToStringShort(myDate: Date): String? {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        try {
            return dateFormat.format(myDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun routeToNewActivity(activity: Class<*>) {
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }

    fun routeToNewActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(context, activity)
        intent.putExtra(Constraint.TAG_BUNDLE, bundle)
        context.startActivity(intent)
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard(view: View){
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    abstract fun showLoading()
    abstract fun hideLoading()
}