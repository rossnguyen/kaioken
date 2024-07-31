package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.CategoryStoreResult
import agv.kaak.vn.kaioken.fragment.home.SearchFragment
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_category_store_1.view.*

class CategoryStoreAdapter1(val mContext: Context, val source: ArrayList<CategoryStoreResult>) : RecyclerView.Adapter<CategoryStoreAdapter1.ViewHolder>() {

    var onItemClickListener: OnItemCategoryStoreClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category_store_1, parent, false))
    }

    override fun getItemCount(): Int {
        return source.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = source[position]
        ImageHelper.loadImage(mContext, holder.imgCategory, category.icon, PlaceHolderType.CATEGORY)
        holder.tvCategoryName.text = category.name
        if (SearchFragment.categoryId == category.id)
            holder.layoutRoot.background = mContext.getDrawable(R.drawable.custom_background_0_radius_masterial_1_no_border)
        else
            holder.layoutRoot.setBackgroundResource(0)
        holder.layoutRoot.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onClick(category.id, category.name, category.icon)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val imgCategory = view.imgCategory
        val tvCategoryName = view.tvCategoryName
    }

    interface OnItemCategoryStoreClickListener {
        fun onClick(id: Int, name: String?, linkImage: String?)
    }
}