package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_quick_call_waiter.view.*

class QuickCallWaiterAdapter(val context: Context, val listContent: Array<String>) : RecyclerView.Adapter<QuickCallWaiterAdapter.ViewHoler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoler {
        return QuickCallWaiterAdapter.ViewHoler(LayoutInflater.from(context).inflate(R.layout.item_quick_call_waiter, parent, false))
    }

    override fun getItemCount(): Int {
        return listContent.size
    }

    override fun onBindViewHolder(holder: ViewHoler, position: Int) {
        holder!!.content.text = listContent[position]
        holder.layoutRoot.setOnClickListener {
            if (position != (listContent.size - 1) && onItemClick != null)
                onItemClick!!.onCLick(listContent[position])
            else
                onItemClick!!.onClickMore()
        }
    }

    class ViewHoler(val view: View) : RecyclerView.ViewHolder(view) {
        val content = view.tvContent
        val layoutRoot = view.layoutRoot
    }

    interface OnItemClick {
        fun onCLick(value: String)
        fun onClickMore()
    }

    var onItemClick: OnItemClick? = null
}