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
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlin.math.roundToInt

class CartAdapter(val mContext: Context, val sourceListOrder: ArrayList<DetailOrder>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detailOrder = sourceListOrder[position]
        holder.apply {
            tvName.text = detailOrder.nameFood
            tvQuantity.text = detailOrder.numberOrder.toString()

            tvPriceAfterPromotion.text =ConvertHelper.doubleToMoney(mContext, detailOrder.getPrice())

            if (detailOrder.sizeName!!.isEmpty())
                tvSizeName.visibility = View.GONE
            else
                tvSizeName.text = detailOrder.sizeName

            if (detailOrder.getStringTopping().isEmpty())
                tvTopping.visibility = View.GONE
            else
                tvTopping.text = detailOrder.getStringTopping()

            if (detailOrder.note!!.isEmpty()) {
                tvNote.visibility = View.GONE
                tvAddNote.visibility = View.VISIBLE
            } else {
                tvNote.text = detailOrder.note
                tvNote.visibility = View.VISIBLE
                tvAddNote.visibility = View.GONE
            }



            ibtnPlus.setOnClickListener {
                val oldValue = tvQuantity.text.toString().toInt()
                val newValue = oldValue + 1

                tvQuantity.text = "$newValue"
                detailOrder.numberOrder = newValue
                onChangeNumberListener?.onChangeNumberOrder(position, oldValue, newValue)
            }

            ibtnMinus.setOnClickListener {
                val oldValue = tvQuantity.text.toString().toInt()
                if (oldValue == 0)
                    return@setOnClickListener
                val newValue = oldValue - 1

                tvQuantity.text = "$newValue"
                detailOrder.numberOrder = newValue
                onChangeNumberListener?.onChangeNumberOrder(position, oldValue, newValue)
            }

            tvNote.setOnClickListener {
                initDialogAddNote(tvAddNote, tvNote, detailOrder)
            }

            tvAddNote.setOnClickListener {
                initDialogAddNote(tvAddNote, tvNote, detailOrder)
            }
        }
    }

    override fun getItemCount() = sourceListOrder.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.tvName
        val tvPrice = view.tvPrice
        val tvPriceAfterPromotion=view.tvPriceAfterPromotion
        val tvSizeName = view.tvSizeName
        val tvTopping = view.tvTopping
        val tvNote = view.tvNote
        val tvAddNote = view.tvAddNote
        val ibtnPlus = view.ibtnPlus
        val tvQuantity = view.tvQuantity
        val ibtnMinus = view.ibtnMinus
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

    var onChangeNumberListener: ShowMenuItemAdapter.OnChangeNumberListener? = null
}