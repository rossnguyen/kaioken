package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.fragment.home.ScanQRFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

class DialogShowScan :BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_empty,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen)
    }

    override fun getData() {
        //nothing to do
    }

    override fun loadData() {
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        //add ScanQR Fragment
        val scanQRFragment=ScanQRFragment()
        scanQRFragment.onBackButtonPressListener=View.OnClickListener {
            dismiss()
        }
        val bundle=Bundle()
        bundle.putParcelable(Constraint.DATA_SEND,Constraint.myLocation)
        scanQRFragment.arguments=bundle

        mFragmentManager.beginTransaction().replace(R.id.layoutContent,scanQRFragment).commit()
    }

    override fun addEvent() {
        //nothing to do
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

}