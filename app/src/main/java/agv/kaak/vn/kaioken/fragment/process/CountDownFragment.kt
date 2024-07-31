package agv.kaak.vn.kaioken.fragment.process


import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.fragment_count_down.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class CountDownFragment : BaseFragment() {
    private var countDownTimer: CountDownTimer?=null
    var forceFinish = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_count_down, container, false)
    }

    override fun onPause() {
        super.onPause()
        offProcessingClock()
    }

    override fun getData(){
        //nothing to do
    }

    override fun loadData(){
        //nothing to do
    }

    override fun addEvent(){
        //nothing to do
    }

    fun updateRealTimeForProcess(dateChefConfirm: Date, minutesProcess: Int) {
        process.max = minutesProcess
        if (countDownTimer == null) {
            val now = Calendar.getInstance().time
            //get duration
            // from confirm to now
            val miliSecondsDistance = Math.abs(now.time - dateChefConfirm.time)
            //get time remaining
            val miliSecondsRemaining = minutesProcess * 60 * 1000 - miliSecondsDistance
            if (miliSecondsRemaining > 0) {
                val minutes = (miliSecondsRemaining / 1000 / 60).toInt()

                //lấy mấy giây dư ra chạy trước
                process.bottomText = activityParent.resources.getString(R.string.format_time_x_seconds, (miliSecondsRemaining / 1000 % minutes))
                process.progress = minutes + 1
                countDownTimer = object : CountDownTimer(miliSecondsRemaining, 1000) {
                    override fun onTick(l: Long) {
                        try {
                            var second = Integer.parseInt(process.bottomText.split(" ")[0])
                            if (second > 0) {
                                second--
                                process.bottomText = activityParent.resources.getString(R.string.format_time_x_seconds, second)
                            } else {
                                //if 0 minute 0 second: finish countdown
                                if (process.progress == 0)
                                    this.onFinish()
                                else {
                                    process.bottomText =activityParent.resources.getString(R.string.format_time_x_seconds, 59)
                                    var minutes = process.progress
                                    minutes--
                                    process.progress = minutes
                                }
                            }
                        } catch (ex: Exception) {
                        }

                    }

                    override fun onFinish() {
                        process.progress = 0
                        process.bottomText = activityParent.resources.getString(R.string.all_finish)
                        //if fish cause end time (not cause pause): ring the bell
                        if (!forceFinish)
                            GlobalHelper.ringTheBell(activityParent)
                    }
                }
                forceFinish = false
                countDownTimer!!.start()
            } else {
                process.progress = 0
                process.bottomText = activityParent.resources.getString(R.string.all_finish)
            }
        }
    }

    fun updateSumMoney(sumMoney:Double){
        tvSumBill.setupValue("${activityParent.resources.getString(R.string.all_final_money)}: ${sumMoney.toInt()}")
    }

    private fun offProcessingClock() {
        if (countDownTimer != null) {
            forceFinish = true
            countDownTimer!!.onFinish()
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }
}
