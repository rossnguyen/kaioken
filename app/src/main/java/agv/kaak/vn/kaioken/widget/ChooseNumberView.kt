package agv.kaak.vn.kaioken.widget

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.auto_complete_list.view.*
import kotlinx.android.synthetic.main.custom_choose_number_view.view.*

class ChooseNumberView(val mContext: Context, val attrs: AttributeSet) : RelativeLayout(mContext, attrs) {
    private var tvValue: TextView
    private var btnMinus: TextView
    private var btnPlus: TextView

    private var mHeight: Int = 0
    private var mBackgroundColor: Int = 0
    private var mTextButtonColor: Int = 0
    private var mTextValueColor: Int = 0
    private var mBackgroundValueColor: Int = 0
    private var mTextButtonSize: Int = 0
    private var mTextValueSize: Int = 0
    private var mValue: Int = 0

    private lateinit var drawableButtonPlus: GradientDrawable
    private lateinit var drawableButtonMinus: GradientDrawable
    private lateinit var drawableCircle: GradientDrawable

    private var value: Int = 0

    init {
        val typeArray = mContext.obtainStyledAttributes(attrs, R.styleable.ChooseNumberView, 0, 0)
        mHeight = typeArray.getDimensionPixelOffset(R.styleable.ChooseNumberView_cnv_height, ConvertHelper.dpToPx(mContext, 32))
        mBackgroundColor = typeArray.getColor(R.styleable.ChooseNumberView_cnv_background_color, mContext.resources.getColor(R.color.colorVioletPrimary))
        mTextButtonColor = typeArray.getColor(R.styleable.ChooseNumberView_cnv_text_button_color, mContext.resources.getColor(R.color.colorWhiteApp))
        mTextValueColor = typeArray.getColor(R.styleable.ChooseNumberView_cnv_text_value_color, mContext.resources.getColor(R.color.colorBlackPrimary))
        mBackgroundValueColor = typeArray.getColor(R.styleable.ChooseNumberView_cnv_background_value_color, mContext.resources.getColor(R.color.colorWhiteApp))
        mTextButtonSize = typeArray.getDimensionPixelSize(R.styleable.ChooseNumberView_cnv_text_button_size, ConvertHelper.dpToPx(mContext, 15))
        mTextValueSize = typeArray.getDimensionPixelSize(R.styleable.ChooseNumberView_cnv_text_value_size, ConvertHelper.dpToPx(mContext, 20))
        mValue = typeArray.getInt(R.styleable.ChooseNumberView_cnv_value, ConvertHelper.dpToPx(mContext, 0))
        typeArray.recycle()


        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_choose_number_view, this, true)
        tvValue = view.tvValue
        btnMinus = view.btnMinus
        btnPlus = view.btnPlus


        val drawableRoot = GradientDrawable()
        drawableRoot.cornerRadius = (mHeight / 2).toFloat()
        drawableRoot.setTint(mBackgroundColor)

        drawableButtonPlus = GradientDrawable()
        drawableButtonPlus.cornerRadius = (mHeight / 2).toFloat()
        drawableButtonPlus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButtonPressed))
        drawableButtonPlus.color = ContextCompat.getColorStateList(mContext, R.color.color_choose_number_view)


        drawableButtonMinus = GradientDrawable()
        drawableButtonMinus.cornerRadius = (mHeight / 2).toFloat()
        drawableButtonMinus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButtonPressed))
        drawableButtonMinus.color = ContextCompat.getColorStateList(mContext, R.color.color_choose_number_view)

        drawableCircle = GradientDrawable()
        drawableCircle.cornerRadius = (mHeight / 2).toFloat()
        drawableCircle.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
        drawableCircle.setColor(mBackgroundValueColor)

        super.setBackground(drawableRoot)

        val layoutParamsButtonPlus = LayoutParams(mHeight * 2, mHeight)
        layoutParamsButtonPlus.apply {
            addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
            addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            addRule(RelativeLayout.ALIGN_PARENT_START, 0)
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        }

        val layoutParamsButtonMinus = LayoutParams(mHeight * 2, mHeight)
        /*layoutParamsButtonMinus.apply {
            addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
            addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, 0)
        }*/

        btnMinus.layoutParams = layoutParamsButtonMinus
        btnMinus.background = drawableButtonMinus
        btnMinus.setPadding(0, 0, mHeight, 0)
        btnMinus.gravity = Gravity.CENTER

        btnPlus.layoutParams = layoutParamsButtonPlus
        btnPlus.background = drawableButtonPlus
        btnPlus.setPadding(mHeight, 0, 0, 0)
        btnPlus.gravity = Gravity.CENTER

        val layoutParamValue = LayoutParams(mHeight, mHeight)
        layoutParamValue.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        tvValue.layoutParams = layoutParamValue
        tvValue.background = drawableCircle


        btnMinus.setTextColor(mTextButtonColor)
        btnPlus.setTextColor(mTextButtonColor)
        tvValue.setTextColor(mTextValueColor)

        btnMinus.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextButtonSize.toFloat() + 20)
        btnPlus.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextButtonSize.toFloat())
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextValueSize.toFloat())

        tvValue.text = "$value"

        btnMinus.setOnClickListener {
            if (value > 0) {
                if (onChangeNumberListener != null) {
                    onChangeNumberListener!!.onChangeNumberOrder(-1, value, value - 1)
                } else {
                    value--
                    tvValue.text = "$value"

                    //highlight item choose
                    if (value == 0) {
                        drawableButtonPlus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
                        drawableButtonMinus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
                        drawableCircle.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
                    } else {
                        drawableButtonPlus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
                        drawableButtonMinus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
                        drawableCircle.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayApp))
                    }
                }
            }
        }

        btnPlus.setOnClickListener {
            //highlight item choose
            drawableButtonPlus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
            drawableButtonMinus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
            drawableCircle.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayApp))

            if (onChangeNumberListener != null) {
                onChangeNumberListener!!.onChangeNumberOrder(-1, value, value + 1)
            } else {
                value++
                tvValue.text = "$value"
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resizeRoot(mHeight * 3, mHeight)
    }

    private fun resizeRoot(width: Int, height: Int) {
        //set value for view
        val layoutParamsRoot = layoutParams
        layoutParams.width = width
        layoutParams.height = height
        super.setLayoutParams(layoutParamsRoot)
    }

    fun getValue(): Int {
        return value
    }

    fun setValue(value: Int?) {
        if (value == null)
            this.value = 0
        else
            this.value = value

        tvValue.text = "$value"

        if (value == 0) {
            drawableButtonPlus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
            drawableButtonMinus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
            drawableCircle.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayAppLight))
        } else {
            drawableButtonPlus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
            drawableButtonMinus.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorPrimaryButton))
            drawableCircle.setStroke(ConvertHelper.dpToPx(mContext, 1), ContextCompat.getColor(mContext, R.color.colorGrayApp))
        }
    }

    var onChangeNumberListener: ShowMenuItemAdapter.OnChangeNumberListener? = null
}