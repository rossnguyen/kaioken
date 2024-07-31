package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_store_offer.view.*
import java.util.ArrayList

class StoreOfferAdapter(val context: Context, val sourceStore: ArrayList<Store>) : RecyclerView.Adapter<StoreOfferAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_offer, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storeData = sourceStore[position]

        ImageHelper.loadImage(context, holder.imgCover, storeData.image, PlaceHolderType.RESTAURANT)
        if (storeData.discount != null) {
            holder.tvDiscount.visibility = View.VISIBLE
            //holder.layoutReasonDiscount.visibility = View.VISIBLE

            if(storeData.discountType== DiscountType.PERCENT)
                holder.tvDiscount.text=context.resources.getString(R.string.format_x_percent,storeData.discount!!.toInt())
            else
                holder.tvDiscount.text=context.resources.getString(R.string.format_x_k,storeData.discount!!.toInt())

            //holder.tvReasonDiscount.text = storeData.reason
        } else {
            holder.tvDiscount.visibility = View.GONE
            //holder.layoutReasonDiscount.visibility = View.GONE
        }

        /*if (storeData.liked == 1)
            holder.imgLike.setImageResource(R.drawable.ic_heart_bold)
        else
            holder.imgLike.setImageResource(R.drawable.ic_heart)*/

        holder.tvName.text = storeData.name

        if (storeData.distance!! > 1)
            holder.tvDistance.text = "${String.format("%.1f", storeData.distance)} ${context.resources.getString(R.string.all_km)}"
        else
            holder.tvDistance.text = "${(storeData.distance!! * 1000).toInt()} ${context.resources.getString(R.string.all_m)}"

        holder.tvAddress.text = storeData.address

        holder.tvRating.text = "${String.format("%.1f", storeData.rating)}"

        //add event when user click item on list
        holder.layoutRoot.setOnClickListener {
            if (onStoreClickListener != null) {
                storeData.imageBitMap = (holder.imgCover.drawable as BitmapDrawable).bitmap
                onStoreClickListener!!.onClick(holder, storeData)
            }
        }

        holder.tvDiscount.setOnClickListener {
            if(onStoreClickListener!=null){
                onStoreClickListener!!.onDiscountClick(storeData.id,storeData.name!!, storeData.image)
            }
        }
    }

    override fun getItemCount(): Int {
        return sourceStore.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var layoutRoot = v.layoutRoot
        //internal var layoutDiscount = v.layoutDiscount
        //internal var layoutReasonDiscount = v.layoutReasonDiscount
        internal var tvDiscount = v.tvDiscount
        internal var tvName = v.tvName
        internal var tvDistance = v.tvDistance
        internal var tvAddress = v.tvAddress
        //internal var tvReasonDiscount = v.tvReasonDiscount
        internal var tvRating = v.tvRating
        internal var imgCover = v.imgCover
        //internal var imgLike = v.imgLike
    }

    interface OnStoreClickListener {
        fun onClick(holder: ViewHolder, storeData: Store)
        fun onDiscountClick(pageId:Int?, name:String, linkImage:String?)
    }

    var onStoreClickListener: OnStoreClickListener? = null
}