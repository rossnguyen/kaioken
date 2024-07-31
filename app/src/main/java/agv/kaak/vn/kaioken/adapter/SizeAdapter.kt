package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.SizeFood
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_size.view.*
import kotlin.math.roundToInt

class SizeAdapter(val mContext: Context, val sourceSize: ArrayList<SizeFood>) : RecyclerView.Adapter<SizeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_size, parent, false))
    }

    override fun getItemCount() = sourceSize.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size = sourceSize[position]
        holder.radSize.text = size.sizeName
        holder.tvSizePrice.text = ConvertHelper.doubleToMoney(mContext,size.price)
        holder.tvSizePriceAfterPromotion.text =ConvertHelper.doubleToMoney(mContext,size.priceAfterPromotion)

        if (size.price != size.priceAfterPromotion) {
            holder.tvSizePrice.visibility = View.VISIBLE
            holder.tvSizePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        /* if (position == 0)
             holder.radSize.isChecked = true*/

        holder.radSize.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                onSizeCheckedChange?.onChange(position, size)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val radSize = view.radSize
        val tvSizePrice = view.tvSizePrice
        val tvSizePriceAfterPromotion = view.tvSizePriceAfterPromotion

        fun unCheck() {
            radSize.isChecked = false
        }

        fun checked() {
            radSize.isChecked = true
        }
    }

    var onSizeCheckedChange: OnSizeCheckedChange? = null

    interface OnSizeCheckedChange {
        fun onChange(positionChecked: Int, sizeInfo: SizeFood)
    }
}