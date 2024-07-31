package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.payment.PaymentFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {
    var orderId:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        addEvent()
        getData()
        loadData()
    }

    fun getData(){
        orderId=intent.getIntExtra(Constraint.ORDER_ID_SEND,-1)
    }

    fun loadData(){
        val paymentFragment=PaymentFragment()
        val bundle=Bundle()
        bundle.putInt(Constraint.ORDER_ID_SEND,orderId)
        paymentFragment.arguments=bundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.layoutContent,paymentFragment).commit()
    }

    fun addEvent(){
        toolbar.setOnClickListener {
            onBackPressed()
        }
    }

}
