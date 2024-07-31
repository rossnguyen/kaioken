package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.location.Location
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_store_on_map.view.*
import android.graphics.drawable.BitmapDrawable
import agv.kaak.vn.kaioken.fragment.home.MapFragment

class StoreOnMapAdapter(val context: Context, val sourceStore: ArrayList<Store>) : RecyclerView.Adapter<StoreOnMapAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_store_on_map, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = sourceStore[position]
        //holder.imgAvatar.setImageBitmap(data.getImage());
        ImageHelper.loadImage(context,holder.imgAvatar,data.coverSmall, PlaceHolderType.RESTAURANT)
        /*data.imageBitMap = Glide.with(context).asBitmap()
                .load(data.image!!).into(-1, -1).get()
        holder.imgAvatar.setImageBitmap(data.imageBitMap)*/

        if (data.discount == null)
            holder.layoutDiscount.visibility = View.GONE
        else {
            if(data.discountType==DiscountType.PERCENT)
                holder.tvDiscount.text=context.resources.getString(R.string.format_x_percent,data.discount!!.toInt())
            else
                holder.tvDiscount.text=context.resources.getString(R.string.format_x_k,data.discount!!.toInt())
            holder.layoutDiscount.visibility = View.VISIBLE
        }
        holder.tvName.text = data.name

        var distance: FloatArray = FloatArray(1)
        if(MapFragment.locationSearch!=null){
            Location.distanceBetween(
                    MapFragment.locationSearch!!.latitude,
                    MapFragment.locationSearch!!.longitude,
                    data.location!!.latitude,
                    data.location!!.longitude, distance)
        }

        if (distance[0] > 0) {
            if (distance[0] > 1000)
                holder.tvDistance.text = "${String.format("%.1f", distance[0] / 1000)} ${context.resources.getString(R.string.all_km)}"
            else
                holder.tvDistance.text = "${distance[0].toInt()} ${context.resources.getString(R.string.all_m)}"
        } else
            holder.tvDistance.text = "${data.distance!!.toInt()}"


        holder.tvRating.setText("${String.format("%.1f",data.rating)}")

        holder.imgAvatar.setOnClickListener {
            if (onItemClickedListener != null){
                data.imageBitMap=(holder.imgAvatar.drawable as BitmapDrawable).bitmap
                onItemClickedListener!!.onClick(holder, data)
            }
        }
    }

    override fun getItemCount(): Int {
        return sourceStore.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var layoutDiscount = view.layoutDiscount
        internal var imgAvatar = view.imgAvatar
        internal var tvDiscount = view.tvDiscount
        internal var tvName = view.tvName
        internal var tvDistance = view.tvDistance
        internal var tvRating = view.tvRating
    }

    interface OnItemClickedListener {
        fun onClick(holder:ViewHolder, store: Store)
    }

    var onItemClickedListener: OnItemClickedListener? = null
}