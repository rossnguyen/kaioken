package agv.kaak.vn.kaioken.widget

import agv.kaak.vn.kaioken.R
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class ToolbarCustom(context: Context, attrs: AttributeSet?):RelativeLayout(context,attrs) {
    private var view: View? = null
    private var ibtnBack: ImageButton? = null
    private var tvTitle: TextView? = null
    private var layoutRoot:RelativeLayout?=null

    private var title: String? = null
    private var titleColor: Int = 0
    private var background:Int=0

    init{
        init(context, attrs)
    }

    private fun init(context: Context, atts: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(atts, R.styleable.ToolbarCustom, 0, 0)
        title = typedArray.getString(R.styleable.ToolbarCustom_tc_title)
        titleColor = typedArray.getColor(R.styleable.ToolbarCustom_tc_title_color, Color.BLACK)
        background=typedArray.getColor(R.styleable.ToolbarCustom_tc_background_color,Color.WHITE)
        typedArray.recycle()

        view = LayoutInflater.from(context).inflate(R.layout.custom_toolbar, this, true)
        layoutRoot=view!!.layoutRoot
        ibtnBack = view!!.ibtnBack
        tvTitle = view!!.tvTitle
        setTitle(title)
        setTitleColor(titleColor)

        ibtnBack!!.setOnClickListener { v ->
            if (onBackButtonClickListener != null)
                onBackButtonClickListener!!.onClick(v)
        }
    }

    fun setTitle(title: String?) {
        this.title = title
        tvTitle!!.text = title
    }

    fun setTitleColor(color: Int) {
        this.titleColor = color
        tvTitle!!.setTextColor(titleColor)
    }

    fun setBackground(color: Int) {
        this.background = color
        layoutRoot!!.setBackgroundColor(color)
    }

    private var onBackButtonClickListener: View.OnClickListener? = null
    fun setOnBackButtonClickListener(onBackButtonClickListener: View.OnClickListener) {
        this.onBackButtonClickListener = onBackButtonClickListener
    }
}