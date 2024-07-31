package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.response.KiInfo
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_ki_of_user.view.*

class KiOfUserAdapter(val context: Context, val listKi: ArrayList<KiInfo>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_ki_of_user, parent, false)
            holder = ViewHolder(view)
            view.setTag(holder)
        } else
            holder = view.tag as ViewHolder

        val data = listKi[position]
        ImageHelper.loadImage(context, holder.imgStore, data.linkPageImage, PlaceHolderType.IMAGE)
        holder.tvName.text = data.pageName
        holder.tvValue.text = "${data.kiPoint!!.toInt()}"
        return view!!
    }

    override fun getItem(position: Int): Any {
        return listKi[position]
    }

    override fun getItemId(position: Int): Long {
        return listKi[position].id!!.toLong()
    }

    override fun getCount(): Int {
        return listKi.size
    }

    class ViewHolder(view: View) {
        var imgStore = view.imgStore
        var tvName = view.tvName
        var tvValue = view.tvValue
    }
}