package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.City
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter(val context:Context,val sourceCity:ArrayList<City>):RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_city,parent,false))
    }

    override fun getItemCount(): Int {
        return sourceCity.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city=sourceCity[position]
        holder!!.tvName.text=city.city

        holder.tvName.setOnClickListener {
            if(onItemClickListener!=null)
                onItemClickListener!!.onItemClick(position)
        }
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvName=view.tvName
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    var onItemClickListener:OnItemClickListener?=null
}