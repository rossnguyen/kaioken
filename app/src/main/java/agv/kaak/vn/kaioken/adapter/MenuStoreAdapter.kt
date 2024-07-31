package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_menu_store.view.*
import kotlin.math.roundToInt

class MenuStoreAdapter(internal var context: Context, internal var listFood: List<MenuFood>) : RecyclerView.Adapter<MenuStoreAdapter.ViewHolder>() {
    internal var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_menu_store, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ViewCompat.setElevation(holder.layoutRoot, context.resources.getDimension(R.dimen.all_masterial_elevation_fab_rest))
        val food = listFood[position]

        when (position % 4) {
            0 -> bindItem2(holder, food)
            1 -> bindItem1(holder, food)
            2 -> bindItem2(holder, food)
            3 -> bindItem1(holder, food)
        }

        holder.layoutRoot.setOnClickListener {
            if (onItemClickListener != null)
                onItemClickListener!!.onItemClick(food)
        }
    }

    private fun bindItem1(holder: ViewHolder, food: MenuFood) {
        holder.apply {
            layout_1.visibility = View.VISIBLE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.GONE

            ImageHelper.loadImage(context, imgAvatar_1, food.image, PlaceHolderType.FOOD)
            tvName_1.text = food.name
            tvPrice_1.text = context.resources.getString(R.string.format_x_k, food.price.roundToInt())
        }

    }

    private fun bindItem2(holder: ViewHolder, food: MenuFood) {
        holder.apply {
            layout_1.visibility = View.GONE
            layout_2.visibility = View.VISIBLE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.GONE

            ImageHelper.loadImage(context, imgAvatar_2, food.image, PlaceHolderType.FOOD)
            tvName_2.text = food.name
            tvPrice_2.text = context.resources.getString(R.string.format_x_k, food.price.roundToInt())
        }

    }

    private fun bindItem3(holder: ViewHolder, food: MenuFood) {
        holder.apply {
            layout_1.visibility = View.GONE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.VISIBLE
            layout_4.visibility = View.GONE

            ImageHelper.loadImage(context, imgAvatar_3, food.image, PlaceHolderType.FOOD)
            tvName_3.text = food.name
            tvPrice_3.text = context.resources.getString(R.string.format_x_k, food.price.roundToInt())
        }

    }

    private fun bindItem4(holder: ViewHolder, food: MenuFood) {
        holder.apply {
            layout_1.visibility = View.GONE
            layout_2.visibility = View.GONE
            layout_3.visibility = View.GONE
            layout_4.visibility = View.VISIBLE

            ImageHelper.loadImage(context, imgAvatar_4, food.image, PlaceHolderType.FOOD)
            tvName_4.text = food.name
            tvPrice_4.text = context.resources.getString(R.string.format_x_k, food.price.roundToInt())
        }

    }

    override fun getItemCount(): Int {
        return this.listFood.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val layoutRoot = v.layoutRootFood
        val layout_1 = v.layout_1
        val layout_2 = v.layout_2
        val layout_3 = v.layout_3
        val layout_4 = v.layout_4


        val imgAvatar_1 = v.imgAvatar_1
        val tvName_1 = v.tvNameFood_1
        val tvPrice_1 = v.tvPrice_1

        val imgAvatar_2 = v.imgAvatar_2
        val tvName_2 = v.tvNameFood_2
        val tvPrice_2 = v.tvPrice_2

        val imgAvatar_3 = v.imgAvatar_3
        val tvName_3 = v.tvNameFood_3
        val tvPrice_3 = v.tvPrice_3

        val imgAvatar_4 = v.imgAvatar_4
        val tvName_4 = v.tvNameFood_4
        val tvPrice_4 = v.tvPrice_4
    }

    interface OnItemClickListener {
        fun onItemClick(food: MenuFood?)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}