package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.OrderStatus
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.entity.result.TrackOrder
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_summary_order.view.*
import java.util.*

class TrackListOrderingAdapter(val context: Context, var listOrdering: ArrayList<TrackOrder>) : RecyclerView.Adapter<TrackListOrderingAdapter.ViewHolder>() {
    var onButtonClickListener: OnButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.item_summary_order, parent, false))
    }

    override fun getItemCount(): Int {
        return listOrdering.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detailOrder = listOrdering[position]
        //bind data
        ImageHelper.loadImage(context, holder.imgAvatarStore, detailOrder.imageStore, PlaceHolderType.RESTAURANT)
        holder.tvNameStore.text = detailOrder.pageName
        holder.tvAddressStore.text = detailOrder.pageAddress
        holder.tvSumMoney.text = "${context.resources.getString(R.string.payment_sum_bill)}: ${ConvertHelper.doubleToMoney(context, detailOrder.priceAfter!!)}"
        //holder.stepView.go(position,false)


        when (detailOrder.orderType) {
            UseType.LOCAL_HAVE_TABLE -> setupViewOnLocal(holder, detailOrder)
            UseType.BOOK -> setupViewOnBook(holder, detailOrder)
            UseType.DELIVERY -> setupViewOnDelivery(holder, detailOrder)
        }

        holder.tvDetailStore.setOnClickListener {
            if (onButtonClickListener != null) {
                detailOrder.imageBitmap = (holder.imgAvatarStore.drawable as BitmapDrawable).bitmap
                onButtonClickListener!!.onSeeDetailStoreClick(holder, detailOrder)
            }

        }

        holder.btnSeeDetailOrder.setOnClickListener {
            if (onButtonClickListener != null)
                onButtonClickListener!!.onSeeDetailOrderClick(detailOrder.pageId!!, detailOrder.pageName!!, detailOrder.orderId!!)
        }

        holder.btnCallWaiter.setOnClickListener {
            if (onButtonClickListener != null)
                onButtonClickListener!!.onCallWaiterClick(detailOrder.pageId!!, detailOrder.orderId!!)
        }

        holder.btnCallToStore.setOnClickListener {
            holder.stepView.go(holder.stepView.currentStep + 1, true)
            if (onButtonClickListener != null)
                onButtonClickListener!!.onCallToStoreClick(detailOrder.phone)
        }

        holder.btnSeeShiper.setOnClickListener {
            if (onButtonClickListener != null)
                onButtonClickListener!!.onSeeSniperClick(detailOrder.orderId!!)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun setupViewOnLocal(holder: ViewHolder, trackOrder: TrackOrder) {
        when (trackOrder.statusId) {
            OrderStatus.SEND_ORDER -> holder.stepView.go(0, false)
            OrderStatus.CONFIRM -> holder.stepView.go(1, false)
            OrderStatus.CHEF_START -> holder.stepView.go(2, false)
            OrderStatus.CHEF_FINISH -> holder.stepView.go(3, false)
            OrderStatus.SERVING -> holder.stepView.go(3, false)
            OrderStatus.REQUIRE_PAYMENT -> holder.stepView.go(4, false)
        }

        holder.tvTypeUse.text = context.resources.getString(R.string.track_order_type_use_local)
        holder.tvTypeUse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eating, 0, 0, 0)
        holder.btnCallToStore.visibility = View.GONE
        holder.btnSeeShiper.visibility = View.GONE
        holder.stepViewDelivery.visibility = View.GONE
        holder.stepView.visibility = View.VISIBLE
    }

    private fun setupViewOnBook(holder: ViewHolder, trackOrder: TrackOrder) {
        when (trackOrder.statusId) {
            OrderStatus.SEND_ORDER -> holder.stepView.go(0, false)
            OrderStatus.CONFIRM -> holder.stepView.go(1, false)
            OrderStatus.CHEF_START -> holder.stepView.go(2, false)
            OrderStatus.CHEF_FINISH -> holder.stepView.go(3, false)
            OrderStatus.SERVING -> holder.stepView.go(3, false)
            OrderStatus.REQUIRE_PAYMENT -> holder.stepView.go(4, false)
        }

        if (trackOrder.orderTableTimeArrive != null) {
            val timeArrival = ConvertHelper.stringGlobalToDateTimeWithoutTimezone(trackOrder.orderTableTimeArrive!!)
            holder.tvTypeUse.text = context.resources.getString(R.string.track_order_type_use_book, timeArrival!!.hours, timeArrival.minutes, timeArrival.date, (timeArrival.month + 1))
        } else
            holder.tvTypeUse.text = context.resources.getString(R.string.track_order_type_use_book_unknow)
        holder.tvTypeUse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_booked, 0, 0, 0)
        holder.btnCallWaiter.visibility = View.GONE
        holder.btnSeeShiper.visibility = View.GONE
        holder.stepViewDelivery.visibility = View.GONE
        holder.stepView.visibility = View.VISIBLE
    }

    private fun setupViewOnDelivery(holder: ViewHolder, trackOrder: TrackOrder) {
        when (trackOrder.statusId) {
            OrderStatus.SEND_ORDER -> holder.stepViewDelivery.go(0, false)
            OrderStatus.CONFIRM -> holder.stepViewDelivery.go(1, false)
            OrderStatus.CHEF_START -> holder.stepViewDelivery.go(2, false)
            OrderStatus.CHEF_FINISH -> holder.stepViewDelivery.go(2, false)
            OrderStatus.SHIPPING -> holder.stepViewDelivery.go(3, false)
        }

        if (trackOrder.deliveryTimeArrive != null) {
            val timeArrival = ConvertHelper.stringGlobalToDateTimeWithoutTimezone(trackOrder.deliveryTimeArrive!!)
            val now = Calendar.getInstance()
            if (timeArrival?.year == now.time.year && timeArrival.month == now.time.month && timeArrival.date == now.time.date)
                holder.tvTypeUse.text= context.resources.getString(R.string.track_order_type_use_delivery_now, timeArrival.hours, timeArrival.minutes)
            else
                holder.tvTypeUse.text = context.resources.getString(R.string.track_order_type_use_delivery, timeArrival!!.hours, timeArrival.minutes, timeArrival.date, (timeArrival.month + 1))
        } else
            holder.tvTypeUse.text = context.resources.getString(R.string.track_order_type_use_delivery_unknow)

        holder.tvTypeUse.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delivery, 0, 0, 0)
        holder.btnCallWaiter.visibility = View.GONE
        holder.btnCallToStore.visibility = View.GONE
        holder.stepViewDelivery.visibility = View.VISIBLE
        holder.stepView.visibility = View.GONE
    }

    class ViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatarStore = view.imgAvatarStore
        val tvNameStore = view.tvNameStore
        val tvAddressStore = view.tvAddressStore
        val tvTypeUse = view.tvTypeUse
        val tvDetailStore = view.tvDetailStore
        val stepView = view.stepView
        val stepViewDelivery = view.stepViewDelivery
        val btnSeeDetailOrder = view.btnSeeDetailOrder
        val btnCallWaiter = view.btnCallWaiter
        val btnSeeShiper = view.btnSeeShiper
        val btnCallToStore = view.btnCallToStore
        val tvSumMoney = view.tvSumMoney

        init {
            stepView.state
                    // You should specify only stepsNumber or steps array of strings.
                    // In case you specify both steps array is chosen.
                    .steps(context.resources.getStringArray(R.array.step_order_local).toMutableList())
                    .stepsNumber(5)
                    .animationDuration(1000)
                    .commit()

            stepViewDelivery.state
                    .steps(context.resources.getStringArray(R.array.step_order_delivery).toMutableList())
                    .stepsNumber(4)
                    .animationDuration(1000)
                    .commit()
        }

        fun updateStep(step: Int) {
            if (step < 5) {
                if (stepView.visibility == View.VISIBLE)
                    stepView.go(step, true)
                else
                    stepViewDelivery.go(step, true)
            } else
                stepView.go(step, true)

        }
    }

    interface OnButtonClickListener {
        fun onSeeDetailStoreClick(holder: ViewHolder, trackOrder: TrackOrder)
        fun onSeeDetailOrderClick(storeId: Int, storeName: String, orderId: Int)
        fun onCallWaiterClick(storeId: Int, orderId: Int)
        fun onCallToStoreClick(phoneNumberStore: String?)
        fun onSeeSniperClick(orderId: Int)
    }
}