package com.example.clockdraw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

/**
 * TODO: document your custom view class.
 */
public class Clock extends View {
    /** height, width of the clock's view */
    private int mHeight, mWidth = 0;

    /** numeric numbers to denote the hours */
    private int[] mClockHours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    /** spacing and padding of the clock-hands around the clock round */
    private int mPadding = 0;
    private int mNumeralSpacing = 0;

    /** truncation of the heights of the clock-hands,
     hour clock-hand will be smaller comparetively to others */
    private int mHandTruncation, mHourHandTruncation = 0;

    /** others attributes to calculate the locations of hour-points */
    private int mRadius = 0;
    private Paint mPaint;
    private Rect mRect = new Rect();
    private boolean isInit;  // it will be true once the clock will be initialized.

    public Clock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Clock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /** initialize necessary values */
        if (!isInit) {
            mPaint = new Paint();
            mHeight = getHeight();
            mWidth = getWidth();
            mPadding = mNumeralSpacing + 50;  // spacing from the circle border
            int minAttr = Math.min(mHeight, mWidth);
            mRadius = minAttr / 2 - mPadding;

            // for maintaining different heights among the clock-hands
            mHandTruncation = minAttr / 20;
            mHourHandTruncation = minAttr / 17;

            isInit = true;  // set true once initialized
        }

        /** draw the canvas-color */
        canvas.drawColor(Color.parseColor("#333333"));
        /** circle border */
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mPadding - 10, mPaint);

        /** clock-center */
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 12, mPaint);  // the 03 clock hands will be rotated from this center point.
        /** Draw markers*/
        mPaint.setColor(Color.WHITE);
        drawMarkers(canvas);

        /** border of hours */

        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);  // set font size (optional)

        for (int hour : mClockHours) {
            String tmp = String.valueOf(hour);
            mPaint.getTextBounds(tmp, 0, tmp.length(), mRect);  // for circle-wise bounding

            // find the circle-wise (x, y) position as mathematical rule
            double angle = Math.PI / 6 * (hour - 3);
            int x = (int) (mWidth / 2 + Math.cos(angle) * (mRadius*0.9) - mRect.width() / 2);
            int y = (int) (mHeight / 2 + Math.sin(angle) * (mRadius*0.9) + mRect.height() / 2);

            canvas.drawText(String.valueOf(hour), x, y, mPaint);  // you can draw dots to denote hours as alternative
        }
        /** draw clock hands to represent the every single time */

        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY) % 12 + calendar.get(Calendar.MINUTE) / 60f;
        drawHand(canvas, hour * 30, mRadius - 120, 15, 6, Color.WHITE);
        float minute = calendar.get(Calendar.MINUTE);
        drawHand(canvas, minute * 6, mRadius - 40, 12, 4, Color.WHITE);
        float second = calendar.get(Calendar.SECOND);
        drawHand(canvas, second * 6, mRadius - 20, 10, 2, Color.RED);

        /** invalidate the appearance for next representation of time  */
        postInvalidateDelayed(500);
        invalidate();
    }
    private void drawHand(Canvas canvas, double angle, int length, int baseWidth, int tipWidth, int color) {
        mPaint.reset();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);  // Enable anti-aliasing for smooth edges
        mPaint.setStyle(Paint.Style.STROKE);  // Draw strokes for the hand

        // Convert angle to radians and adjust for 12 o'clock being at -90 degrees
        double radians = Math.toRadians(angle - 90);

        // Create a Path for the hand
        Path handPath = new Path();

        // Start point at the center of the clock
        handPath.moveTo(mWidth / 2, mHeight / 2);

        // Calculate the coordinates of the tip of the hand
        int tipX = (int) (mWidth / 2 + length * Math.cos(radians));
        int tipY = (int) (mHeight / 2 + length * Math.sin(radians));

        // Set up gradient stroke width based on distance from the center
        mPaint.setStrokeWidth(baseWidth);  // Start with the base width at the center

        // Draw the line with a gradually decreasing stroke width
        for (int i = 0; i < length; i += 10) {
            // Calculate the interpolated width from base to tip
            float currentWidth = baseWidth - ((float) i / length) * (baseWidth - tipWidth);

            // Set the stroke width dynamically
            mPaint.setStrokeWidth(currentWidth);

            // Calculate the intermediate points between the center and the tip
            int intermediateX = (int) (mWidth / 2 + i * Math.cos(radians));
            int intermediateY = (int) (mHeight / 2 + i * Math.sin(radians));

            // Draw the segment
            canvas.drawLine(mWidth / 2, mHeight / 2, intermediateX, intermediateY, mPaint);
        }

        // Draw the final segment from the last intermediate point to the tip
        mPaint.setStrokeWidth(tipWidth);  // Set to the final tip width
        canvas.drawLine(mWidth / 2, mHeight / 2, tipX, tipY, mPaint);
    }

    // Draw the hour and minute markers
    private void drawMarkers(Canvas canvas) {
        // Draw the hour markers
        mPaint.setStrokeWidth(8);
        for (int i = 0; i < 12; i++) {
            int startX = (int) (mWidth / 2 + (mRadius + 15) * Math.cos(Math.toRadians(i * 30)));
            int startY = (int) (mHeight / 2 + (mRadius + 15) * Math.sin(Math.toRadians(i * 30)));
            int stopX = (int) (mWidth / 2  + (mRadius - 15) * Math.cos(Math.toRadians(i * 30)));
            int stopY = (int) (mHeight / 2 + (mRadius - 15) * Math.sin(Math.toRadians(i * 30)));
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }
        // Draw the minute markers (shorter markers)
        mPaint.setStrokeWidth(4);
        for (int i = 0; i < 60; i++) {
            // Skip drawing if it's an hour position
            if (i % 5 == 0) continue;

            int startX = (int) (mWidth / 2 + (mRadius + 15) * Math.cos(Math.toRadians(i * 6)));
            int startY = (int) (mHeight / 2 + (mRadius + 15) * Math.sin(Math.toRadians(i * 6)));
            int stopX = (int) (mWidth / 2 + (mRadius - 8) * Math.cos(Math.toRadians(i * 6)));
            int stopY = (int) (mHeight / 2 + (mRadius - 8) * Math.sin(Math.toRadians(i * 6)));
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }
    }
}