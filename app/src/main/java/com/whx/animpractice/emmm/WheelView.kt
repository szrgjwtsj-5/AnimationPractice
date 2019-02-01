package com.whx.animpractice.emmm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.whx.animpractice.R

/**
 * 轮式选择View, 仅支持显示文字
 */
class WheelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defArr: Int = 0) : View(context, attrs, defArr) {

    private var textSize: Int
    private var textColor: Int
    private var textPadding: Int

    private var textScale: Float
    private var textMinAlpha: Float

    private var isRecycleMode: Boolean
    private var maxShowNum: Int

    private val textPaint: Paint
    private val fontMetrics: Paint.FontMetrics

    private val scroller: Scroller
    private var velocityTracker: VelocityTracker? = null

    private var minVelocity: Int
    private var maxVelocity: Int
    private var scaledTouchSlop: Int

    private val mData = arrayListOf<WheelData>()

    private var cx = 0
    private var cy = 0

    private var maxTextWidth = 0f
    private var textHeight: Int
    private var contentWidth = 0
    private var contentHeight = 0

    private var downY = 0f
    private var offsetY = 0f
    private var oldOffsetY = 0f

    private var curIndex = 0
    private var offsetIndex = 0

    private var bounceDistance = 0f
    private var isSliding = false

    private var onScrollChangedListener: OnScrollChangedListener? = null

    init {
        val tpa = context.theme.obtainStyledAttributes(attrs, R.styleable.WheelView, defArr, 0)
        textSize = tpa.getDimensionPixelSize(R.styleable.WheelView_wvTextSize, resources.getDimensionPixelSize(R.dimen.sp_16))
        textColor = tpa.getColor(R.styleable.WheelView_wvTextColor, resources.getColor(R.color.color_333333))
        textPadding = tpa.getDimensionPixelSize(R.styleable.WheelView_wvTextPadding, resources.getDimensionPixelSize(R.dimen.dp_10))
        textScale = tpa.getFloat(R.styleable.WheelView_wvTextMaxScale, 2.0f)
        textMinAlpha = tpa.getFloat(R.styleable.WheelView_wvTextMinAlpha, 0.5f)
        isRecycleMode = tpa.getBoolean(R.styleable.WheelView_wvRecycleMode, true)
        maxShowNum = tpa.getInteger(R.styleable.WheelView_wvMaxShowNum, 3)
        tpa.recycle()

        textPaint = Paint().apply {
            color = textColor
            textSize = textSize
            isAntiAlias = true
        }

        fontMetrics = textPaint.fontMetrics
        textHeight = (fontMetrics.bottom - fontMetrics.top).toInt()

        scroller = Scroller(context)
        minVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
        maxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        contentWidth = (maxTextWidth * textScale + paddingLeft + paddingRight).toInt()
        if (mode != MeasureSpec.EXACTLY) { // wrap_content
            width = contentWidth
        }

        mode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        contentHeight = textHeight * maxShowNum + textPadding * maxShowNum
        if (mode != MeasureSpec.EXACTLY) { // wrap_content
            height = contentHeight + paddingTop + paddingBottom
        }

        cx = width / 2
        cy = height / 2

        setMeasuredDimension(width, height)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        addVelocityTracker(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.forceFinished(true)
                    finishScroll()
                }
                downY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                offsetY = event.y - downY
                if (isSliding || Math.abs(offsetY) > scaledTouchSlop) {
                    isSliding = true
                    reDraw()
                }
            }

