package com.example.lcit.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${qiuweizhong} on 2017/3/16.
 */
public class MyCircle extends View {

    private Paint mPaint;
    private int mColor;

    //new的时候代码里面调用
    public MyCircle(Context context) {
        this(context, null);
    }

    //xml文件里面调用
    public MyCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

        //用到自定义属性的时候调用
    public MyCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, defStyleAttr);
        //解析自定义属性
//        context.obtainStyledAttributes(attrs, R.style);
        // 加载自定义属性集合CircleView
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        mColor = a.getColor(R.styleable.CircleView_circle_color, Color.RED);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(50f);//画笔的边距
        mPaint.setStyle(Paint.Style.STROKE);//设置填充样式
    }

    // 复写onDraw()进行绘制
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // 获取传入的padding值,使设置padding生效
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();


        // 获取绘制内容的高度和宽度（考虑了四个方向的padding值）
        int width = getWidth() - paddingLeft - paddingRight ;
        int height = getHeight() - paddingTop - paddingBottom ;

        // 设置圆的半径 = 宽,高最小值的2分之1
        int r = Math.min(width, height)/2;

        // 圆心 = 控件的中央,半径 = 宽,高最小值的2分之1
        canvas.drawCircle(paddingLeft+width/2,paddingTop+height/2,r,mPaint);

    }

    /**
     * 使wrap_content生效就要这个方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = 400;
        int mHeight = 400;

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }

}
