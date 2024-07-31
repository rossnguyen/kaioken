package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.RateForStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.RateForStorePresenter
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_vote_after_payment.*

class DialogRatingAfterPayment(val mContext: Activity, val pageId: Int?, private val namePage: String?, val orderId: Int?) : Dialog(mContext), RateForStoreContract.View {
    var presenter: RateForStorePresenter = RateForStorePresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_vote_after_payment)
        loadData()
        addEvent()
    }

    fun loadData() {
        tvTitle.text = namePage
    }

    fun addEvent() {
        rbVote.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating == 0f) {
                rbVote.rating = 0.5f
            } else
                etScore.setText((rating * 2).toInt().toString())
        }

        btnConfirm.setOnClickListener {
            if (etScore.text.isEmpty())
                GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.dialog_vote_must_input_score), true)
            else {
                val score = Integer.parseInt(etScore.text.toString())
                val comment = etComment.text.toString()
                if (pageId == null || orderId == null)
                    GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), false)
                else if (GlobalHelper.networkIsConnected(mContext)) {
                    presenter.rateForStore(pageId, score, comment, orderId)
                    this.dismiss()
                }
            }
        }

        btnLater.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onRateSuccess() = GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_rating_success), true)

    override fun onRateFail(message: String?) {
        if (message == null || message.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, message, true)
    }
}