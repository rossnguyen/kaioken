package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.QuickCallWaiterAdapter
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.BaseSocketEmit
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.speech.RecognizerIntent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_call_waiter.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class CallWaiterActivity : AppCompatActivity() {
    private val REQ_CODE_SPEECH_INPUT = 100

    lateinit var progressDialog: ProgressDialog

    internal var baseSocketEmit = BaseSocketEmit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_call_waiter)
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.setFinishOnTouchOutside(true)
        progressDialog = GlobalHelper.createProgressDialogHandling(this@CallWaiterActivity, R.style.progressDialogPrimary)
        addEvent()
        initQuickCallWaiter()
    }

    override fun onResume() {
        super.onResume()
        Constraint.BASE_SOCKET?.on("callWaiters", onCallWaiterListerner)
    }

    override fun onPause() {
        super.onPause()
        Constraint.BASE_SOCKET?.off("callWaiters")
    }

    private fun addEvent() {
        btnConfirm.setOnClickListener(View.OnClickListener {
            val content = etContent.getText().toString()
            //check status socket before emit
            if (!GlobalHelper.networkIsConnected(this@CallWaiterActivity))
                return@OnClickListener
            //show dialog handling

            progressDialog.show()

            var alreadyEmitSocket = false

            object : CountDownTimer(Constraint.timeOutSocket, 1000L) {
                override fun onFinish() {
                    if (progressDialog.isShowing) {
                        GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
                        progressDialog.dismiss()
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    if (Constraint.BASE_SOCKET!!.connected() && !alreadyEmitSocket) {
                        baseSocketEmit.callWaiters(Constraint.ID_STORE_ORDERING,
                                Constraint.ID_TABLE_ORDERING,
                                content)
                        alreadyEmitSocket = true
                    }

                }
            }.start()

        })

        ibtnSpeech.setOnClickListener(View.OnClickListener {
            promptSpeechInput()
            etContent.visibility = View.VISIBLE
            btnConfirm.visibility = View.VISIBLE
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        })

        ibtnClose.setOnClickListener {
            finish()
        }
    }

    val onCallWaiterListerner = Emitter.Listener {
        this@CallWaiterActivity.runOnUiThread {
            //                    progressDialog.dismiss();
            Log.d("****CallWaiter", it[0].toString())
            progressDialog.dismiss()
            val jsonObjectResponse = it[0] as JSONObject
            try {
                val status = jsonObjectResponse.getInt("status")
                if (status == 1)
                    GlobalHelper.showMessage(applicationContext, resources.getString(R.string.call_waiter_success), true)
                else
                    GlobalHelper.showMessage(applicationContext, resources.getString(R.string.call_waiter_fail), true)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            finish()
        }
    }

    /**
     * Showing google speech input dialog
     */
    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, resources.getString(R.string.call_waiter_say_require))
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            GlobalHelper.showMessage(applicationContext,
                    resources.getString(R.string.call_waiter_not_detect_voice), true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    etContent.setText(result[0])
                }
            }
        }
    }

    private fun initQuickCallWaiter() {
        val source = resources.getStringArray(R.array.quick_call_waiter)
        /*val abc: ArrayList<String> = arrayListOf()
        abc.addAll(source)*/
        val adapter = QuickCallWaiterAdapter(applicationContext, source)
        adapter.onItemClick = object : QuickCallWaiterAdapter.OnItemClick {
            override fun onCLick(value: String) {
                if (value.trim().isEmpty())
                    return

                //check status socket before emit
                if (!GlobalHelper.networkIsConnected(this@CallWaiterActivity))
                    return
                //show dialog handling

                progressDialog.show()

                var alreadyEmitSocket = false

                object : CountDownTimer(10000L, 1000L) {
                    override fun onFinish() {
                        if (progressDialog.isShowing) {
                            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
                            progressDialog.dismiss()
                        }
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        if (Constraint.BASE_SOCKET!!.connected() && !alreadyEmitSocket) {
                            baseSocketEmit.callWaiters(Constraint.ID_STORE_ORDERING,
                                    Constraint.ID_TABLE_ORDERING,
                                    value)
                            alreadyEmitSocket = true
                        }
                    }
                }.start()
            }

            override fun onClickMore() {
                etContent.visibility = View.VISIBLE
                btnConfirm.visibility = View.VISIBLE
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstQuickCallWaiter.adapter = adapter
        lstQuickCallWaiter.layoutManager = layoutManager
    }
}
