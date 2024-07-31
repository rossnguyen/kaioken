package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.process.ProcessFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import java.util.concurrent.TimeUnit

class ProcessActivity : BaseActivity() {
    var orderId: Int? = null
    var fragmentProcess: ProcessFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process)
        getData()
        loadData()
    }

    fun getData() {
        orderId = intent.getIntExtra(Constraint.ORDER_ID_SEND, -1)
    }

    fun loadData() {
        fragmentProcess = ProcessFragment()
        val bundle = Bundle()
        bundle.putInt(Constraint.ORDER_ID_SEND, orderId!!)
        fragmentProcess!!.arguments = bundle

        supportFragmentManager.beginTransaction()
                .replace(R.id.layoutContent, fragmentProcess)
                .commit()
    }

    override fun onBackPressed() {
        if (ProcessFragment.dialogIsShowing)
            super.onBackPressed()
        else
            startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }
}
