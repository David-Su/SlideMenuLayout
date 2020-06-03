package com.davidsu.slidemenulayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Scroller
import kotlin.math.abs

/**
 * @author SuK
 * @time 2020/5/28 14:13
 * @des 左滑显示一个自定义的视图
 */
class SlideMenuLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val mTouchSlop: Int = ViewConfiguration.get(getContext()).scaledTouchSlop
    private var mLastX = 0f
    private var mLastY = 0f
    private var mXdistance = 0f
    private var mYdistance = 0f
    private var mMenu: View? = null
    private var mOpenable = true
    private val mScroller = Scroller(context)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SlideMenuLayout)
        a.getResourceId(R.styleable.SlideMenuLayout_menuLayout, -1).takeIf { it != -1 }
            ?.let { layoutId ->
                LayoutInflater.from(context).inflate(layoutId, this)
                mMenu = getChildAt(0)
            }
        a.recycle()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!valid()) return super.dispatchTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x
                mLastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                mXdistance = ev.x - mLastX
                mYdistance = ev.y - mLastY
                mLastX = ev.x
                mLastY = ev.y
                if (isHorizontalMove(null)) {//判断是一个横向的滑动
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!valid()) return super.onInterceptTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_MOVE -> {
                if (isHorizontalMove(null)) {//判断是一个横向的滑动
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (scrollX < 0) return true
            }
        }
        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!valid()) return super.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                Log.d("onTouchEvent", scrollX.toString())
                if (scrollX >= 0) scrollInRange() else smoothScroll(0)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (scrollX < 0) {
                    smoothScroll(0)
                } else if (scrollX > 0) {
                    if (abs(scrollX) > mMenu!!.width / 2) smoothScroll(mMenu!!.width) else smoothScroll(
                        0
                    )
                }
            }
        }
        return event?.action == MotionEvent.ACTION_DOWN
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mMenu?.let { it.layout(right, it.top, right + it.right, it.bottom) }
    }


    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    private fun smoothScroll(destination: Int) {
        Log.d("smoothScroll", "$scrollX ---> $destination")
        mScroller.startScroll(scrollX, scrollY, destination - scrollX, scrollY)
        invalidate()
    }

    private fun scrollInRange() {
        Log.d("scrollInRange", mMenu!!.javaClass.simpleName)
        val sx = scrollX - mXdistance.toInt()
        scrollTo(if (abs(sx) > mMenu!!.width) mMenu!!.width else sx, 0)
    }

    /**
     * left == null,表示只判断是否横向滑动
     */
    private fun isHorizontalMove(left: Boolean?): Boolean {

        var result: Boolean? = null
        result = if (abs(mXdistance) <= abs(mYdistance)) {
            false
        } else when (left) {
            null -> true
            true -> mXdistance < 0
            false -> mXdistance > 0
        }

        Log.d("isHorizontalMove", abs(mXdistance).toString())
        Log.d("isHorizontalMove", abs(mYdistance).toString())
        Log.d("isHorizontalMove", result.toString() + "\n")

        return result
    }

    private fun valid() = mMenu != null && mOpenable

    fun  <T : View?> getMenuView(): T? = mMenu as T?

    fun closeImmediate() {
        mMenu?.let { scrollX = 0 }
    }

    fun setOpenable(openable: Boolean) {
        if (!openable) closeImmediate()
        mOpenable = openable
    }
}