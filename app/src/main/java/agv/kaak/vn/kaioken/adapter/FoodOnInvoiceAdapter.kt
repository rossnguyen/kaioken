package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_food_on_invoice.view.*

class FoodOnInvoiceAdapter(val context: Context, val listOrder: ArrayList<DetailOrder>) : RecyclerView.Adapter<FoodOnInvoiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_food_on_invoice, parent, false))
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = listOrder[position]
        holder.tvName?.text = order.nameFood
        holder.tvQuantity?.text = "${order.numberOrder}"
        if (order.numberOrder != 0)
            holder.tvUnitPrice?.text = ConvertHelper.doubleToMoney(context, (order.getPrice()/order.numberOrder!!))
        holder.tvPrice?.text = ConvertHelper.doubleToMoney(context, order.getPrice())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.tvName
        val tvQuantity = view.tvQuantity
        val tvUnitPrice = view.tvUnitPrice
        val tvPrice = view.tvPrice
    }
}