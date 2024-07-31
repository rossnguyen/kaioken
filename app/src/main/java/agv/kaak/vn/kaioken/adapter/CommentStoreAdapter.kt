package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.GetStoreCommentResult
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_store_comment.view.*

class CommentStoreAdapter(val context: Context, val sourceImage: ArrayList<GetStoreCommentResult>) : RecyclerView.Adapter<CommentStoreAdapter.ViewHolder>(), View.OnClickListener {
    private val POSITION_COMMENT = 0
    private val POSITION_IMAGE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return sourceImage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = sourceImage[position]
        ImageHelper.loadImage(context, holder.imgAvartar, data.userAvatar, PlaceHolderType.AVATAR)
        holder?.tvName?.text = data.userName
        holder?.tvTime?.text = data.dateAdd
        holder?.tvContent?.text = data.content

        when (data.listImage.size) {
            0 -> holder?.layoutImage?.visibility = View.GONE
            1 -> {
                holder?.layout1Image?.visibility = View.VISIBLE
                ImageHelper.loadImage(context, holder?.img1_1, data.listImage[0].url, PlaceHolderType.IMAGE)

                holder?.img1_1?.tag = intArrayOf(position, 0)
                holder?.img1_1?.setOnClickListener(this)
            }
            2 -> {
                holder?.layout2Image?.visibility = View.VISIBLE

                ImageHelper.loadImage(context, holder?.img2_1, data.listImage[0].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img2_2, data.listImage[1].url, PlaceHolderType.IMAGE)

                holder?.img2_1?.tag = intArrayOf(position, 0)
                holder?.img2_2?.tag = intArrayOf(position, 1)
                holder?.img2_1?.setOnClickListener(this)
                holder?.img2_2?.setOnClickListener(this)
            }
            3 -> {
                holder?.layout3Image?.visibility = View.VISIBLE

                ImageHelper.loadImage(context, holder?.img3_1, data.listImage[0].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img3_2, data.listImage[1].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img3_3, data.listImage[2].url, PlaceHolderType.IMAGE)

                holder?.img3_1?.tag = intArrayOf(position, 0)
                holder?.img3_2?.tag = intArrayOf(position, 1)
                holder?.img3_3?.tag = intArrayOf(position, 2)
                holder?.img3_1?.setOnClickListener(this)
                holder?.img3_2?.setOnClickListener(this)
                holder?.img3_3?.setOnClickListener(this)
            }
            4 -> {
                holder?.layout4Image?.visibility = View.VISIBLE

                ImageHelper.loadImage(context, holder?.img4_1, data.listImage[0].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img4_2, data.listImage[1].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img4_3, data.listImage[2].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img4_4, data.listImage[3].url, PlaceHolderType.IMAGE)

                holder?.img4_1?.tag = intArrayOf(position, 0)
                holder?.img4_2?.tag = intArrayOf(position, 1)
                holder?.img4_3?.tag = intArrayOf(position, 2)
                holder?.img4_4?.tag = intArrayOf(position, 3)

                holder?.img4_1?.setOnClickListener(this)
                holder?.img4_2?.setOnClickListener(this)
                holder?.img4_3?.setOnClickListener(this)
                holder?.img4_4?.setOnClickListener(this)
            }
            5 -> {
                holder?.layout5Image?.visibility = View.VISIBLE

                ImageHelper.loadImage(context, holder?.img5_1, data.listImage[0].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img5_2, data.listImage[1].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img5_3, data.listImage[2].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img5_4, data.listImage[3].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img5_5, data.listImage[4].url, PlaceHolderType.IMAGE)

                holder?.img5_1?.tag = intArrayOf(position, 0)
                holder?.img5_2?.tag = intArrayOf(position, 1)
                holder?.img5_3?.tag = intArrayOf(position, 2)
                holder?.img5_4?.tag = intArrayOf(position, 3)
                holder?.img5_5?.tag = intArrayOf(position, 4)

                holder?.img5_1?.setOnClickListener(this)
                holder?.img5_2?.setOnClickListener(this)
                holder?.img5_3?.setOnClickListener(this)
                holder?.img5_4?.setOnClickListener(this)
                holder?.img5_5?.setOnClickListener(this)
            }
            else -> {
                holder?.layout6Image?.visibility = View.VISIBLE

                ImageHelper.loadImage(context, holder?.img6_1, data.listImage[0].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img6_2, data.listImage[1].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img6_3, data.listImage[2].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img6_4, data.listImage[3].url, PlaceHolderType.IMAGE)
                ImageHelper.loadImage(context, holder?.img6_5, data.listImage[4].url, PlaceHolderType.IMAGE)

                holder?.img6_1?.tag = intArrayOf(position, 0)
                holder?.img6_2?.tag = intArrayOf(position, 1)
                holder?.img6_3?.tag = intArrayOf(position, 2)
                holder?.img6_4?.tag = intArrayOf(position, 3)
                //holder?.img6_5.setTag(new int[]{position,4});
                holder?.tvNumberMoreImage?.tag = intArrayOf(position, 4)

                holder?.img6_1?.setOnClickListener(this)
                holder?.img6_2?.setOnClickListener(this)
                holder?.img6_3?.setOnClickListener(this)
                holder?.img6_4?.setOnClickListener(this)
                holder?.tvNumberMoreImage?.setOnClickListener(this)

                holder?.tvNumberMoreImage?.text = "+${data.listImage.size - 5}"
            }
        }
    }

    override fun onClick(v: View?) {
        val postition = v!!.getTag() as IntArray
        val positionComment = postition[POSITION_COMMENT]
        val positionImage = postition[POSITION_IMAGE]
        //init listImage only link
        val sizeListImage = sourceImage.get(postition[POSITION_COMMENT]).listImage.size
        val srcImage = java.util.ArrayList<String>()
        for (i in 0 until sizeListImage)
            srcImage.add(sourceImage.get(postition[POSITION_COMMENT]).listImage.get(i).url)

        if (onImageClicked != null)
            onImageClicked!!.onClick(srcImage, positionComment, positionImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layoutRoot: LinearLayout
        var imgAvartar: CircleImageView
        var tvName: TextView
        var tvTime: TextView
        var tvContent: TextView
        var layoutImage: FrameLayout
        var layout1Image: LinearLayout
        var layout2Image: LinearLayout
        var layout3Image: LinearLayout
        var layout4Image: LinearLayout
        var layout5Image: LinearLayout
        var layout6Image: LinearLayout
        var img1_1: ImageView
        var img2_1: ImageView
        var img2_2: ImageView
        var img3_1: ImageView
        var img3_2: ImageView
        var img3_3: ImageView
        var img4_1: ImageView
        var img4_2: ImageView
        var img4_3: ImageView
        var img4_4: ImageView
        var img5_1: ImageView
        var img5_2: ImageView
        var img5_3: ImageView
        var img5_4: ImageView
        var img5_5: ImageView
        var img6_1: ImageView
        var img6_2: ImageView
        var img6_3: ImageView
        var img6_4: ImageView
        var img6_5: ImageView
        var tvNumberMoreImage: TextView

        init {
            layoutRoot = view.layoutRoot
            imgAvartar = view.imgAvatar
            tvName = view.tvName
            tvTime = view.tvTime
            tvContent = view.tvContent
            layoutImage = view.layoutImage
            layout1Image = view.layout1Image
            layout2Image = view.layout2Image
            layout3Image = view.layout3Image
            layout4Image = view.layout4Image
            layout5Image = view.layout5Image
            layout6Image = view.layout6Image
            img1_1 = view.img1_1
            img2_1 = view.img2_1
            img2_2 = view.img2_2
            img3_1 = view.img3_1
            img3_2 = view.img3_2
            img3_3 = view.img3_3
            img4_1 = view.img4_1
            img4_2 = view.img4_2
            img4_3 = view.img4_3
            img4_4 = view.img4_4
            img5_1 = view.img5_1
            img5_2 = view.img5_2
            img5_3 = view.img5_3
            img5_4 = view.img5_4
            img5_5 = view.img5_5
            img6_1 = view.img6_1
            img6_2 = view.img6_2
            img6_3 = view.img6_3
            img6_4 = view.img6_4
            img6_5 = view.img6_5
            tvNumberMoreImage = view.tvNumberMoreImage
        }
    }

    interface OnImageClicked {
        fun onClick(sourceImgae: java.util.ArrayList<String>, positionComment: Int, positionImage: Int)
    }

    var onImageClicked: OnImageClicked? = null
}