            MotionEvent.ACTION_UP -> {
                val scrollYVelocity = 2 * getScrollYVelocity() / 3
                if (Math.abs(scrollYVelocity) > minVelocity) {
                    oldOffsetY = offsetY
                    scroller.fling(0, 0, 0, scrollYVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE)
                    invalidate()
                } else {
                    finishScroll()
                }

                // 没有滑动，则判断点击事件
                if (!isSliding) {
                    if (downY < contentHeight / 3)
                        moveBy(-1)
                    else if (downY > 2 * contentHeight / 3)
                        moveBy(1)
                }

                isSliding = false
                recycleVelocityTracker()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        if (mData.size > 0) {
            canvas?.clipRect(
                    cx - contentWidth / 2,
                    cy - contentHeight / 2,
                    cx + contentWidth / 2,
                    cy + contentHeight / 2
            )

            // 绘制文字，从当前中间项往前、后一共绘制maxShowNum个字
            val size = mData.size
            val centerPadding = textHeight + textPadding
            val half = maxShowNum / 2 + 1
            for (i in -half..half) {
                var index = curIndex - offsetIndex + i

                if (isRecycleMode) {
                    if (index < 0)
                        index = (index + 1) % mData.size + mData.size - 1
                    else if (index > mData.size - 1)
                        index %= mData.size
                }

                if (index in 0..(size - 1)) {
                    // 计算每个字的中间y坐标
                    var tempY = cy + i * centerPadding
                    tempY += (offsetY % centerPadding).toInt()

                    // 根据每个字中间y坐标到cy的距离，计算出scale值
                    val scale = 1.0f - 1.0f * Math.abs(tempY - cy) / centerPadding

                    // 根据textMaxScale，计算出tempScale值，即实际text应该放大的倍数，范围 1~textMaxScale
                    var tempScale = scale * (textScale - 1.0f) + 1.0f
                    tempScale = if (tempScale < 1.0f) 1.0f else tempScale

                    // 计算文字alpha值
                    var textAlpha = textMinAlpha
                    if (textScale != 1f) {
                        val tempAlpha = (tempScale - 1) / (textScale - 1)
                        textAlpha = (1 - textMinAlpha) * tempAlpha + textMinAlpha
                    }

                    textPaint.textSize = textSize * tempScale
                    textPaint.alpha = (255 * textAlpha).toInt()

                    // 绘制
                    val tempFm = textPaint.fontMetrics
                    val text = mData[index].text
                    val textWidth = textPaint.measureText(text)
                    canvas?.drawText(text, cx - textWidth / 2, tempY - (tempFm.ascent + tempFm.descent) / 2, textPaint)
                }
            }
        }
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            offsetY = oldOffsetY + scroller.currY

            if (!scroller.isFinished)
                reDraw()
            else
                finishScroll()
        }
    }

    private fun getScrollYVelocity(): Int {
        velocityTracker?.computeCurrentVelocity(1000, maxVelocity.toFloat())
        return velocityTracker!!.yVelocity.toInt()
    }

    private fun reDraw() {
        // curIndex需要偏移的量
        val i = (offsetY / (textHeight + textPadding)).toInt()
        if (isRecycleMode || curIndex - i >= 0 && curIndex - i < mData.size) {
            if (offsetIndex != i) {
                offsetIndex = i

                if (null != onScrollChangedListener)
                    onScrollChangedListener!!.onScrollChanged(getNowIndex(-offsetIndex))
            }
            postInvalidate()
        } else {
            finishScroll()
        }
    }

    private fun finishScroll() {
        // 判断结束滑动后应该停留在哪个位置
        val centerPadding = textHeight + textPadding
        val v = offsetY % centerPadding
        if (v > 0.5f * centerPadding)
            ++offsetIndex
        else if (v < -0.5f * centerPadding)
            --offsetIndex

        // 重置curIndex
        curIndex = getNowIndex(-offsetIndex)

        // 计算回弹的距离
        bounceDistance = offsetIndex * centerPadding - offsetY
        offsetY += bounceDistance

        // 更新
        if (null != onScrollChangedListener)
            onScrollChangedListener!!.onScrollFinished(curIndex)

        // 重绘
        reset()
        postInvalidate()
    }

    private fun getNowIndex(offsetIndex: Int): Int {
        var index = curIndex + offsetIndex
        if (isRecycleMode) {
            if (index < 0)
                index = (index + 1) % mData.size + mData.size - 1
            else if (index > mData.size - 1)
                index %= mData.size
        } else {
            if (index < 0)
                index = 0
            else if (index > mData.size - 1)
                index = mData.size - 1
        }
        return index
    }

    private fun reset() {
        offsetY = 0f
        oldOffsetY = 0f
        offsetIndex = 0
        bounceDistance = 0f
    }

    private fun addVelocityTracker(event: MotionEvent?) {
        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain()

        velocityTracker!!.addMovement(event)
    }

    private fun recycleVelocityTracker() {
        velocityTracker?.recycle()
        velocityTracker = null
    }

    fun setData(data: List<WheelData>?) {
        if (data == null) return

        this.mData.clear()
        this.mData.addAll(data)

        // 更新maxTextWidth
        if (mData.isNotEmpty()) {
            val size = mData.size
            for (i in 0 until size) {
                val tempWidth = textPaint.measureText(mData[i].text)
                if (tempWidth > maxTextWidth)
                    maxTextWidth = tempWidth
            }
            curIndex = 0
        }
        requestLayout()
        invalidate()
    }

    /**
     * 获取当前状态下选中的索引
     */
    fun getCurIndex(): Int {
        return getNowIndex(-offsetIndex)
    }

    /**
     * 滚动到指定位置
     */
    fun moveTo(index: Int) {
        if (index !in mData.indices || curIndex == index) {
            return
        }
        if (!scroller.isFinished) {
            scroller.forceFinished(true)
        }
        finishScroll()
        var dy = 0
        val centerPadding = textHeight + textPadding
        dy = if (!isRecycleMode) {
            (curIndex - index) * centerPadding
        } else {
            val offsetIndex = curIndex - index
            val d1 = Math.abs(offsetIndex) * centerPadding
            val d2 = (mData.size - Math.abs(offsetIndex)) * centerPadding

            when {
                offsetIndex > 0 && d1 < d2 -> d1        // ascent
                offsetIndex > 0 && d1 >= d2 -> -d2       // descent
                offsetIndex < 0 && d1 < d2 -> -d1       // descent
                else -> d2        // ascent
            }
        }
        scroller.startScroll(0, 0, 0, dy, 500)
        invalidate()
    }

    /**
     * 滚动指定的偏移量
     */
    fun moveBy(offset: Int) {
        moveTo(getNowIndex(offset))
    }

    fun setOnScrollChangedListener(listener: OnScrollChangedListener) {
        onScrollChangedListener = listener
    }

    interface OnScrollChangedListener {
        fun onScrollChanged(index: Int)

        fun onScrollFinished(index: Int)
    }

    class WheelData() {
        var index: Int? = null
        var text: String? = null

        constructor(index: Int?, text: String?) : this() {
            this.index = index
            this.text = text
        }
    }
}