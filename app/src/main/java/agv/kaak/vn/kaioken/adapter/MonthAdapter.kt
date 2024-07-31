package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_month.view.*

/**
 * Created by shakutara on 26/12/2017.
 */
class MonthAdapter(private val context: Context, private val monthList: ArrayList<String>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = (context as Activity).layoutInflater.inflate(R.layout.item_month, null)
        val tvMonth = view.tvMonth

        tvMonth.text = monthList[position]
        return view
    }

    override fun getItem(position: Int): Any = monthList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = monthList.size
}