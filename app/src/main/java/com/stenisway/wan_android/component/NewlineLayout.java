package com.stenisway.wan_android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class NewlineLayout extends ViewGroup {

    private final String TAG = this.getClass().getName();

    public NewlineLayout(Context context) {
        super(context);
    }

    public NewlineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewlineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewlineLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    //測量需要的長寬高總共是多少
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.EXACTLY) {

            measureChildren(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        } else {
//       如有需要可以擴充其他類型，這邊只有提供 (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.AT_MOST)
            int layoutChildViewCurX = this.getPaddingLeft();

            int totalControlHeight = 0;

            for (int i = 0; i < getChildCount(); i++) {
                final View childView = this.getChildAt(i);
                if (childView.getVisibility() == GONE) {
                    continue;
                }

                final MarginLayoutParams  lpMargin  = (MarginLayoutParams) childView.getLayoutParams();
                childView.measure(
                        getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lpMargin.width),
                        getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lpMargin.height)
                );

                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                if (totalControlHeight == 0) {
                    totalControlHeight = (height + lpMargin.topMargin + lpMargin.bottomMargin);
                }

                if (layoutChildViewCurX + width + lpMargin.leftMargin + lpMargin.rightMargin > sizeWidth) {
                    layoutChildViewCurX = this.getPaddingLeft();
                    totalControlHeight += height + lpMargin.topMargin + lpMargin.bottomMargin;
                }
                layoutChildViewCurX += width + lpMargin.leftMargin + lpMargin.rightMargin;

            }

            int cachedTotalWith = resolveSize(sizeWidth, widthMeasureSpec);
            int cachedTotalHeight = resolveSize(totalControlHeight, modeHeight);

            this.setMeasuredDimension(cachedTotalWith, cachedTotalHeight);

        }

    }
//  規定子物件的放置規則
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int viewGroupWidth = getMeasuredWidth();
        int childCount = getChildCount();

        int layoutChildViewCurX = l; //View的X座標
        int layoutChildViewCurY = 0; //View的Y坐標，若設定成t，子view會距離parent有一段很大的距離

//        繪製所有子物件
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //子控件的宽和高
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
//            取得子物件margin
            final MarginLayoutParams mglp = (MarginLayoutParams) childView.getLayoutParams();

            //如果當前子物件+margin超過ViewGroup的寬度，就會自動換行
            if ((layoutChildViewCurX + width + mglp.leftMargin + mglp.rightMargin) > viewGroupWidth) {
                layoutChildViewCurX = l;
                //如果換行，物件高度要加上之前物件的高度以及margin總量
                layoutChildViewCurY += height + mglp.topMargin + mglp.bottomMargin;
            }
            Log.d(TAG + "layoutMargin", mglp.leftMargin+"");
            childView.layout(
                    layoutChildViewCurX + mglp.leftMargin,
                    layoutChildViewCurY + mglp.topMargin,
                    layoutChildViewCurX + width + mglp.leftMargin + mglp.rightMargin,
                    layoutChildViewCurY + height + mglp.topMargin + mglp.bottomMargin);
            Log.d(TAG + "btMargin", mglp.bottomMargin+"");
            Log.d(TAG + "topMargin", mglp.topMargin+"");
            //一個子物件繪製完後，下個子物件的位子要把前面物件的寬度加上margin算進去
            layoutChildViewCurX += width + mglp.leftMargin + mglp.rightMargin;

        }

    }

}
