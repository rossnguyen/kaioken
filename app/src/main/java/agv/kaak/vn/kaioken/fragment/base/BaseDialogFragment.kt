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
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
abstract class BaseDialogFragment : DialogFragment() {

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
        getData()
        addEvent()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        activityParent = context as Activity
        mFragmentManager = childFragmentManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //No call for super(). Bug on API Level > 11.
    }

    override fun onResume() {
        super.onResume()
        listenSocket()
        loadData()
    }

    override fun onPause() {
        super.onPause()
        offSocket()
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
    abstract fun listenSocket()
    abstract fun offSocket()
}
