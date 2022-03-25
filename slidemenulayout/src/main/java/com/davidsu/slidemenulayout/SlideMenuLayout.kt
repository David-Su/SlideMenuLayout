package com.davidsu.slidemenulayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Scroller
import java.lang.Exception
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
    private val mScroller = Scroller(context)
    private var mIsMove = false //是否有划动


    private var mOpenable = true
    private var mClickMenuAutoClose = true

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
        Log.d("dispatchTouchEvent", ev?.action.toString())
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x
                mLastY = ev.y
                mIsMove = false
            }
            MotionEvent.ACTION_MOVE -> {
                mXdistance = ev.x - mLastX
                mYdistance = ev.y - mLastY
                mLastX = ev.x
                mLastY = ev.y
                mIsMove = true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!valid()) return super.onInterceptTouchEvent(ev)
        Log.d("onInterceptTouchEvent", ev?.action.toString())

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (scrollX == mMenu!!.width && mLastX < width - mMenu!!.width) { //1.如果菜单已经完全展开并且没有点击菜单，那么菜单应该收回，并且子View也不需要处理这次触摸系列事件。
                    smoothScrollTo(0)
                    parent.requestDisallowInterceptTouchEvent(true)
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isHorizontalMove(null)) {//2.判断是一个横向的滑动
                    parent.requestDisallowInterceptTouchEvent(true)
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (scrollX == mMenu!!.width && mClickMenuAutoClose) { //来到这里，证明不符合1，2，而且有子View消费了DOWN事件，正准备要响应点击事件。如果此时菜单打开，则应该关闭。
                    smoothScrollTo(0)
                }
            }
            //为什么ACTION_UP拦截了之后，这个事件不会去到onTouchEvent？有大神能解释吗？
        }
        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("onTouchEvent", event?.action.toString())
        if (!valid()) return super.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                scrollInRange()
//                if (scrollX >= 0) scrollInRange() else smoothScrollTo(0)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (scrollX < 0) {
                    smoothScrollTo(0)
                } else if (scrollX > 0) {
                    when {
                        scrollX > mMenu!!.width / 2 -> {
                            smoothScrollTo(mMenu!!.width)
                        }
                        scrollX < mMenu!!.width / 2 -> {
                            smoothScrollTo(0)
                        }
                    }
                } else {
                    performClick()
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

    private fun smoothScrollTo(destination: Int) {
        if (!mScroller.isFinished) return
        mScroller.startScroll(scrollX, scrollY, destination - scrollX, scrollY)
        invalidate()
    }

    private fun scrollInRange() {
        val sx = scrollX - mXdistance.toInt()
        scrollTo(if (sx > mMenu!!.width) mMenu!!.width else if (sx >= 0) sx else 0, 0)
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
//        Log.d("isHorizontalMove", abs(mXdistance).toString())
//        Log.d("isHorizontalMove", abs(mYdistance).toString())
//        Log.d("isHorizontalMove", result.toString() + "\n")
        return result
    }

    private fun valid() = mMenu != null && mOpenable

    fun <T : View?> getMenuView(): T? = mMenu as T?


    /**
     * 立刻关闭菜单，没有滑动效果
     */
    fun closeImmediate() {
        mMenu?.let { scrollX = 0 }
    }

    /**
     * 立刻关闭菜单，没有滑动效果
     */
    fun close() {
        smoothScrollTo(0)
    }


    /**
     * 设置点击菜单是否自动关闭
     */
    fun setClickMenuAutoClose(auto: Boolean) {
        mClickMenuAutoClose = auto
    }

    fun setOpenable(openable: Boolean) {
        if (!openable) closeImmediate()
        mOpenable = openable
    }
}