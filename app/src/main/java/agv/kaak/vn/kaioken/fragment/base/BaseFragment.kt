package agv.kaak.vn.kaioken.fragment.base


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import agv.kaak.vn.kaioken.R
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
abstract class BaseFragment : Fragment() {

    lateinit var mContext: Context
    lateinit var activityParent: Activity
    lateinit var mFragmentManager: FragmentManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return TextView(activity).apply {
            setText(R.string.hello_blank_fragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addEvent()
        getData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        activityParent = context as Activity
        mFragmentManager = childFragmentManager
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun getWidthScreen(): Int {
        val displayMetric = DisplayMetrics()
        activityParent.windowManager.defaultDisplay.getMetrics(displayMetric)
        return displayMetric.widthPixels
    }

    fun getHeightScreen(): Int {
        val displayMetric = DisplayMetrics()
        activityParent.windowManager.defaultDisplay.getMetrics(displayMetric)
        return displayMetric.heightPixels
    }

    fun getDimenPixel(id: Int): Int {
        return activityParent.resources.getDimensionPixelOffset(id)
    }

    fun hideKeyboard(view: View?) {
        val imm = activityParent.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun showKeyboard() {
        val imm = activityParent.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun showDialogMessage(title:String?, message:String?){
        AlertDialog.Builder(activityParent)
                .setMessage(message)
                .setNegativeButton(resources.getString(R.string.all_close), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setTitle(title)
                .setCancelable(true)
                .show()
    }

    abstract fun getData()
    abstract fun loadData()
    abstract fun addEvent()
}
