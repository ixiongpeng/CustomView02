package com.example.xiongpeng.customview02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by xiongpeng on 2017/9/7.
 */

public class CustomImageView extends View {

    public static int IMAG_SCALL_FIXY = 0;
    public static int CENTER = 1;

    Bitmap mImage;
    int mImageScale;
    String mTitle;
    int mTextColor;
    int mTextSize;
    Rect mRect;
    Rect mTextBound;
    Paint mPaint;
    int mWidth;
    int mHeight;
    Canvas canvas;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int count = a.getIndexCount();
        for(int i = 0; i < count; i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitle = a.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                case R.styleable.CustomImageView_titleTextSize:
                    mTextSize = a.getInt(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
            }
            a.recycle();
            mRect = new Rect();
            mPaint = new Paint();
            mTextBound = new Rect();
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int speceSize = MeasureSpec.getSize(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            mWidth = speceSize;
        }else{
            int desiredByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            int desiredByTitle = getPaddingLeft() + getPaddingRight() + mTextBound.width();

            if(specMode == MeasureSpec.AT_MOST){
                int desire = Math.max(desiredByImg, desiredByTitle);
                mWidth = Math.min(desire,speceSize);
            }
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        speceSize = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            mHeight = speceSize;
        }else{
            int desired = getBottom() + getPaddingTop() + mImage.getHeight() + mTextBound.height();
            mHeight = Math.min(desired, speceSize);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mRect.left = getPaddingLeft();
        mRect.top = getPaddingTop();
        mRect.right = mWidth - getPaddingRight();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        if(mTextBound.width() > mWidth){
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitle, paint, (float)mWidth - getPaddingRight() - getPaddingLeft(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        }else{
            canvas.drawText(mTitle, mWidth / 2 - mTextBound.width() * 1.0f / 2, mHeight / 2 - mTextBound.height() * 1.0f / 2, mPaint);
            mRect.bottom -= mTextBound.height();

            if(mImageScale == IMAG_SCALL_FIXY){
                mRect.left = mWidth / 2 - mImage.getWidth() / 2 ;
                mRect.right = mWidth / 2 + mImage.getWidth() / 2;
                mRect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
                mRect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;
                canvas.drawBitmap(mImage, null, mRect, mPaint);
            }

        }


    }
}
