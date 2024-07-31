package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.result.Coupon
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_coupon_of_page.view.*

class ShowListCouponAdapter(val context: Context, val listCoupon: ArrayList<Coupon>) : RecyclerView.Adapter<ShowListCouponAdapter.ViewHolder>() {

    var onCouponChoosedListener:OnCouponChoosedListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coupon_of_page, parent, false))
    }

    override fun getItemCount(): Int {
        return listCoupon.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coupon = listCoupon[position]
        holder.tvCode.text = coupon.couponCode
        if (coupon.discountType == DiscountType.PERCENT)
            holder.tvValue.text = context.resources.getString(R.string.format_x_percent, coupon.discount)
        else
            holder.tvValue.text = context.resources.getString(R.string.format_x_k, coupon.discount)

        holder.layoutRoot.setOnClickListener {
            if(onCouponChoosedListener!=null)
                onCouponChoosedListener!!.onChoosed(coupon.discountType,coupon.discount, coupon.couponCode)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val tvCode = view.tvCode
        val tvValue = view.tvValue
    }

    interface OnCouponChoosedListener{
        fun onChoosed(type:Int, value:Int, code: String)
    }
}