package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.News
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(val mContext: Context, val source: ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false), mContext)
    }

    override fun getItemCount(): Int {
        return source.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rootParams = holder.layoutRoot.layoutParams
        rootParams.width = Resources.getSystem().displayMetrics.widthPixels * 80 / 100
        holder.layoutRoot.requestLayout()

        val news = source[position]
        ImageHelper.loadImage(mContext, holder.imgAvatar, news.banner, PlaceHolderType.IMAGE)
//        ImageHelper.loadImage(mContext, holder.imgAvatar, "https://vnn-imgs-f.vgcloud.vn/2018/06/28/17/tu-vi-12-cung-hoang-dao-ngay-29-6.jpg", PlaceHolderType.IMAGE)
        holder.tvTitle.text = news.title
        holder.tvContent.text = news.description

        holder.layoutRoot.setOnClickListener {
            if (onItemClickListener != null)
                onItemClickListener!!.onClick(news.news_id!!, news.title!!)
        }

    }

    class ViewHolder(view: View, val childContext: Context) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val imgAvatar = view.imgAvatar
        val tvTitle = view.tvTitle
        val tvContent = view.tvContent

        fun isFocus(focus: Boolean) {
            if (focus) {
                val layoutParams = layoutRoot.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(
                        0,
                        0,
                        childContext.resources.getDimensionPixelOffset(R.dimen.all_margin_short),
                        0)
                layoutRoot.requestLayout()
            } else {
                val layoutParams = layoutRoot.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(
                        0,
                        childContext.resources.getDimensionPixelOffset(R.dimen.all_margin_short),
                        childContext.resources.getDimensionPixelOffset(R.dimen.all_margin_short),
                        childContext.resources.getDimensionPixelOffset(R.dimen.all_margin_short))
                layoutRoot.requestLayout()
            }
        }
    }


    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(newsId: Int, title: String)
    }
}