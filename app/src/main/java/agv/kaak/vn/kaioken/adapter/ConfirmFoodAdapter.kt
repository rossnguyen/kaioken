package agv.kaak.vn.kaioken.adapter

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.item_confirm_food.view.*
import kotlin.math.roundToInt

class ConfirmFoodAdapter(val mContext: Context, val listOrder: ArrayList<DetailOrder>) : RecyclerView.Adapter<ConfirmFoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_confirm_food, parent, false))
    }

    override fun getItemCount() = listOrder.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detailOrder = listOrder[position]

        holder.apply {
            tvQuantity.text = detailOrder.numberOrder.toString()
            tvName.text = detailOrder.nameFood
            tvUnitPrice.text = ConvertHelper.doubleToMoney(mContext,detailOrder.getPrice())

            if (detailOrder.sizeName!!.isEmpty())
                tvSizeName.visibility = View.GONE
            else
                tvSizeName.text = detailOrder.sizeName

            if (detailOrder.note!!.isEmpty())
                tvNote.visibility = View.GONE
            else {
                tvNote.visibility = View.VISIBLE
                tvNote.text = detailOrder.note
            }


            tvPrice.text =ConvertHelper.doubleToMoney(mContext,(detailOrder.getPrice() * detailOrder.numberOrder!!))

            if (detailOrder.listTopping.isEmpty())
                tvTopping.visibility = View.GONE
            else {
                tvTopping.visibility = View.VISIBLE
                tvTopping.text = detailOrder.getStringTopping()
            }

            layoutRoot.setOnClickListener {
                onItemOrderClickListener?.onClick(detailOrder)
            }

            tvNote.setOnClickListener {
                initDialogAddNote(null, tvNote, detailOrder)
            }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val layoutRoot = view.layoutRoot
        val tvQuantity = view.tvQuantity
        val tvName = view.tvName
        val tvUnitPrice = view.tvUnitPrice
        val tvSizeName = view.tvSizeName
        val tvTopping = view.tvTopping
        val tvNote = view.tvNote
        val tvPrice = view.tvPrice
    }

    private fun initDialogAddNote(tvAddNote: TextView?, tvContent: TextView?, order: DetailOrder) {
        val builder = AlertDialog.Builder(mContext)
        //etReason.setPadding(ConvertHelper.dpToPx(context, 10), 0, 0, ConvertHelper.dpToPx(context, 10));
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_one_edit_text, null)
        val etContent = view.findViewById(R.id.etContent) as EditText
        etContent.setLines(3)
        etContent.hint = mContext.getString(R.string.choose_food_add_note_title)
        etContent.setText(tvContent?.text)
        builder.setView(view)
                .setPositiveButton(mContext.getString(R.string.all_ok)) { dialog, whichButton ->
                    tvContent?.text = etContent.text.toString()
                    //if have context: show tvContext and hide tvAddNote
                    //else like press cancel
                    if (tvContent?.text.toString().isNotEmpty()) {
                        tvContent?.visibility = View.VISIBLE
                        tvAddNote?.visibility = View.GONE
                        //save note to detail order
                        order.note = etContent.text.toString()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(mContext.getString(R.string.all_cancel)) { dialog, _ ->
                    // what ever you want to do with No option.
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
    }

    var onItemOrderClickListener: OnItemOrderClickListener? = null

    interface OnItemOrderClickListener {
        fun onClick(detailOrder: DetailOrder)
    }
}