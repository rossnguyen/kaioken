package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.ToppingForSocket
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_topping.view.*
import kotlin.math.roundToInt

class ToppingAdapter(val mContext: Context, val sourceTopping: ArrayList<MenuFood>) : RecyclerView.Adapter<ToppingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_topping, parent, false))
    }

    override fun getItemCount() = sourceTopping.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topping = sourceTopping[position]
        holder.apply {
            tvPriceTopping.text = mContext.resources.getString(R.string.format_x_k, topping.price.roundToInt())
            ckbNameTopping.text = topping.name

            ckbNameTopping.setOnCheckedChangeListener { buttonView, isChecked ->
                onToppingCheckedChange?.onChange(isChecked, topping)
            }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvPriceTopping = view.tvPriceTopping
        val ckbNameTopping = view.ckbNameTopping

    }


    var onToppingCheckedChange: OnToppingCheckedChange? = null

    interface OnToppingCheckedChange {
        fun onChange(isChecked: Boolean, topping: MenuFood)
    }
}