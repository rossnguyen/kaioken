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
import kotlinx.android.synthetic.main.custom_component_textview_with_image_above.view.*

class TextviewWithImageAbove(context: Context, val attributes: AttributeSet) : RelativeLayout(context, attributes) {
    var view: View
    var text: String
    var textColor: Int = 0

    var textSize: Int = 0
    var src: Drawable
    var srcSize: Int = 0
    var srcTint: Int = 0

    val tvContent: TextView
    val imgIcon: ImageView

    init {
        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.TextviewWithImageAbove, 0, 0)
        text = typedArray.getString(R.styleable.TextviewWithImageAbove_twia_text);
        textColor = typedArray.getInteger(R.styleable.TextviewWithImageAbove_twia_text_color, Color.BLACK)
        textSize = ConvertHelper.pxTodp(context, typedArray.getDimensionPixelSize(R.styleable.TextviewWithImageAbove_twia_text_size, 12))
        src = typedArray.getDrawable(R.styleable.TextviewWithImageAbove_twia_src)
        srcSize = ConvertHelper.pxTodp(context, typedArray.getDimensionPixelSize(R.styleable.TextviewWithImageAbove_twia_src_size, 40))
        srcTint = typedArray.getInteger(R.styleable.TextviewWithImageAbove_twia_src_tint, Color.BLACK);
        typedArray.recycle()

        view = LayoutInflater.from(context).inflate(R.layout.custom_component_textview_with_image_above, this, true)
        tvContent = view.tvContent
        imgIcon = view.imgIcon

        setContentText(text)
        setContentTextColor(textColor)
        setContentTextSize(textSize)
        setSource(src)
        setSourceSize(srcSize)
        setSourceTint(srcTint)
    }

    fun setContentText(value: String) {
        this.text = value
        this.tvContent.text = value
    }

    fun getContentText(): String {
        return text
    }

    fun setContentTextColor(textColor: Int) {
        this.textColor = textColor
        this.tvContent.setTextColor(textColor)
    }

    fun setContentTextSize(textSize: Int) {
        this.textSize = textSize
        this.tvContent.setTextSize(textSize.toFloat())
    }

    fun setSource(src: Drawable) {
        this.src = src
        this.imgIcon.setImageDrawable(src)
    }

    fun setSourceSize(sourceSize: Int) {
        this.srcSize = sourceSize
        var layoutParams = this.imgIcon.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = sourceSize
        layoutParams.height = sourceSize
        this.imgIcon.layoutParams = layoutParams
    }

    fun setSourceTint(sourceTint: Int) {
        this.srcTint = sourceTint
        this.imgIcon.setColorFilter(sourceTint)
    }
}