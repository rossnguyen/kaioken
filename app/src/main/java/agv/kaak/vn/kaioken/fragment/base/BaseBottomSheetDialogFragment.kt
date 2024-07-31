package agv.kaak.vn.kaioken.fragment.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    lateinit var mContext: Context
    lateinit var activityParent: Activity

    private lateinit var mBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        addEvent()
        loadData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        activityParent = context as Activity
    }

    override fun onStart() {
        super.onStart()
        mBehavior = BottomSheetBehavior.from(view!!.parent as View)
        if (dialogIsExpand())
            mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    abstract fun dialogIsExpand(): Boolean
    abstract fun getData()
    abstract fun addEvent()
    abstract fun loadData()
}