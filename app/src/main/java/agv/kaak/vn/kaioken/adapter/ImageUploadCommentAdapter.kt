package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList

class ImageUploadCommentAdapter(var context: Context,var arrayImage: ArrayList<String>) : RecyclerView.Adapter<ImageUploadCommentAdapter.ViewHolder>() {

    var onItemClickedListener: OnItemClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_upload_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = arrayImage[position]
        ImageHelper.loadImage(context,holder.imgImageUpload,path, PlaceHolderType.IMAGE)

        holder.ibtnDelete.setOnClickListener {
            if (onItemClickedListener != null)
                onItemClickedListener!!.onClickDelete(path, position)
        }

        holder.imgImageUpload.setOnClickListener {
            if (onItemClickedListener != null) {
                val bitmap = (holder.imgImageUpload.drawable as BitmapDrawable).bitmap
                onItemClickedListener!!.onClickImage(path, bitmap)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayImage.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var imgImageUpload: ImageView
        /*LinearLayout layoutMist;*/
        internal var ibtnDelete: ImageButton

        init {
            imgImageUpload = view.findViewById<View>(R.id.imgImageUpload) as ImageView
            /*layoutMist=(LinearLayout)view.findViewById(R.id.layoutMist);*/
            ibtnDelete = view.findViewById<View>(R.id.ibtnDelete) as ImageButton
        }
    }

    interface OnItemClickedListener {
        fun onClickDelete(path: String, position: Int)
        fun onClickImage(path: String, bitmap: Bitmap)
    }
}