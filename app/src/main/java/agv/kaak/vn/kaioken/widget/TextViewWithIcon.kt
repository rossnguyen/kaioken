package agv.kaak.vn.kaioken.widget

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.ConvertHelper
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_component_textview_with_icon.view.*

class TextViewWithIcon(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private lateinit var view: View
    private lateinit var tvContent: TextView
    private lateinit var tvSubText: TextView
    private lateinit var tvNavigateText: TextView
    private lateinit var imgIcon: ImageView

    private lateinit var srcImage: Drawable
    private var srcTint: Int = 0
    private var srcSize: Int = 0
    private var text: String = ""
    private var textSub: String = ""
    private var textNavigate: String = ""
    private var textColor: Int = 0
    private var textSize: Int = 0
    private var subSize: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithIcon, 0, 0)
        text = typedArray.getString(R.styleable.TextViewWithIcon_tvwi_text)
        textSub = typedArray.getString(R.styleable.TextViewWithIcon_tvwi_text_sub)
        textNavigate = typedArray.getString(R.styleable.TextViewWithIcon_tvwi_text_navigate)
        textColor = typedArray.getInteger(R.styleable.TextViewWithIcon_tvwi_text_color, Color.BLACK)
        textSize = typedArray.getDimensionPixelOffset(R.styleable.TextViewWithIcon_tvwi_text_size, 15)
        subSize = typedArray.getDimensionPixelOffset(R.styleable.TextViewWithIcon_tvwi_sub_size, 12)
        srcImage = typedArray.getDrawable(R.styleable.TextViewWithIcon_tvwi_src)
        srcTint = typedArray.getInteger(R.styleable.TextViewWithIcon_tvwi_src_tint, Color.BLACK)
        srcSize = ConvertHelper.pxTodp(context, typedArray.getDimensionPixelSize(R.styleable.TextViewWithIcon_tvwi_src_size, 40))
        typedArray.recycle()

        view = LayoutInflater.from(context).inflate(R.layout.custom_component_textview_with_icon, this, true)
        tvContent = view.tvContent
        tvSubText = view.tvSubText
        tvNavigateText = view.tvNavigate
        imgIcon = view.imgIcon

        setContentText(text)
        setSubText(textSub)
        setNavigateText(textNavigate)
        setSubTextSize(subSize)
        setContentTextColor(textColor)
        setContentTextSize(textSize)
        setSource(srcImage)
        setSourceSize(srcSize)
        setSourceTint(srcTint)
    }

    fun setContentText(value: String) {
        this.text = value
        tvContent.text = text
    }

    fun setSubText(value: String) {
        this.textSub = value
        if (value.isEmpty()) {
            tvSubText.visibility = View.GONE

        } else {
            tvSubText.visibility = View.VISIBLE
            tvSubText.text = textSub
        }
    }

    private fun setNavigateText(value: String) {
        this.textNavigate = value
        if (value.isEmpty())
            tvNavigateText.visibility = View.GONE
        else {
            tvNavigateText.visibility = View.VISIBLE
            tvNavigateText.text = textNavigate
        }
    }


    fun getContentText(): String {
        return text
    }

    private fun setContentTextColor(textColor: Int) {
        this.textColor = textColor
        tvContent.setTextColor(textColor)
    }

    private fun setContentTextSize(textSize: Int) {
        this.textSize = textSize
        tvContent.textSize = textSize.toFloat()
    }

    private fun setSubTextSize(textSize: Int) {
        this.subSize = textSize
        tvSubText.textSize = textSize.toFloat()
        tvNavigateText.textSize = textSize.toFloat()
    }


    private fun setSource(src: Drawable) {
        this.srcImage = src
        imgIcon.setImageDrawable(srcImage)
    }

    private fun setSourceSize(sourceSize: Int) {
        this.srcSize = sourceSize
        val layoutParams = imgIcon.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = sourceSize
        layoutParams.height = sourceSize
        imgIcon.layoutParams = layoutParams
    }

    private fun setSourceTint(sourceTint: Int) {
        /*this.srcTint = sourceTint
        imgIcon.setColorFilter(sourceTint)*/
    }
}