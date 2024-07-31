package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.OrderDetail
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_bill_order_items.view.*
import java.util.*

/**
 * Created by shakutara on 11/27/17.
 */
class BillOrderItemsAdapter(private var context: Context, private var orderDetailList: ArrayList<OrderDetail>) : RecyclerView.Adapter<BillOrderItemsAdapter.MyViewHolder>() {

    override fun getItemCount(): Int = orderDetailList.count()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder?.tvName?.text = orderDetailList[position].name
        if (orderDetailList[position].itemQuantity!! > 0)
            holder?.tvQuantity?.text = "${context.getString(R.string.format_money_long, orderDetailList[position].priceAfter!! / orderDetailList[position].itemQuantity!!)} x${orderDetailList[position].itemQuantity}"
        holder?.tvPrice?.text = context.getString(R.string.format_money_long, orderDetailList[position].priceAfter!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_bill_order_items, parent, false)

        return MyViewHolder(itemView)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView = view.tvName
        var tvQuantity: TextView = view.tvQuantity
        var tvPrice: TextView = view.tvPrice
    }

}