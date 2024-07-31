package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Context
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_food_on_tracking.view.*
import kotlin.math.roundToInt

class ShowListFoodOnTrackingAdapter(var context: Context, var listOrder: ArrayList<DetailOrder>) : RecyclerView.Adapter<ShowListFoodOnTrackingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_food_on_tracking, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachOrder = listOrder[position]
        ImageHelper.loadImage(context, holder.imgAvatar, eachOrder.linkImage, PlaceHolderType.FOOD)
        holder.tvName.text = eachOrder.nameFood

        //if chef cancel foodWithTopping, event status = 2
        if (eachOrder.eventStatus == 2)
            holder.tvNumber.text = context.resources.getString(R.string.order_process_chef_cancel)
        else
            holder.tvNumber.text = "x ${eachOrder.numberOrder}"


        //set note
        if (eachOrder.note != null && !eachOrder.note!!.isEmpty()) {
            holder.tvContentNote.visibility = View.VISIBLE
            holder.tvContentNote.text = eachOrder.note
        } else
            holder.tvContentNote.visibility = View.GONE

        //set author
        if (eachOrder.staffEvent == null) {
            if (eachOrder.customerName!!.substring(0, 1) == "+")
                holder.tvAuthor.text = "*****${eachOrder.customerName!!.substring((eachOrder.customerName!!.length - 4), (eachOrder.customerName!!.length))}"
            else
                holder.tvAuthor.text = eachOrder.customerName
        } else {
            holder.tvAuthor.text = context.resources.getString(R.string.format_staff_x, eachOrder.customerName)
            holder.tvAuthor.setTextColor(ContextCompat.getColor(context, R.color.colorRedPrimary))

        }

        if (eachOrder.getStringTopping().isEmpty()) {
            holder.tvTopping.visibility = View.GONE
            //set price
            if (eachOrder.numberOrder != 0) {
                holder.tvPriceUnitBefore.text = ConvertHelper.doubleToMoney(context, (eachOrder.priceBefore!! / eachOrder.numberOrder!!))
                holder.tvPriceUnitAfter.text = ConvertHelper.doubleToMoney(context, (eachOrder.priceAfter!! / eachOrder.numberOrder!!))
            } else {
                holder.tvPriceUnitBefore.text = ConvertHelper.doubleToMoney(context, eachOrder.priceBefore!!)
                holder.tvPriceUnitAfter.text = ConvertHelper.doubleToMoney(context, eachOrder.priceAfter!!)
            }

            if (eachOrder.priceBefore == eachOrder.priceAfter)
                holder.tvPriceUnitBefore.visibility = View.GONE

            holder.tvPrice.text = ConvertHelper.doubleToMoney(context, eachOrder.priceAfter!!)
        } else {
            holder.tvTopping.visibility = View.VISIBLE
            holder.tvTopping.text = eachOrder.getStringTopping()
            val priceTopping = eachOrder.totalPrice!! - eachOrder.priceBefore!!
            val totalPriceAfter = priceTopping + eachOrder.priceAfter!!
            val totalPriceBefore = priceTopping + eachOrder.priceBefore!!
            //set price
            if (eachOrder.numberOrder != 0) {
                holder.tvPriceUnitBefore.text = ConvertHelper.doubleToMoney(context, (totalPriceBefore / eachOrder.numberOrder!!))
                holder.tvPriceUnitAfter.text = ConvertHelper.doubleToMoney(context, (totalPriceAfter / eachOrder.numberOrder!!))
            } else {
                holder.tvPriceUnitBefore.text = ConvertHelper.doubleToMoney(context, totalPriceBefore)
                holder.tvPriceUnitAfter.text = ConvertHelper.doubleToMoney(context, totalPriceAfter)
            }

            if (eachOrder.priceAfter == eachOrder.priceBefore)
                holder.tvPriceUnitBefore.visibility = View.GONE

            holder.tvPrice.text = ConvertHelper.doubleToMoney(context, totalPriceAfter)
        }

        if (eachOrder.sizeName.isNullOrEmpty())
            holder.tvSizeName.visibility = View.GONE
        else {
            holder.tvSizeName.visibility = View.VISIBLE
            holder.tvSizeName.text = eachOrder.sizeName
        }

    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var imgAvatar = view.imgAvatar
        var tvName = view.tvName
        var tvPriceUnitBefore = view.tvPriceUnitBefore
        var tvPriceUnitAfter = view.tvPriceUnitAfter
        var tvPrice = view.tvPrice
        var tvContentNote = view.tvContentNote
        var tvAuthor = view.tvAuthor
        var tvNumber = view.tvNumber
        var tvSizeName = view.tvSizeName
        var tvTopping = view.tvTopping

        init {
            tvPriceUnitBefore.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}

