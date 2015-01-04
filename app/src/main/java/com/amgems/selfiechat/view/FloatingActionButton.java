package com.amgems.selfiechat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.amgems.selfiechat.R;

/**
 * TODO: document your custom view class.
 */
public class FloatingActionButton extends Button {
    private Paint mBackgroundPaint;
    private Paint mButtonPaint;
    private Paint mCrossPaint;

    public FloatingActionButton(Context context) {
        super(context);
        init(null, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.FloatingActionButton, defStyle, 0);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.WHITE);

        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(typedArray.getColor(
                R.styleable.FloatingActionButton_backgroundColor, Color.BLACK));

        mCrossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCrossPaint.setColor(typedArray.getColor(
                R.styleable.FloatingActionButton_foregroundColor,Color.WHITE));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        float radius = getWidth() / 2f;

        canvas.drawRect(0f, 0f, getWidth(), getWidth(), mBackgroundPaint);
        canvas.drawCircle(radius, radius, radius,  mButtonPaint);
        float[] cross =  {
                radius, radius * 0.70f, radius,  radius * 1.30f,
                radius * 0.70f, radius, radius * 1.30f, radius
        };
        mCrossPaint.setStrokeWidth(radius / 10f);
        canvas.drawLines(cross, mCrossPaint);
    }
}
