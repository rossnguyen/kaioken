package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.result.Coupon
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_coupon.view.*

class CouponAdapter(val mContext: Context, val source: ArrayList<Coupon>) : RecyclerView.Adapter<CouponAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_coupon, parent, false))
    }

    override fun getItemCount(): Int {
        return source.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coupon = source[position]
        holder.tvNameStore.text = coupon.pageName
        if (coupon.discountType == DiscountType.PERCENT)
            holder.tvDiscount.text = mContext.resources.getString(R.string.format_x_percent, coupon.discount)
        else
            holder.tvDiscount.text = mContext.resources.getString(R.string.format_x_k, coupon.discount)
        holder.tvCode.text = mContext.resources.getString(R.string.format_coupon_code, coupon.couponCode)

        if (coupon.dateEnd != null && coupon.dateEnd!!.isNotEmpty()){
            val timeExp = (coupon.dateEnd!!.split(" ")[1]).substring(0, 5)
            val dateExp = ConvertHelper.reverseDate(coupon.dateEnd!!.split(" ")[0], "-")
            val stringExp = "$timeExp ngày $dateExp"
            holder.tvExp.text = mContext.resources.getString(R.string.format_coupon_exp, stringExp)
        }

        holder.tvDescription.text = coupon.description

        holder.layoutRoot.setOnClickListener {
            if (onCouponClickListener != null)
                onCouponClickListener!!.onClick(coupon.pageId, coupon.pageName!!)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val tvNameStore = view.tvNameStore
        val tvDiscount = view.tvDiscount
        val tvCode = view.tvCode
        val tvExp = view.tvExp
        val tvDescription = view.tvDescription
    }

    var onCouponClickListener: OnCouponClickListener? = null

    interface OnCouponClickListener {
        fun onClick(pageId: Int, pageName: String)
    }
}