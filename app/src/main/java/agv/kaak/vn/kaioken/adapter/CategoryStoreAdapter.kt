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
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_category_store.view.*
import java.util.ArrayList

class CategoryStoreAdapter(val context: Context, val sourceCategory: ArrayList<CategoryStoreResult>) : RecyclerView.Adapter<CategoryStoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_store, parent, false))
    }

    override fun getItemCount(): Int {
        return sourceCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = sourceCategory[position]
        holder.tvName.text = data.name
        ImageHelper.loadImage(context, holder.imgIcon, data.icon, PlaceHolderType.CATEGORY)

        if (data.id == SearchFragment.categoryId)
            holder.layoutRoot.background = context.getDrawable(R.drawable.custom_background_5_radius_transparent_border_orange)
        else
            holder.layoutRoot.setBackgroundResource(0)

        holder.layoutRoot.setOnClickListener {
            if (onItemCategoryStoreClickListener != null)
                onItemCategoryStoreClickListener!!.onClick(position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName = view.tvName
        var imgIcon = view.imgIcon
        var layoutRoot = view.layoutRoot
    }

    var onItemCategoryStoreClickListener: OnItemCategoryStoreClickListener? = null

    interface OnItemCategoryStoreClickListener {
        fun onClick(position: Int)
    }
}