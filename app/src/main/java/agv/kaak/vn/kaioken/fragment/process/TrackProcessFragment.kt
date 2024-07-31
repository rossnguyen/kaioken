package agv.kaak.vn.kaioken.fragment.process


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.OrderStatus
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_track_process.*

/**
 * A simple [Fragment] subclass.
 *
 */
class TrackProcessFragment : BaseFragment() {

    var status: Int = 0
    private var useType = UseType.LOCAL_HAVE_TABLE
    private var heightScroll: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_process, container, false)
    }

    override fun getData() {
        status = arguments!!.getInt(ProcessFragment.STATUS_SEND)
        useType = arguments!!.getInt(ProcessFragment.USE_TYPE_SEND)
    }

    override fun addEvent() {
        //nothing to do
    }

    override fun loadData() {
        updateStatus(status, "", "", "", "")
    }

    fun setUseType(useType: Int?) {
        this.useType = useType!!
        when (useType) {
            UseType.LOCAL_HAVE_TABLE -> layoutProcessLocal.visibility = View.VISIBLE
            UseType.BOOK -> layoutProcessLocal.visibility = View.VISIBLE
            UseType.DELIVERY -> layoutProcessDelivery.visibility = View.VISIBLE
            UseType.LOCAL_NO_TABLE -> layoutProcessLocal.visibility = View.VISIBLE
        }
    }

    fun updateStatus(status: Int, customerName: String, staffName: String, chefName: String, cashierName: String) {
        stepSendRequirement.setFinish(mContext, false)
        stepConfirm.setFinish(mContext, false)
        stepIsProcessing.setFinish(mContext, false)
        stepIsServing.setFinish(mContext, false)
        stepSendRequirePayment.setFinish(mContext, false)
        stepEnd.setFinish(mContext, false)
        when (status) {
            OrderStatus.SEND_ORDER -> stepSendRequirement.setFinish(mContext, true)

            OrderStatus.CONFIRM -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
            }

            OrderStatus.CHEF_START -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
                stepIsProcessing.setFinish(mContext, true)
            }

            OrderStatus.SERVING -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
                stepIsProcessing.setFinish(mContext, true)
                stepIsServing.setFinish(mContext, true)
            }

            OrderStatus.REQUIRE_PAYMENT -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
                stepIsProcessing.setFinish(mContext, true)
                stepIsServing.setFinish(mContext, true)
                stepSendRequirePayment.setFinish(mContext, true)
            }

            OrderStatus.FINISH -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
                stepIsProcessing.setFinish(mContext, true)
                stepIsServing.setFinish(mContext, true)
                stepSendRequirePayment.setFinish(mContext, true)
                stepEnd.setFinish(mContext, true)
            }

            OrderStatus.CANCEL -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
                stepIsProcessing.setFinish(mContext, true)
                stepIsServing.setFinish(mContext, true)
                stepSendRequirePayment.setFinish(mContext, true)
                stepEnd.setFinish(mContext, true)
            }

            OrderStatus.CHEF_FINISH -> {
                stepSendRequirement.setFinish(mContext, true)
                stepConfirm.setFinish(mContext, true)
                stepIsProcessing.setFinish(mContext, true)
                stepIsServing.setFinish(mContext, true)
            }
        }
        if (!customerName.isEmpty())
            stepSendRequirement.setSubText(customerName)
        else
            stepSendRequirement.setSubText("")

        if (!staffName.isEmpty())
            stepConfirm.setSubText(staffName)
        else
            stepConfirm.setSubText("")

        if (!chefName.isEmpty())
            stepIsProcessing.setSubText(chefName)
        else
            stepIsProcessing.setSubText("")

        stepIsServing.setSubText("")
        stepSendRequirePayment.setSubText("")

        //auto scroll
        scrollToPosition(status, 6)


        // for delivery
        stepSendRequirementDelivery.setFinish(mContext, false)
        stepConfirmDelivery.setFinish(mContext, false)
        stepIsProcessingDelivery.setFinish(mContext, false)
        stepIsShippingDelivery.setFinish(mContext, false)
        stepEndDelivery.setFinish(mContext, false)
        when (status) {
            OrderStatus.SEND_ORDER -> stepSendRequirementDelivery.setFinish(mContext, true)

            OrderStatus.CONFIRM -> {
                stepSendRequirementDelivery.setFinish(mContext, true)
                stepConfirmDelivery.setFinish(mContext, true)
            }

            OrderStatus.CHEF_START -> {
                stepSendRequirementDelivery.setFinish(mContext, true)
                stepConfirmDelivery.setFinish(mContext, true)
                stepIsProcessingDelivery.setFinish(mContext, true)
            }

            OrderStatus.CHEF_FINISH -> {
                stepSendRequirementDelivery.setFinish(mContext, true)
                stepConfirmDelivery.setFinish(mContext, true)
                stepIsProcessingDelivery.setFinish(mContext, true)
            }

            OrderStatus.SHIPPING -> {
                stepSendRequirementDelivery.setFinish(mContext, true)
                stepConfirmDelivery.setFinish(mContext, true)
                stepIsProcessingDelivery.setFinish(mContext, true)
                stepIsShippingDelivery.setFinish(mContext, true)
            }

            OrderStatus.FINISH -> {
                stepSendRequirementDelivery.setFinish(mContext, true)
                stepConfirmDelivery.setFinish(mContext, true)
                stepIsProcessingDelivery.setFinish(mContext, true)
                stepIsShippingDelivery.setFinish(mContext, true)
                stepEnd.setFinish(mContext, true)
            }

            OrderStatus.CANCEL -> {
                stepSendRequirementDelivery.setFinish(mContext, true)
                stepConfirmDelivery.setFinish(mContext, true)
                stepIsProcessingDelivery.setFinish(mContext, true)
                stepIsShippingDelivery.setFinish(mContext, true)
                stepEnd.setFinish(mContext, true)
            }


        }
        if (!customerName.isEmpty())
            stepSendRequirementDelivery.setSubText(customerName)
        else
            stepSendRequirementDelivery.setSubText("")

        if (!staffName.isEmpty())
            stepConfirmDelivery.setSubText(staffName)
        else
            stepConfirmDelivery.setSubText("")

        if (!chefName.isEmpty())
            stepIsProcessingDelivery.setSubText(chefName)
        else
            stepIsProcessingDelivery.setSubText("")

        stepIsShippingDelivery.setSubText("")

        //auto scroll
        scrollToPosition(status, 5)

    }

    private fun scrollToPosition(position: Int, size: Int) {
        heightScroll = layoutProcess.height
        scrollView.smoothScrollTo(0, heightScroll / size * (position - 1))
    }
}
