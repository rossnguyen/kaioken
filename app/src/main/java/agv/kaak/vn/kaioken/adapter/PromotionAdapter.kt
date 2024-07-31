package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.Promotion
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_promotion.view.*

class PromotionAdapter(val mContext: Context, val source: ArrayList<Promotion>) : RecyclerView.Adapter<PromotionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_promotion, parent, false))
    }

    override fun getItemCount(): Int {
        return source.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHelper.loadImage(mContext, holder.img, source[position].banner, PlaceHolderType.IMAGE)
//        ImageHelper.loadImage(mContext, holder.img, "abc", PlaceHolderType.IMAGE)
        holder.img.setOnClickListener {
            if (onItemPromotionClick != null) {
                if (source[position].pageId != 0)
                    onItemPromotionClick!!.onPageClick(source[position].pageId)
                else
                    onItemPromotionClick!!.onCLick(source[position].news_id!!, source[position].title!!)

            }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val img = view.img
    }


    var onItemPromotionClick: OnItemPromotionClick? = null

    interface OnItemPromotionClick {
        fun onCLick(promotionId: Int, promotionTitle: String)
        fun onPageClick(pageId: Int?)
    }
}