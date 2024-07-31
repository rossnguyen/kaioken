package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.MenuInfo
import agv.kaak.vn.kaioken.widget.GridSpacingDecoration
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import kotlinx.android.synthetic.main.item_category_menu_store.view.*

class CategoryMenuStoreAdapter(internal var context: Context, internal var listMenuInfo: List<MenuInfo>) : RecyclerView.Adapter<CategoryMenuStoreAdapter.ViewHolder>() {
    internal var onFoodClickListener: OnFoodClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_category_menu_store, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listMenuInfo[position]

        //display name category
        holder.tvName.text = data.categoryName

        //set auto expand first category
        if (position == 0) {
            holder.isExpand = true
            holder.lstFood.visibility = View.VISIBLE
            holder.ibtnExpand.setImageResource(R.drawable.ic_collapse)

            if (holder.adapter == null) {
                holder.adapter = MenuStoreAdapter(context, data.items!!)
                holder.adapter!!.setOnItemClickListener(object : MenuStoreAdapter.OnItemClickListener {
                    override fun onItemClick(food: MenuFood?) {
                        if (onFoodClickListener != null)
                            onFoodClickListener!!.onFoodClick(food!!)
                    }
                })
                val layoutManager = GridLayoutManager(context, 2)
                //layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                holder.lstFood.adapter = holder.adapter
                holder.lstFood.addItemDecoration(GridSpacingDecoration(context.resources.getDimensionPixelOffset(R.dimen.all_margin_long), 2))
                holder.lstFood.layoutManager = layoutManager
            }
        }

        //if have foodWithTopping, display foodWithTopping
        if (data.items != null) {
            holder.tvCount.text = " (${data.items!!.size})"
            holder.ibtnExpand.visibility = View.VISIBLE
            //add event to switch show/hide list foodWithTopping
            holder.layoutCategory.setOnClickListener {
                toogleMenu(holder, data)
            }

            holder.ibtnExpand.setOnClickListener {
                toogleMenu(holder, data)
            }

        }

    }

    private fun toogleMenu(holder: ViewHolder, data: MenuInfo) {
        holder.isExpand = !holder.isExpand
        //only load data when first
        if (holder.adapter == null) {
            holder.adapter = MenuStoreAdapter(context, data.items!!)
            holder.adapter!!.setOnItemClickListener(object : MenuStoreAdapter.OnItemClickListener {
                override fun onItemClick(food: MenuFood?) {
                    if (onFoodClickListener != null)
                        onFoodClickListener!!.onFoodClick(food!!)
                }
            })
            val layoutManager = GridLayoutManager(context, 2)
            //layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            holder.lstFood.adapter = holder.adapter
            holder.lstFood.addItemDecoration(GridSpacingDecoration(context.resources.getDimensionPixelOffset(R.dimen.all_margin_long), 2))
            holder.lstFood.layoutManager = layoutManager
        }
        //animation for state from expand to collapse
        val animationClockwise = AnimationUtils.loadAnimation(context, R.anim.rotate_180)
        animationClockwise.fillAfter = true
        //animation for state from collapse to expand
        val animationCounterClockwise = AnimationUtils.loadAnimation(context, R.anim.rotate_180_)
        animationCounterClockwise.fillAfter = true
        //show list foodWithTopping when state is expand
        if (holder.isExpand) {
            holder.ibtnExpand.startAnimation(animationCounterClockwise)
            holder.layoutFood.visibility = View.VISIBLE
            holder.ibtnExpand.setImageResource(R.drawable.ic_expand)
        } else {
            holder.ibtnExpand.startAnimation(animationClockwise)
            holder.layoutFood.visibility = View.GONE
            holder.ibtnExpand.setImageResource(R.drawable.ic_collapse)
        }//hide list foodWithTopping when state is collapse
    }

    override fun getItemCount(): Int {
        return listMenuInfo.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var layoutRoot: LinearLayout
        internal var layoutCategory: RelativeLayout
        internal var ibtnExpand: ImageButton
        internal var tvName: TextView
        internal var tvCount: TextView
        internal var layoutFood: FrameLayout
        internal var lstFood: RecyclerView

        internal var isExpand: Boolean = false
        internal var adapter: MenuStoreAdapter? = null

        init {
            layoutRoot = v.layoutRoot
            layoutCategory = v.layoutCategory
            ibtnExpand = v.ibtnExpand
            tvName = v.tvName
            tvCount = v.tvCount
            layoutFood = v.layoutFood
            lstFood = v.lstFood
            isExpand = false
            adapter = null
        }
    }

    interface OnFoodClickListener {
        fun onFoodClick(food: MenuFood)
    }

    fun setOnFoodClickListener(onFoodClickListener: OnFoodClickListener) {
        this.onFoodClickListener = onFoodClickListener
    }
}