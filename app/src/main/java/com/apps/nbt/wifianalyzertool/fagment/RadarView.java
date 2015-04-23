package com.apps.nbt.wifianalyzertool.fagment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.apps.nbt.wifianalyzertool.R;
import com.apps.nbt.wifianalyzertool.activity.WifiConnector;

import java.util.ArrayList;

/**
 * Created by Tiennb on 4/23/2015.
 */
public class RadarView extends View {

    /**
     * True when we have know our own location
     */
    private boolean mHaveWifiList = false;

    /**
     * The bitmap used to draw the target
     */
    private Bitmap mBlip;

    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint0;
    private Paint mNameWifiPaint;

    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint1;

    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint2;

    /**
     * Time in millis when the most recent sweep began
     */
    private long mSweepTime;

    /**
     * True if the sweep has not yet intersected the blip
     */
    private boolean mSweepBefore;

    /**
     * Time in millis when the sweep last crossed the blip
     */
    private long mBlipTime;

    private Paint mGridPaint;

    private Canvas mCanvas;
    /**
     * Ratio of the distance to the target to the radius of the outermost ring on the radar screen
     */
    private float mDistanceRatio;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mNameWifiPaint = new Paint();
        mNameWifiPaint.setColor(0xFFFFFFFF);
        mNameWifiPaint.setTextSize(20);
//        mNameWifiPaint.setTextAlign(Paint.Align.CENTER);

        // Outer ring of the sweep
        mSweepPaint0 = new Paint();
        mSweepPaint0.setColor(0xFFFFFFFF);
        mSweepPaint0.setAntiAlias(true);
        mSweepPaint0.setStyle(Paint.Style.STROKE);
        mSweepPaint0.setStrokeWidth(2f);

        // Middle ring of the sweep
        mSweepPaint1 = new Paint();
        mSweepPaint1.setColor(0x77FFFFFF);
        mSweepPaint1.setAntiAlias(true);
        mSweepPaint1.setStyle(Paint.Style.STROKE);
        mSweepPaint1.setStrokeWidth(2f);

        // Inner ring of the sweep
        mSweepPaint2 = new Paint();
        mSweepPaint2.setColor(0x33FFFFFF);
        mSweepPaint2.setAntiAlias(true);
        mSweepPaint2.setStyle(Paint.Style.STROKE);
        mSweepPaint2.setStrokeWidth(2f);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;

        int x = getWidth() / 2;
        int y = getHeight() / 2;

        int radius = center - 8;

        int blipRadius = (int) (1.0 * radius);

        final long now = SystemClock.uptimeMillis();
        if (mSweepTime > 0) {
            // Draw the sweep. Radius is determined by how long ago it started
            long sweepDifference = now - mSweepTime;
            if (sweepDifference < 512L) {
                int sweepRadius = (int) (((radius + 6) * sweepDifference) >> 9);
                canvas.drawCircle(x, y, sweepRadius, mSweepPaint0);
                canvas.drawCircle(x, y, sweepRadius - 2, mSweepPaint1);
                canvas.drawCircle(x, y, sweepRadius - 4, mSweepPaint2);

                // Note when the sweep has passed the blip
                boolean before = sweepRadius < blipRadius;
                if (!before && mSweepBefore) {
                    mSweepBefore = false;
                    mBlipTime = now;
                }
            } else {
                mSweepTime = now + 1000;
                mSweepBefore = true;
            }
            postInvalidate();
        }


        if (mHaveWifiList) {

            for (WifiConnector wifi : mWifiList) {

                int pix = wifi.getDistance() * 5;
                Point p = getWifiCoordinator(wifi.getAngle(), pix);

                int xPix = x + p.x - 8;
                int yPix = y + p.y - 8;


                if (wifi.isConnected()) {

                    mBlip = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_wifi_radar_connected)).getBitmap();

                } else {
                    mBlip = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_wifi_radar_scan)).getBitmap();
                }

                canvas.drawText(wifi.getWifiName(), xPix, yPix,mNameWifiPaint);

                canvas.drawBitmap(mBlip, xPix,
                        yPix, mNameWifiPaint);

            }
        }
    }

    private Point getWifiCoordinator(int angle, int radius) {

        Point p = new Point();

        p.y = (int) ((double) radius * (Math.sin(angle)));

        p.x = (int) Math.sqrt(radius * radius - p.y * p.y);

//        Log.d("Tiennb", p.x +" - " + p.y + "   "+ radius);

        return p;
    }

    /**
     * Turn on the sweep animation starting with the next draw
     */
    public void startSweep() {
        mSweepTime = SystemClock.uptimeMillis();
        mSweepBefore = true;
    }

    /**
     * Turn off the sweep animation
     */
    public void stopSweep() {
        mSweepTime = 0L;
    }

    public void hasUnknownWifiList() {
        mHaveWifiList = false;
    }


    ArrayList<WifiConnector> mWifiList;

    public void updateRadar(ArrayList<WifiConnector> wifiList) {
        // 10m ~ 50px

        if (wifiList != null && wifiList.size() > 0) {

            mHaveWifiList = true;
            mWifiList = wifiList;
        }

        invalidate();
    }

    //

    private OnClickWifiListener mOnClickWifiListener;
    public void setOnClickWifiListener(OnClickWifiListener onClickWifiListener){
        mOnClickWifiListener=onClickWifiListener;
    }

    public interface OnClickWifiListener{
        public void onClicked(String name);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
