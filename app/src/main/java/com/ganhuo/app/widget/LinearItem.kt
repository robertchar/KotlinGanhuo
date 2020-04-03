package com.ganhuo.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.ganhuo.app.R
import org.jetbrains.anko.find

/**
 *    author : zkk
 *    date   : 2020-04-02 14:29
 *    desc   :自定义控件
 */
class LinearItem : LinearLayout {
    private var content: String? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (attrs != null) {
            initView(context, attrs)
        }
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        LayoutInflater.from(context).inflate(R.layout.widget_linear, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.LinearItem, 0, 0)
        try {
            content = attributes.getString(R.styleable.LinearItem_text)
        } finally {
            attributes.recycle()
        }
        setView()
    }

    private fun setView() {
        val linearLayout = find<LinearLayout>(R.id.ll)
        val mContent = find<TextView>(R.id.tv_content)
        mContent.text = content
        linearLayout.setOnClickListener {
            listener?.itemClick()
        }
    }

    //自定义监听器
    var listener: OnLlClick? = null

    interface OnLlClick {
        fun itemClick(
        )
    }

    fun setItemClickListener(listener: OnLlClick) {
        this.listener = listener
    }
}