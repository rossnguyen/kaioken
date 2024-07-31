package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.order.OrderFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.os.Bundle

class OrderActivity : BaseActivity() {

    lateinit var orderFragment: OrderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        initComponent()
        addEvent()
        loadData()
    }

    private fun initComponent() {
        orderFragment = OrderFragment()
    }

    private fun loadData() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.layoutContent, orderFragment)
                .commitAllowingStateLoss()
    }

    private fun addEvent() {

    }

    override fun onBackPressed() {
        when (orderFragment.tagFragmentIsShowing) {
            orderFragment.FRAGMENT_CONFIRM -> orderFragment.setFragmentIsShowing(orderFragment.FRAGMENT_CHOOSE)
            orderFragment.FRAGMENT_INFO_BOOK -> orderFragment.setFragmentIsShowing(orderFragment.FRAGMENT_CHOOSE)
            orderFragment.FRAGMENT_INFO_DELIVERY -> orderFragment.setFragmentIsShowing(orderFragment.FRAGMENT_CHOOSE)
            else -> super.onBackPressed()
        }
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }


    override fun onConnectSocketFail() {
        //nothing to do
    }
}
