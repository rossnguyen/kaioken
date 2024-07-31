package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_show_image.view.*

class ShowImageStoreAdapter(val context: Context, val listImage: ArrayList<String>) : RecyclerView.Adapter<ShowImageStoreAdapter.ViewHolder>() {
    var onItemtemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_show_image_with_border, parent, false))
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //load image
        ImageHelper.loadImage(context, holder.img, listImage[position], PlaceHolderType.IMAGE)
        holder.img.setImageResource(R.drawable.place_holder_4)

        holder.img.setOnClickListener {
            if (onItemtemClickListener != null)
                onItemtemClickListener!!.onClick(position)
        }
        /*//set height
        val layoutParams = holder.img.layoutParams
        layoutParams.height = Resources.getSystem().displayMetrics.widthPixels / 3
        holder.img.layoutParams = layoutParams*/
    }


    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val img = view.img
    }
}