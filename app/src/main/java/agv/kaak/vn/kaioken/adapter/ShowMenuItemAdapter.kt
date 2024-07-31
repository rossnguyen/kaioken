package agv.kaak.vn.kaioken.adapter


import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.define.OrderType
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.fragment.order.OptionFoodFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.item_show_menu_item.view.*
import kotlin.math.roundToInt

class ShowMenuItemAdapter(var context: Context,
                          var listSource: ArrayList<MenuFood>?,
                          var listOrder: ArrayList<DetailOrder>,
                          var type: Int) : RecyclerView.Adapter<ShowMenuItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_show_menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (type == OrderType.TYPE_CONFIRM_FOOD)
            return listOrder.size
        return listSource!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (type) {
            OrderType.TYPE_CHOSSE_FOOD -> onBindChooseFood(holder, position)
            OrderType.TYPE_CONFIRM_FOOD -> onBindConfirmFood(holder, position)
        }
    }

    fun filterList(filterFoods: ArrayList<MenuFood>) {
        this.listSource = filterFoods
        notifyDataSetChanged()
    }

    private fun onBindChooseFood(holder: ViewHolder, position: Int) {
        //hide control not necessary

        val food = listSource!![position]
        //ImageHelper.loadImage(context, holder.imgAvatar, foodWithTopping.image, PlaceHolderType.FOOD)
        ImageHelper.loadImageWithCorner(context, holder.imgAvatar, food.image, context.resources.getDimensionPixelOffset(R.dimen.all_corner_short), PlaceHolderType.FOOD)

        holder.tvName.text = food.name

        holder.tvPrice.text = ConvertHelper.doubleToMoney(context, food.price)

        holder.tvPriceAfterPromotion.text = ConvertHelper.doubleToMoney(context, food.priceAfterPromotion)

        if (food.price != food.priceAfterPromotion)
            holder.tvPrice.visibility = View.VISIBLE
        //if item have number: load number
        //else set text=''
        val indexWhenBind = getIndexOrderItem(food, listOrder)
        if (indexWhenBind == -1) {
            holder.tvQuantity.text = "0"
            holder.ibtnMinus.visibility = View.GONE
            holder.tvQuantity.visibility = View.GONE
        } else {
            holder.tvQuantity.text = DetailOrder.getQuantityViaFood(listOrder, food).toString()
            holder.ibtnMinus.visibility = View.VISIBLE
            holder.tvQuantity.visibility = View.VISIBLE

            //if have multi and have note: do nothing
            //if single and have note: show note
            if (indexWhenBind != -2) {
                val order = listOrder[indexWhenBind]
                if (order.note!!.isEmpty())
                    holder.tvAddNote.visibility = View.VISIBLE
                else {
                    holder.tvContentNote.visibility = View.VISIBLE
                    holder.tvContentNote.text = order.note
                }

            } else {
                holder.tvAddNote.visibility = View.GONE
                holder.tvContentNote.visibility = View.GONE
            }

        }

        holder.imgAvatar!!.setOnClickListener {
            GlobalHelper.showDialogDetailFood(context, food)
        }

        holder.ibtnPlus.setOnClickListener {
            if (food.hasTopping == 1)
                showDialogOptionFood(holder, food, position)
            else {
                addItemIntoListOrder(food)
                val oldQuantity = holder.tvQuantity.text.toString().toInt()
                val newQuantity = oldQuantity + 1
                holder.tvQuantity.text = "$newQuantity"
                holder.tvQuantity.visibility = View.VISIBLE
                holder.ibtnMinus.visibility = View.VISIBLE

                val indexWhenAdd = getIndexOrderItem(food, listOrder)
                //if have multi: do nothing
                //if single and have note: show note
                if (indexWhenAdd >= 0) {
                    val order = listOrder[indexWhenAdd]
                    if (order.note!!.isEmpty())
                        holder.tvAddNote.visibility = View.VISIBLE
                    else {
                        holder.tvContentNote.visibility = View.VISIBLE
                        holder.tvContentNote.text = order.note
                    }

                }

                onChangeNumberListener?.onChangeNumberOrder(position, oldQuantity, newQuantity)
            }
        }

        holder.ibtnMinus.setOnClickListener {
            val indexInListOrder = getIndexOrderItem(food, listOrder)
            if (indexInListOrder == -2)
                onShowDialogCartListener?.onShow()
            else {
                DetailOrder.minusItemFromListOrder(indexInListOrder, listOrder)
                var oldValue = holder.tvQuantity.text.toString().toInt()
                var newValue = oldValue - 1
                holder.tvQuantity.text = "$newValue"
                if (newValue == 0) {
                    holder.tvQuantity.visibility = View.GONE
                    holder.ibtnMinus.visibility = View.GONE
                    holder.tvAddNote.visibility = View.GONE
                    holder.tvContentNote.visibility = View.GONE
                    holder.tvContentNote.text = ""
                }

                onChangeNumberListener?.onChangeNumberOrder(position, oldValue, newValue)
            }
        }

        holder.tvAddNote.setOnClickListener {
            val indexWhenNote = getIndexOrderItem(food, listOrder)
            initDialogAddNote(holder.tvAddNote, holder.tvContentNote, listOrder[indexWhenNote])
        }

        holder.tvContentNote.setOnClickListener {
            val indexWhenNote = getIndexOrderItem(food, listOrder)
            initDialogAddNote(holder.tvAddNote, holder.tvContentNote, listOrder[indexWhenNote])
        }
    }

    private fun onBindConfirmFood(holder: ViewHolder, position: Int) {
        //hide control not necessary

        val food = listOrder[position]
        //ImageHelper.loadImage(context, holder.imgAvatar, foodWithTopping.linkImage, PlaceHolderType.FOOD)
        ImageHelper.loadImageWithCorner(context, holder.imgAvatar, food.linkImage, context.resources.getDimensionPixelOffset(R.dimen.all_corner_short), PlaceHolderType.FOOD)
        holder.tvName.text = food.nameFood
        holder.tvPrice.text = context.resources.getString(R.string.format_x_k, food.priceAfter!!.roundToInt())
        //if item have number: load number
        //else settext=''

        holder.tvQuantity.text = food.numberOrder.toString()
        //if have note: show note
        //else show textview addnote
        if (food.note == "") {
            holder.tvAddNote.visibility = View.VISIBLE
            holder.tvContentNote.visibility = View.GONE
        } else {
            holder.tvAddNote.visibility = View.GONE
            holder.tvContentNote.visibility = View.VISIBLE
            holder.tvContentNote.text = food.note
        }

        holder.imgAvatar!!.setOnClickListener {
            //GlobalHelper.showDialogDetailFood(context, foodWithTopping)
        }

        holder.tvAddNote.setOnClickListener {
            initDialogAddNote(holder.tvAddNote, holder.tvContentNote, food)
        }

        holder.tvContentNote.setOnClickListener {
            initDialogAddNote(holder.tvAddNote, holder.tvContentNote, food)
        }

        holder.ibtnPlus.setOnClickListener {
            val oldValue = holder.tvQuantity.text.toString().toInt()
            val newValue = oldValue + 1
            onChangeNumberListener?.onChangeNumberOrder(position, oldValue, newValue)
        }

        holder.ibtnMinus.setOnClickListener {
            val oldValue = holder.tvQuantity.text.toString().toInt()
            val newValue = oldValue - 1
            onChangeNumberListener?.onChangeNumberOrder(position, oldValue, newValue)
        }
    }

    private fun initDialogAddNote(tvAddNote: TextView, tvContent: TextView, order: DetailOrder) {
        val builder = AlertDialog.Builder(context)
        //etReason.setPadding(ConvertHelper.dpToPx(context, 10), 0, 0, ConvertHelper.dpToPx(context, 10));
        val view = LayoutInflater.from(context).inflate(R.layout.item_one_edit_text, null)
        val etContent = view.findViewById(R.id.etContent) as EditText
        etContent.setLines(3)
        etContent.hint = context.getString(R.string.choose_food_add_note_title)
        etContent.setText(tvContent.text)
        builder.setView(view)
                .setPositiveButton(context.getString(R.string.all_ok)) { dialog, whichButton ->
                    tvContent.text = etContent.text.toString()
                    //if have context: show tvContext and hide tvAddNote
                    //else like press cancel
                    if (tvContent.text.toString().isNotEmpty()) {
                        tvContent.visibility = View.VISIBLE
                        tvAddNote.visibility = View.GONE
                        //save note to detail order
                        order.note = etContent.text.toString()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(context.getString(R.string.all_cancel)) { dialog, _ ->
                    // what ever you want to do with No option.
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutRoot = itemView.layoutRoot
        val imgAvatar = itemView.imgAvatar
        val tvName = itemView.tvName
        val tvPrice = itemView.tvPrice
        val tvPriceAfterPromotion = itemView.tvPriceAfterPromotion
        val tvAddNote = itemView.tvAddNote
        val tvContentNote = itemView.tvContentNote
        val ibtnPlus = itemView.ibtnPlus
        val tvQuantity = itemView.tvQuantity
        val ibtnMinus = itemView.ibtnMinus

        init {
            tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    //return -2 if have more than 1 item in list
    //return -1 if no exist
    private fun getIndexOrderItem(item: MenuFood, listOrder: ArrayList<DetailOrder>): Int {
        var occurrenceCount = 0
        var indexItem = -1
        listOrder.forEachIndexed { index, detailOrder ->
            if (detailOrder.parentId == item.id) {
                occurrenceCount++
                indexItem = index
            }
        }

        if (occurrenceCount > 1)
            return -2
        else if (occurrenceCount == 1)
            return indexItem
        else
            return -1

    }

    private fun showDialogOptionFood(holder: ViewHolder, menuFood: MenuFood, position: Int) {
        val dialogOptionFood = OptionFoodFragment()
        dialogOptionFood.onAddFoodToCartClickListener = object : OptionFoodFragment.OnAddFoodToCartClickListener {
            override fun onClick(newOrder: DetailOrder) {
                DetailOrder.addItemIntoListOrder(newOrder, listOrder)

                onBindChooseFood(holder, position)
                /*val oldQuantity = holder.tvQuantity.text.toString().toInt()
                val newQuantity = oldQuantity + newOrder.numberOrder!!
                holder.tvQuantity.text = newQuantity.toString()
                if (newQuantity > 0) {
                    holder.ibtnMinus.visibility = View.VISIBLE
                    holder.tvQuantity.visibility = View.VISIBLE
                }*/

                onChangeNumberListener?.onChangeNumberOrder(-1, -1, -1)
            }
        }

        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, menuFood.id)


        dialogOptionFood.arguments = bundle
        dialogOptionFood.show((context as AppCompatActivity).supportFragmentManager, dialogOptionFood.tag)
    }

    private fun addItemIntoListOrder(item: MenuFood) {
        val newOrderItem = DetailOrder(1,
                0,
                item.id,
                1,
                "",
                item.name, item.image, item.priceAfterPromotion,
                "",
                1,
                1,
                item.id,
                "")
        DetailOrder.addItemIntoListOrder(newOrderItem, listOrder)
    }

    interface OnChangeNumberListener {
        fun onChangeNumberOrder(index: Int, oldValue: Int, newValue: Int)
    }

    interface OnShowDialogCartListener {
        fun onShow()
    }

    var onShowDialogCartListener: OnShowDialogCartListener? = null
    var onChangeNumberListener: OnChangeNumberListener? = null
}