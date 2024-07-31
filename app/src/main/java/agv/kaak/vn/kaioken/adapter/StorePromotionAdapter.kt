package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.StorePromotion
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_store_promotion.view.*

class StorePromotionAdapter(val mContext: Context, val promotions: ArrayList<StorePromotion>) : RecyclerView.Adapter<StorePromotionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_store_promotion, parent, false))
    }

    override fun getItemCount(): Int {
        return promotions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promotion = promotions[position]
        ViewCompat.setElevation(holder?.imgIcon,mContext.resources.getDimensionPixelOffset(R.dimen.all_masterial_elevation_card_pickup).toFloat())

        holder?.tvTime?.text = Html.fromHtml("${mContext.resources.getString(R.string.detail_store_see_detail_promotion_time_from)} " +
                "<b>${ConvertHelper.reverseDate(promotion.dateStart!!.split(" ")[0], "-")}</b> " +
                "${mContext.resources.getString(R.string.detail_store_see_detail_promotion_time_to)} " +
                "<b>${ConvertHelper.reverseDate(promotion.dateEnd!!.split(" ")[0], "-")}</b")

        holder?.tvDiscountValue?.text = Html.fromHtml("${mContext.resources.getString(R.string.detail_store_see_detail_promotion_discount_value)} " +
                "<b>${if (promotion.discountType == DiscountType.PERCENT) "${promotion.discountValue}%" else "${promotion.discountValue}K"}</b>")
        holder?.tvDescription?.text = promotion.description
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTime = view.tvTime
        val tvDiscountValue = view.tvDiscountValue
        val tvDescription = view.tvDescription
        val imgIcon = view.imgIcon
    }
}