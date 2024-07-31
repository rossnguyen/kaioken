package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken._interface.OnItemCheckedChangeListener
import agv.kaak.vn.kaioken._interface.OnLongClickListener
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.TrackOrder
import agv.kaak.vn.kaioken.fragment.home.NotificationFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.utils.DateUtils
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_ordered_order.view.*

class TrackListOrderedAdapter(val context: Context, var listOrdered: ArrayList<TrackOrder>)
    : RecyclerView.Adapter<TrackListOrderedAdapter.ViewHolder>() {
    private var allowChooseItem = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ordered_order, parent, false))
    }

    override fun getItemCount(): Int {
        return listOrdered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detailOrder = listOrdered[position]
        ImageHelper.loadImage(context, holder.imgAvatarStore, detailOrder.imageStore, PlaceHolderType.RESTAURANT)

        holder.tvNameStore.text = detailOrder.pageName
        holder.tvAddress.text = detailOrder.pageAddress
        if (detailOrder.dateComplete != null)
            holder.tvDateTime.text = DateUtils.getDistanceTimeTMP7(context, ConvertHelper.stringGlobalToDateTimeWithoutTimezone(detailOrder.dateComplete))
        holder.tvSumMoney.text = "${context.resources.getString(R.string.payment_sum_bill)}: ${ConvertHelper.doubleToMoney(context, detailOrder.priceAfter!!)}"

        holder.btnRate.isEnabled = detailOrder.isRate == 0

        holder.tvDetailStore.setOnClickListener {
            if (onButtonClick != null) {
                detailOrder.imageBitmap = (holder.imgAvatarStore.drawable as BitmapDrawable).bitmap
                onButtonClick!!.onClickDetailStore(holder, detailOrder)
            }
        }

        holder.btnRate.setOnClickListener {
            if (onButtonClick != null)
                onButtonClick!!.onClickRate(detailOrder.pageId, detailOrder.pageName, detailOrder.orderId, position)
            holder.btnRate.isEnabled = false
        }

        holder.btnSeeInvoice.setOnClickListener {
            if (onButtonClick != null)
                onButtonClick!!.onClickInvoiceDetail(detailOrder.invoiceNo)
        }

        holder.layoutRoot.setOnLongClickListener {
            if (onItemLongClickListener != null) {
                onItemLongClickListener!!.onLongClick(position,NotificationFragment.FOR_HISTORY)
                holder.ckbChoosed.isChecked = true
            }

            return@setOnLongClickListener true
        }

        holder.ckbChoosed.setOnCheckedChangeListener { buttonView, isChecked ->
            detailOrder.isChecked = isChecked
            if (onItemCheckedChangeListener != null)
                onItemCheckedChangeListener!!.onChange(isChecked)
        }

        if (allowChooseItem) {
            holder.layoutRoot.setOnClickListener {
                holder.ckbChoosed.performClick()
            }
            holder.layoutRoot.isClickable = true

            holder.ckbChoosed.visibility = View.VISIBLE
            val paramSeeInvoice = holder.btnSeeInvoice.layoutParams as RelativeLayout.LayoutParams
            paramSeeInvoice.removeRule(RelativeLayout.ALIGN_PARENT_END)
            holder.btnSeeInvoice.layoutParams = paramSeeInvoice
        } else {
            holder.layoutRoot.isClickable = false
            holder.ckbChoosed.visibility = View.GONE
            val paramSeeInvoice = holder.btnSeeInvoice.layoutParams as RelativeLayout.LayoutParams
            paramSeeInvoice.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            holder.btnSeeInvoice.layoutParams = paramSeeInvoice
        }


        holder.ckbChoosed.isChecked = detailOrder.isChecked
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val imgAvatarStore = view.imgAvatarStore
        val tvDetailStore = view.tvDetailStore
        val tvNameStore = view.tvNameStore
        val tvAddress = view.tvAddressStore
        val tvSumMoney = view.tvSumMoney
        val tvDateTime = view.tvDateTime
        val btnRate = view.btnRate
        val ckbChoosed = view.ckbChoosed
        val btnSeeInvoice = view.btnSeeInvoice
    }

    fun allowChooseItem(allow: Boolean) {
        this.allowChooseItem = allow
    }

    var onButtonClick: OnItemClickListener? = null
    var onItemLongClickListener: OnLongClickListener? = null
    var onItemCheckedChangeListener: OnItemCheckedChangeListener? = null

    interface OnItemClickListener {
        fun onClickDetailStore(holder: ViewHolder, trackOrder: TrackOrder)
        fun onClickInvoiceDetail(invoiceNo: String?)
        fun onClickRate(storeId: Int?, storeName: String?, orderId: Int?, index: Int)
        fun onClickDelete(orderId: Int, index: Int)
    }
}