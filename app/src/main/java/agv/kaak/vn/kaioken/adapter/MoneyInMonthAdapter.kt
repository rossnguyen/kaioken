package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.SpentMoney
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_money_in_month.view.*

/**
 * Created by shakutara on 11/2/17.
 */
class MoneyInMonthAdapter() : RecyclerView.Adapter<MoneyInMonthAdapter.MyViewHolder>() {

    private var spentMoneyList = arrayListOf<SpentMoney>()
    private lateinit var context: Context

    constructor(context: Context, spentMoneyList: ArrayList<SpentMoney>) : this() {
        this.context = context
        this.spentMoneyList = spentMoneyList
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var money = spentMoneyList[position]
        holder!!.tvContent.text = money.content
        if (money.type.toInt() == Constraint.GET_MONEY || money.type.toInt() == Constraint.LOAN_MONEY)
            holder.tvPrice.text = context.getString(R.string.format_money_long, money.money)
        else
            holder.tvPrice.text = context.getString(R.string.format_money_long_with_negative_signal, money.money)
        holder.tvDate.text = money.date
    }

    override fun getItemCount(): Int {
        return spentMoneyList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_money_in_month, parent, false)

        return MyViewHolder(itemView)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvContent: TextView = view.tvContent
        var tvPrice: TextView = view.tvPrice
        var tvDate: TextView = view.tvDate
    }
}