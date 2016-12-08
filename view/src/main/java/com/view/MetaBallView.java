package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * BezierDragView
 * <p/>
 * Created by DoggyZhang on 2016/12/8 .
 */
public class MetaBallView extends FrameLayout {

    private static final String TAG = "BezierDragView";

    private Path mPath;

    private Paint mPaint;

    private int mPaintColor;

    private static final int RADIUS_MIN = 40;
    private static final int DISTANCE_MAX = 200;
    private float mRadiusNormal;
    private float mStartX;
    private float mStartY;
    private float mDragX;
    private float mDragY;

    private float mStartCircleRadius;
    private float mDragCircleRadius;

    private View mDragView;
    private boolean isDraged = false;

    private ImageView mDisappearView;
    private boolean isDisappeared = false;

    public MetaBallView(Context context) {
        this(context, null);
    }

    public MetaBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MetaBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mPaintColor = 0xffc15c5c;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);

        mPath = new Path();

        mRadiusNormal = RADIUS_MIN;
        mStartCircleRadius = mRadiusNormal;
        mDragCircleRadius = mRadiusNormal;

        mDisappearView = new ImageView(getContext());
        mDisappearView.setImageResource(R.drawable.tip_anim);
        mDisappearView.setVisibility(View.INVISIBLE);

        addView(mDisappearView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStartX = getMeasuredWidth() / 2;
        mStartY = getMeasuredHeight() / 2;
        mDragX = mStartX;
        mDragY = mStartY;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mDragView != null) {
            this.mDragView.setX(mStartX - mDragView.getWidth() / 2);
            this.mDragView.setY(mStartY - mDragView.getHeight() / 2);
        }
        if (mDisappearView != null) {
            if (mDisappearView != null) {
                this.mDisappearView.setLayoutParams(new LayoutParams(
                        (int) (mStartCircleRadius * 2 * getResources().getDisplayMetrics().density),
                        (int) (mStartCircleRadius * 2 * getResources().getDisplayMetrics().density))
                );
            }
            this.mDisappearView.setX(mStartX - mDisappearView.getWidth() / 2);
            this.mDisappearView.setY(mStartY - mDisappearView.getHeight() / 2);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateMetaBall(canvas);

        canvas.drawCircle(mStartX, mStartY, mStartCircleRadius, mPaint);
        canvas.drawCircle(mDragX, mDragY, mDragCircleRadius, mPaint);
        super.onDraw(canvas);
    }

    private void calculateMetaBall(Canvas canvas) {
        float x = mDragX;
        float y = mDragY;
        float startX = mStartX;
        float startY = mStartY;
        float controlX = (startX + x) / 2;
        float controlY = (startY + y) / 2;

        float distance = (float) Math.sqrt((controlX - startX) * (controlX - startX) + (controlY - startY) * (controlY - startY));
        if (distance > DISTANCE_MAX) {
            isDisappeared = true;
            mDisappearView.setVisibility(View.VISIBLE);
            mDisappearView.setImageResource(R.drawable.tip_anim);
            ((AnimationDrawable) mDisappearView.getDrawable()).stop();
            ((AnimationDrawable) mDisappearView.getDrawable()).start();

            if (mDragView != null) {
                mDragView.setVisibility(View.GONE);
            }

            mPaint.setColor(0x00000000);
            return;
        }
        double a = Math.acos(mRadiusNormal / distance);

        double b = Math.acos(Math.abs(controlX - startX) / distance);
        float offsetX1 = (float) Math.abs(mRadiusNormal * Math.cos(Math.abs(a - b)));
        float offsetY1 = (float) Math.abs(mRadiusNormal * Math.sin(Math.abs(a - b)));
        float tanX1 = 0;
        float tanY1 = 0;

        double c = Math.acos(Math.abs(controlY - startY) / distance);
        float offsetX2 = (float) Math.abs(mRadiusNormal * Math.sin(Math.abs(a - c)));
        float offsetY2 = (float) Math.abs(mRadiusNormal * Math.cos(Math.abs(a - c)));
        float tanX2 = 0;
        float tanY2 = 0;

        double d = Math.acos(Math.abs(y - controlY) / distance);
        float offsetX3 = (float) Math.abs(mRadiusNormal * Math.sin(Math.abs(a - d)));
        float offsetY3 = (float) Math.abs(mRadiusNormal * Math.cos(Math.abs(a - d)));
        float tanX3 = 0;
        float tanY3 = 0;

        double e = Math.acos(Math.abs(x - controlX) / distance);
        float offsetX4 = (float) Math.abs(mRadiusNormal * Math.cos(Math.abs(a - e)));
        float offsetY4 = (float) Math.abs(mRadiusNormal * Math.sin(Math.abs(a - e)));
        float tanX4 = 0;
        float tanY4 = 0;

        // right bottom
        if (mDragX > mStartX && mDragY > mStartY) {
            tanX1 = startX + offsetX1;
            tanY1 = startY - offsetY1;

            tanX2 = startX - offsetX2;
            tanY2 = startY + offsetY2;

            tanX3 = x + offsetX3;
            tanY3 = y - offsetY3;

            tanX4 = x - offsetX4;
            tanY4 = y + offsetY4;
        }
        // right up
        else if (mDragX > mStartX && mDragY < mStartY) {
            tanX1 = startX + offsetX1;
            tanY1 = startY + offsetY1;

            tanX2 = startX - offsetX2;
            tanY2 = startY - offsetY2;

            tanX3 = x + offsetX3;
            tanY3 = y + offsetY3;

            tanX4 = x - offsetX4;
            tanY4 = y - offsetY4;
        }
        // left bottom
        else if (mDragX < mStartX && mDragY > mStartY) {
            tanX1 = startX - offsetX1;
            tanY1 = startY - offsetY1;

            tanX2 = startX + offsetX2;
            tanY2 = startY + offsetY2;

            tanX3 = x - offsetX3;
            tanY3 = y - offsetY3;

            tanX4 = x + offsetX4;
            tanY4 = y + offsetY4;
        }
        // left up
        else if (mDragX < mStartX && mDragY < mStartY) {
            tanX1 = startX - offsetX1;
            tanY1 = startY + offsetY1;

            tanX2 = startX + offsetX2;
            tanY2 = startY - offsetY2;

            tanX3 = x - offsetX3;
            tanY3 = y + offsetY3;

            tanX4 = x + offsetX4;
            tanY4 = y - offsetY4;
        }
        // right
        else if (mDragX == mStartX && mDragY > mStartX) {
            tanX1 = startX + offsetX1;
            tanY1 = startY - offsetY1;

            tanX2 = startX + offsetX2;
            tanY2 = startY + offsetY2;

            tanX3 = x - offsetX3;
            tanY3 = y - offsetY3;

            tanX4 = x - offsetX4;
            tanY4 = y + offsetY4;
        }
        // left
        else if (mDragX == mStartX && mDragY < mStartX) {
            tanX1 = startX - offsetX1;
            tanY1 = startY + offsetY1;

            tanX2 = startX - offsetX2;
            tanY2 = startY - offsetY2;

            tanX3 = x + offsetX3;
            tanY3 = y + offsetY3;

            tanX4 = x + offsetX4;
            tanY4 = y - offsetY4;
        }
        //up
        else if (mDragX == mStartX && mDragY > mStartX) {
            tanX1 = startX - offsetX1;
            tanY1 = startY - offsetY1;

            tanX2 = startX + offsetX2;
            tanY2 = startY - offsetY2;

            tanX3 = x - offsetX3;
            tanY3 = y + offsetY3;

            tanX4 = x + offsetX4;
            tanY4 = y + offsetY4;
        }
        // down
        else if (mDragX == mStartX && mDragY > mStartX) {
            tanX1 = startX + offsetX1;
            tanY1 = startY + offsetY1;

            tanX2 = startX - offsetX2;
            tanY2 = startY + offsetY2;

            tanX3 = x + offsetX3;
            tanY3 = y - offsetY3;

            tanX4 = x - offsetX4;
            tanY4 = y - offsetY4;
        }
        // center
        else {
            tanX1 = startX;
            tanY1 = startY;

            tanX2 = startX;
            tanY2 = startY;

            tanX3 = x;
            tanY3 = y;

            tanX4 = x;
            tanY4 = y;
        }

        mPath.reset();
        mPath.moveTo(tanX1, tanY1);
        mPath.quadTo(controlX, controlY, tanX3, tanY3);
        mPath.lineTo(tanX4, tanY4);
        mPath.quadTo(controlX, controlY, tanX2, tanY2);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDisappeared) {
            return super.onTouchEvent(event);
        }
        mDragX = event.getX();
        mDragY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mDragView != null) {
                    Rect rect = new Rect();
                    int[] location = new int[2];
                    mDragView.getDrawingRect(rect);
                    mDragView.getLocationOnScreen(location);
                    rect.left = location[0];
                    rect.top = location[1];
                    rect.right = rect.right + location[0];
                    rect.bottom = rect.bottom + location[1];
                    if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        isDraged = true;
                    }
                }
            case MotionEvent.ACTION_MOVE:
                if (isDraged) {
                    if (mDragView != null) {
                        mDragView.setX(mDragX - mDragView.getWidth() / 2);
                        mDragView.setY(mDragY - mDragView.getHeight() / 2);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDraged = false;
                mDragX = mStartX;
                mDragY = mStartY;
                if (mDragView != null) {
                    mDragView.setX(mStartX - mDragView.getWidth() / 2);
                    mDragView.setY(mStartY - mDragView.getHeight() / 2);
                }
                invalidate();
                break;
        }
        return true;
    }

    public int getColor() {
        return mPaintColor;
    }

    public void setColor(int color) {
        this.mPaintColor = color;
        this.mPaint.setColor(this.mPaintColor);
        if (!isDisappeared) {
            invalidate();
        }
    }

    public View getDragView() {
        return mDragView;
    }

    public void setDragView(View dragView) {
        this.mDragView = dragView;
        if (dragView != null) {
            this.mDragView.setLayoutParams(new LayoutParams(
                    (int) (mDragCircleRadius * 2 * getResources().getDisplayMetrics().density),
                    (int) (mDragCircleRadius * 2 * getResources().getDisplayMetrics().density))
            );
            addView(this.mDragView);
        }
    }

    public float getCircleOneRadius() {
        return mStartCircleRadius;
    }

    public void setCircleOneRadius(float circleOneRadius) {
        mStartCircleRadius = circleOneRadius;
    }

    public float getCircleTwoRadius() {
        return mDragCircleRadius;
    }

    public void setCircleTwoRadius(float circleTwoRadius) {
        mDragCircleRadius = circleTwoRadius;
    }

    public float getCircleOneX() {
        return mStartX;
    }

    public void setCircleOneX(float circleOneX) {
        mStartX = circleOneX;
    }

    public float getCircleOneY() {
        return mStartY;
    }

    public void setCircleOneY(float circleOneY) {
        mStartY = circleOneY;
    }

    public float getCircleTwoX() {
        return mDragX;
    }

    public void setCircleTwoX(float circleTwoX) {
        mDragX = circleTwoX;
    }

    public float getCircleTwoY() {
        return mDragY;
    }

    public void setCircleTwoY(float circleTwoY) {
        mDragY = circleTwoY;
    }
}
