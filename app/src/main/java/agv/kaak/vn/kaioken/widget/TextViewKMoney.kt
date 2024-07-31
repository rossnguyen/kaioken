package agv.kaak.vn.kaioken.widget

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

class TextViewKMoney(context: Context, attrs: AttributeSet):RelativeLayout(context,attrs){
    var tvValue: TextView?=null
    var tvSuffix:TextView?=null

    var value: String? = null
    var valueColor: Int = 0
    var suffixColor:Int = 0
    var valueSize: Float = 0.toFloat()
    var suffixSize:Float = 0.toFloat()
    var isBold = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewKMoney, 0, 0)
        value = typedArray.getString(R.styleable.TextViewKMoney_tvkm_text)
        valueColor = typedArray.getInteger(R.styleable.TextViewKMoney_tvkm_text_color, Color.BLACK)
        suffixColor = typedArray.getInteger(R.styleable.TextViewKMoney_tvkm_k_color, Color.BLACK)
        valueSize = ConvertHelper.pxTodp(context, typedArray.getDimensionPixelSize(R.styleable.TextViewKMoney_tvkm_text_size, 12)).toFloat()
        suffixSize = ConvertHelper.pxTodp(context, typedArray.getDimensionPixelSize(R.styleable.TextViewKMoney_tvkm_k_size, 12)).toFloat()
        isBold = typedArray.getBoolean(R.styleable.TextViewKMoney_tvkm_is_bold, false)
        typedArray.recycle()

        val view = LayoutInflater.from(context).inflate(R.layout.custom_component_textview_k_money, this, true)
        tvValue = view.findViewById(R.id.tvValue)
        tvSuffix = view.findViewById(R.id.tvSuffix)

        setupValue(value!!)
        setupValueColor(valueColor)
        setupValueSize(valueSize)
        setupSuffixColor(suffixColor)
        setupSuffixSize(suffixSize)
        setupBold(isBold)
    }

    fun setupValue(value:String) {
        this.value = value
        tvValue!!.text=value
    }

    fun setupValueColor(valueColor:Int) {
        this.valueColor = valueColor
        this.tvValue!!.setTextColor(valueColor)
    }

    fun setupSuffixColor(suffixColor:Int) {
        this.suffixColor = suffixColor;
        this.tvSuffix!!.setTextColor(suffixColor);
    }

    fun setupValueSize(valueSize:Float) {
        this.valueSize = valueSize;
        this.tvValue!!.setTextSize(valueSize);
    }

    fun setupSuffixSize(suffixSize:Float) {
        this.suffixSize = suffixSize
        this.tvSuffix!!.setTextSize(suffixSize)
    }

    fun setupBold(isBold:Boolean){
        this.isBold=isBold
        if(isBold){
            this.tvValue!!.setTypeface(tvValue!!.getTypeface(), Typeface.BOLD);
            this.tvSuffix!!.setTypeface(tvSuffix!!.getTypeface(),Typeface.BOLD);
        }else{
            this.tvValue!!.setTypeface(tvValue!!.getTypeface(), Typeface.NORMAL);
            this.tvSuffix!!.setTypeface(tvSuffix!!.getTypeface(),Typeface.NORMAL);
        }
    }

    fun setHideK(isHide:Boolean){
        if(isHide)
            this.tvSuffix!!.setVisibility(GONE);
        else
            this.tvSuffix!!.setVisibility(VISIBLE);
    }

}