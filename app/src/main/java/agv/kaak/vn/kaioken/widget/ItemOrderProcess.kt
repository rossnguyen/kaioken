package agv.kaak.vn.kaioken.widget

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.custom_component_item_order_process.view.*

class ItemOrderProcess(context: Context, attributeSet: AttributeSet?) : RelativeLayout(context, attributeSet) {
    private var view: View?=null
    private var img: ImageView?=null
    private var tvMain: TextView?=null
    private var tvSub: TextView?=null

    private var mainText: String = ""
    private var subText: String = ""
    private var mainTextSize: Float = 0.toFloat()
    private var subTextSize: Float = 0.toFloat()
    private var mainTextColor: Int = 0
    private var mainTextColorFinish: Int = 0
    private var subTextColor: Int = 0
    private var gravityIsLeft: Boolean = false
    private var isFinish: Boolean = false

    init {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ItemOrderProcess, 0, 0)
        mainText = typedArray.getString(R.styleable.ItemOrderProcess_iop_main_text)
        subText = typedArray.getString(R.styleable.ItemOrderProcess_iop_sub_text)
        mainTextSize = typedArray.getDimensionPixelSize(R.styleable.ItemOrderProcess_iop_main_text_size, 12).toFloat()
        subTextSize = typedArray.getDimensionPixelSize(R.styleable.ItemOrderProcess_iop_sub_text_size, 12).toFloat()
        mainTextColor = typedArray.getInteger(R.styleable.ItemOrderProcess_iop_main_text_color, Color.BLACK)
        mainTextColorFinish = typedArray.getInteger(R.styleable.ItemOrderProcess_iop_main_text_color_finish, Color.GRAY)
        subTextColor = typedArray.getInteger(R.styleable.ItemOrderProcess_iop_sub_text_color, R.color.colorBlueLight)
        gravityIsLeft = typedArray.getBoolean(R.styleable.ItemOrderProcess_iop_gravity_is_left, true)
        isFinish = typedArray.getBoolean(R.styleable.ItemOrderProcess_iop_is_finish, false)
        typedArray.recycle()

        view = LayoutInflater.from(context).inflate(R.layout.custom_component_item_order_process, this, true)
        img = view!!.img
        tvMain = view!!.tvMainText
        tvSub = view!!.tvSubText

        setFinish(context, isFinish)
        setMainText(mainText)
        setMainTextSize(mainTextSize)
        setMainTextColor(mainTextColor)
        setSubText(subText)
        setSubTextSize(subTextSize)
        setSubTextColor(subTextColor)
        //setGravityIsLeft(getContext(), gravityIsLeft)
    }

    fun setMainText(mainText: String) {
        this.mainText = mainText
        tvMain!!.text = mainText
    }

    fun setSubText(subText: String) {
        this.subText = subText
        if(subText.isEmpty())
            tvSub!!.visibility=View.GONE
        else{
            tvSub!!.visibility=View.VISIBLE
            tvSub!!.text = subText
        }
    }

    fun setMainTextSize(mainTextSize: Float) {
        this.mainTextSize = mainTextSize
        tvMain!!.textSize = mainTextSize
    }

    fun setSubTextSize(subTextSize: Float) {
        this.subTextSize = subTextSize
        tvSub!!.textSize = subTextSize
    }

    fun setMainTextColor(mainTextColor: Int) {
        this.mainTextColor = mainTextColor
        tvMain!!.setTextColor(mainTextColor)
    }

    fun setSubTextColor(subTextColor: Int) {
        this.subTextColor = subTextColor
        tvSub!!.setTextColor(subTextColor)
    }

    /*fun setGravityIsLeft(context: Context, gravityIsLeft: Boolean) {
        val paramMain = tvMain!!.layoutParams as RelativeLayout.LayoutParams
        paramMain.setMargins(ConvertHelper.dpToPx(context, 0),
                ConvertHelper.dpToPx(context, 20),
                ConvertHelper.dpToPx(context, 0),
                ConvertHelper.dpToPx(context, 0))

        val paramSub = tvSub!!.layoutParams as RelativeLayout.LayoutParams
        if (gravityIsLeft) {
            paramMain.addRule(RelativeLayout.LEFT_OF, R.id.img)
            paramMain.removeRule(RelativeLayout.RIGHT_OF)
            paramSub.addRule(RelativeLayout.LEFT_OF, R.id.img)
            paramSub.removeRule(RelativeLayout.RIGHT_OF)
        } else {
            paramMain.addRule(RelativeLayout.RIGHT_OF, R.id.img)
            paramMain.removeRule(RelativeLayout.LEFT_OF)
            paramSub.addRule(RelativeLayout.RIGHT_OF, R.id.img)
            paramSub.removeRule(RelativeLayout.LEFT_OF)
        }

        tvMain!!.layoutParams = paramMain
        tvSub!!.layoutParams = paramSub
    }*/

    fun setFinish(context: Context, finish: Boolean) {
        isFinish = finish
        if (isFinish) {
            img!!.setImageResource(R.drawable.ic_dot_filled_red)
            img!!.setPadding(0, 0, 0, 0)
            tvMain!!.setTextColor(mainTextColorFinish)
            tvSub!!.visibility = View.VISIBLE
        } else {
            img!!.setImageResource(R.drawable.ic_dot_red)
            img!!.setPadding(ConvertHelper.dpToPx(context, 7), ConvertHelper.dpToPx(context, 7), ConvertHelper.dpToPx(context, 7), ConvertHelper.dpToPx(context, 7))
            tvMain!!.setTextColor(mainTextColor)
            tvSub!!.visibility = View.GONE
        }

    }
}