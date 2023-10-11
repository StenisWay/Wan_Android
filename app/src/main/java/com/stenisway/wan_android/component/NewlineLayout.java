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
//            (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.AT_MOST)
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

//                MarginLayoutParams lpMargin =  null;

//                if (lp instanceof ViewGroup.MarginLayoutParams){
//                    lpMargin = (ViewGroup.MarginLayoutParams)lp;
//                }else {
//                    lpMargin = new MarginLayoutParams(lp);
//                }
//                lpMargin.setMargins(30, 0, 30, 0);
//                childView.setLayoutParams(lpMargin);
                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                if (totalControlHeight == 0) {
                    totalControlHeight = (height + lpMargin.topMargin + lpMargin.bottomMargin);
                }

                //如果剩余控件不够，则移到下一行开始位置
                if (layoutChildViewCurX + width + lpMargin.leftMargin + lpMargin.rightMargin > sizeWidth) {
                    layoutChildViewCurX = this.getPaddingLeft();
                    totalControlHeight += height + lpMargin.topMargin + lpMargin.bottomMargin;
                }
                layoutChildViewCurX += width + lpMargin.leftMargin + lpMargin.rightMargin;

            }

            //最后确定整个布局的高度和宽度
            int cachedTotalWith = resolveSize(sizeWidth, widthMeasureSpec);
            int cachedTotalHeight = resolveSize(totalControlHeight, modeHeight);

            this.setMeasuredDimension(cachedTotalWith, cachedTotalHeight);


//        } else if (modeWidth == MeasureSpec.AT_MOST && modeHeight == MeasureSpec.AT_MOST) {
//
//            //如果宽高都是Wrap-Content
//            int layoutChildViewCurX = this.getPaddingLeft();
//            //总宽度和总高度
//            int totalControlWidth = 0;
//            int totalControlHeight = 0;
//            //由于宽度是非固定的，所以用一个List接收每一行的最大宽度
//            List<Integer> lineLenghts = new ArrayList<>();
//
//
//
//            for (int i = 0; i < getChildCount(); i++) {
//                final View childView = this.getChildAt(i);
//                if (childView.getVisibility() == GONE) {
//                    continue;
//                }
//
//                final LayoutParams lp = (LayoutParams) childView.getLayoutParams();
//                childView.measure(
//                        getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width),
//                        getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lp.height)
//                );
//                final MarginLayoutParams lpMargin = (MarginLayoutParams) childView.getLayoutParams();
//                int width = childView.getMeasuredWidth();
//                int height = childView.getMeasuredHeight();
//
//                if (totalControlHeight == 0) {
//                    totalControlHeight = height + lpMargin .topMargin + lpMargin .bottomMargin;
//                }
//
//                //如果剩余控件不够，则移到下一行开始位置
//                if (layoutChildViewCurX + width + lpMargin .leftMargin + lpMargin .rightMargin > sizeWidth) {
//                    lineLenghts.add(layoutChildViewCurX);
//                    layoutChildViewCurX = this.getPaddingLeft();
//                    totalControlHeight += height + lpMargin .topMargin + lpMargin .bottomMargin;
//                }
//                layoutChildViewCurX += width + lpMargin.leftMargin + lpMargin .rightMargin;
//
//            }
//
//            //计算每一行的宽度，选出最大值
////            YYLogUtils.w("每一行的宽度 ：" + lineLenghts.toString());
//            totalControlWidth = Collections.max(lineLenghts);
////            YYLogUtils.w("选出最大宽度 ：" + totalControlWidth);
//
//            //最后确定整个布局的高度和宽度
//            int cachedTotalWith = resolveSize(totalControlWidth, widthMeasureSpec);
//            int cachedTotalHeight = resolveSize(totalControlHeight, heightMeasureSpec);
//
//            this.setMeasuredDimension(cachedTotalWith, cachedTotalHeight);
//
//        }else {
//            measureChildren(widthMeasureSpec, heightMeasureSpec);
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int viewGroupWidth = getMeasuredWidth();
        int childCount = getChildCount();

        int layoutChildViewCurX = l; //当前绘制View的X坐标
        int layoutChildViewCurY = 0; //当前绘制View的Y坐标


        //遍历所有子控件，并在其位置上绘制子控件
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //子控件的宽和高
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
            final MarginLayoutParams mglp = (MarginLayoutParams) childView.getLayoutParams();
//            childView.setPadding(20, 20, 20, 20);
//            MarginLayoutParams mglp = new MarginLayoutParams(lp);


            //如果剩余控件不够，则移到下一行开始位置
            if ((layoutChildViewCurX + width + mglp.leftMargin + mglp.rightMargin) > viewGroupWidth) {
                layoutChildViewCurX = l;
                //如果换行，则需要修改当前绘制的高度位置
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
            //布局完成之后，下一次绘制的X坐标需要加上宽度
            layoutChildViewCurX += width + mglp.leftMargin + mglp.rightMargin;

            //执行childView的布局与绘制(右和下的位置加上自身的宽高即可)
//            childView.layout(layoutChildViewCurX, layoutChildViewCurY, layoutChildViewCurX + width, layoutChildViewCurY + height);

            //布局完成之后，下一次绘制的X坐标需要加上宽度
//            layoutChildViewCurX += width;
        }



    }

}
