package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.custom_layout_list_thumbnail_food.view.*
import java.util.ArrayList

class ThumbnailFoodAdapter(private val context: Context, private val arraySource: ArrayList<String>, val position: Int) : RecyclerView.Adapter<ThumbnailFoodAdapter.ViewHolder>() {
    var tempPosition = 0
    var changeByClick = false
    var onItemClickListener: OnItemClickListener? = null

    init {
        tempPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_layout_list_thumbnail_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageHelper.loadImage(context,holder.imgThumbnail,arraySource[position], PlaceHolderType.IMAGE)
        if (position == tempPosition){
            holder.layoutBorder.isEnabled = true
            holder.layoutMist.visibility=View.GONE
        }
        else{
            holder.layoutBorder.isEnabled = false
            holder.layoutMist.visibility=View.VISIBLE
        }

        holder.imgThumbnail.setOnClickListener {
            //Mỗi lần click ta sẽ save lại vị trí hình được click
            //Gọi adapter.notifidatasetchange
            //Xét lúc bind lại dữ liệu, nếu tại vị trí dược lưu thì sẽ ẩn cái làm mờ đi
            //Biến changeByClick để biết người dùng chuyển ảnh bằng cách click hay là lướt ở trên
            //vì nếu click thì ở trên sẽ tự động lướt, và như thế sẽ xét sự kiện 2 lần
            tempPosition = position
            changeByClick = true
            if (onItemClickListener != null)
                onItemClickListener!!.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return arraySource.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgThumbnail=itemView.imgThumbnail
        var layoutMist=itemView.layoutMist
        var layoutBorder=itemView.layoutBorder
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}