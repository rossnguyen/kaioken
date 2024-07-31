package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet
import agv.kaak.vn.kaioken.utils.recyclerview.ItemTouchHelperAdapter
import kotlinx.android.synthetic.main.item_spent_money.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by shakutara on 11/27/17.
 */
class MoneyListAdapter(private var context: Context, private var moneyList: ArrayList<ItemWallet>) : RecyclerView.Adapter<MoneyListAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    private lateinit var callback: MoneyListAdapterCallback

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
    }

    override fun onItemDismiss(position: Int) {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogPrimary))
        } else {
            AlertDialog.Builder(context)
        }
        builder
                .setMessage(context.getString(R.string.all_do_you_want_to_delete_this_item))
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    callback.deleteMoneyItem(moneyList[position])

                    //  refresh adapter
                    moneyList.removeAt(position)
                    notifyDataSetChanged()
                })
                .setNegativeButton(R.string.all_no, { _, _ ->
                    notifyItemChanged(position)
                })
                .setOnCancelListener {
                    notifyItemChanged(position)
                }
                .show()
                .setCanceledOnTouchOutside(false)
    }

    override fun getItemCount(): Int = moneyList.count()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val itemWallet = moneyList[position]
        holder?.tvContentMoney?.text = itemWallet.title
        holder?.tvMoney?.text = itemWallet.amount

        val format = SimpleDateFormat(Constraint.DATE_FORMAT_TEXT_FULL)
        try {
            val date = format.parse(itemWallet.date)
            holder?.tvTime?.text = SimpleDateFormat(Constraint.DATE_FORMAT_FULL).format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (itemWallet.walletType == Constraint.WALLET_TYPE_SPEND || itemWallet.walletType == Constraint.WALLET_TYPE_DEBT) {
            holder?.tvMoney?.text = context.getString(R.string.format_money_long_with_negative_signal, Integer.parseInt(itemWallet.amount))
            holder?.tvMoney?.setTextColor(Color.RED)
        } else {
            holder?.tvMoney?.text = context.getString(R.string.format_money_long, Integer.parseInt(itemWallet.amount))
            holder?.tvMoney?.setTextColor(Color.BLUE)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_spent_money, parent, false)

        return MyViewHolder(itemView)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvContentMoney: TextView = view.tvContentMoney
        var tvMoney: TextView = view.tvMoney
        var tvTime: TextView = view.tvTime
        var viewBackground: RelativeLayout = view.view_background
        var viewForeground: LinearLayout = view.view_foreground
    }

//    ////////////////

    interface MoneyListAdapterCallback {
        fun deleteMoneyItem(itemWallet: ItemWallet)
    }

    fun getCallbackMoneyListAdapter(callback: MoneyListAdapterCallback) {
        this.callback = callback
    }

//    ////////////////

}