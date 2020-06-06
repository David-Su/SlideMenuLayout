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
    private var mIsMove = false //是否有划动

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
                mIsMove = false
            }
            MotionEvent.ACTION_MOVE -> {
                mXdistance = ev.x - mLastX
                mYdistance = ev.y - mLastY
                mLastX = ev.x
                mLastY = ev.y
                if (isHorizontalMove(null)) {//判断是一个横向的滑动
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                mIsMove = true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!valid()) return super.onInterceptTouchEvent(ev)
        Log.d("onInterceptTouchEvent", ev?.action.toString())
        when (ev?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                if (scrollX == mMenu!!.width && mLastX < width - mMenu!!.width) { //如果判断手指点下的那一刻菜单已经完全展开，那么菜单应该收回，并且子View也不需要处理这次触摸系列事件。
//                    parent.requestDisallowInterceptTouchEvent(true)
//                    smoothScrollTo(0)
//                    return true
//                }
//            }
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
        Log.d("onTouchEvent", event?.action.toString())
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {

                if (scrollX >= 0) scrollInRange() else smoothScrollTo(0)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (scrollX < 0) {
                    smoothScrollTo(0)
                } else if (scrollX > 0) {

                    if (mIsMove) {
                        when {
                            scrollX > mMenu!!.width / 2 -> {
                                smoothScrollTo(mMenu!!.width)
                            }
                            scrollX < mMenu!!.width / 2 -> {
                                smoothScrollTo(0)
                            }
                        }
                    }else{
                        when (event.action){
                            MotionEvent.ACTION_UP-> {
                                if (scrollX == mMenu!!.width ) smoothScrollTo(0)
                            }
                            MotionEvent.ACTION_CANCEL ->{

                            }
                        }
                    }
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
        Log.d("smoothScroll", "${mScroller.isFinished}]")
        if (!mScroller.isFinished) return
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

    fun <T : View?> getMenuView(): T? = mMenu as T?


    fun closeImmediate() {
        mMenu?.let { scrollX = 0 }
    }

    fun setOpenable(openable: Boolean) {
        if (!openable) closeImmediate()
        mOpenable = openable
    }
}