package agv.kaak.vn.kaioken.widget

import agv.kaak.vn.kaioken.R
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_input_text.view.*

class InputText(context: Context, val attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var tvTitle: TextView
    private var etContent: EditText

    private var title: String = ""
    private var content: String = ""
    private var inputType: Int = EditorInfo.TYPE_CLASS_TEXT
    private var imeOption: Int = 0
    private var notNull: Boolean = false
    private var lines: Int = 1

    var onEdittextTimeClickListener: View.OnClickListener? = null
    var onEdittextClickListenter: View.OnClickListener? = null
    var onFocusTextChangeListener: OnTextChangeFocus? = null

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.InputText, 0, 0)
        title = typeArray.getString(R.styleable.InputText_it_title)
        content = typeArray.getString(R.styleable.InputText_it_content)
        inputType = typeArray.getInt(R.styleable.InputText_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
        imeOption = typeArray.getInt(R.styleable.InputText_android_imeOptions, 0)
        notNull = typeArray.getBoolean(R.styleable.InputText_it_notnull, false)
        lines = typeArray.getInt(R.styleable.InputText_it_lines, 1)
        typeArray.recycle()

        val view = LayoutInflater.from(context).inflate(R.layout.item_input_text, this, true)
        tvTitle = view.tvTitle
        etContent = view.etContent

        setTitleText(title)
        setContentText(content)
        setInputTextType(inputType)
        setLines(lines)
        setImeOptionText(imeOption)


        etContent.setOnFocusChangeListener { v, hasFocus ->
            if (onEdittextTimeClickListener != null && hasFocus)
                onEdittextTimeClickListener!!.onClick(etContent)
            if (onEdittextClickListenter != null && hasFocus)
                onEdittextClickListenter!!.onClick(etContent)
            if(onFocusTextChangeListener!=null)
                onFocusTextChangeListener!!.onChangeFocus(hasFocus)
        }

        if (notNull) {
            etContent.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //nothing to do
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty())
                        etContent.error = context.resources.getString(R.string.all_must_to_enter)
                }
            })
        }
    }

    fun setTitleText(value: String) {
        this.title = value
        tvTitle.text = value
    }

    fun clearContent() {
        this.content = ""
        etContent.text.clear()
    }

    fun setContentText(value: String) {
        this.content = value
        etContent.setText(value)
    }

    fun getContentText(): String {
        return etContent.text.toString()
    }

    fun setInputTextType(value: Int) {
        inputType = value
        etContent.inputType = value
    }

    fun setImeOptionText(value: Int) {
        imeOption = value
        etContent.imeOptions = value
    }

    fun setError(message: String?) {
        etContent.error = message
    }

    fun setLines(value: Int) {
        lines = value
        if (lines > 1) {
            etContent.setLines(lines)
            etContent.maxLines = lines
            etContent.setHorizontallyScrolling(false)
            //etContent.inputType=InputType.TYPE_TEXT_FLAG_MULTI_LINE
            etContent.imeOptions = EditorInfo.IME_ACTION_NEXT

            //etContent.setRawInputType(InputType.TYPE_CLASS_TEXT)
        } else
            etContent.setSingleLine(true)

    }

    interface OnTextChangeFocus{
        fun onChangeFocus(hasFocus:Boolean)
    }
}