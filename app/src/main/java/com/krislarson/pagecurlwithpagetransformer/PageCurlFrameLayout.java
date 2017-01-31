package com.krislarson.pagecurlwithpagetransformer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class PageCurlFrameLayout extends FrameLayout implements PageCurl {

   private static final String TAG = "PageCurlFrameLayout";

   private Path mClipPath;

   private Path mCurlPath;

   private Paint mCurlStrokePaint;

   private Paint mCurlFillPaint;

   public PageCurlFrameLayout(Context context) {
      super(context);
      init();
   }

   public PageCurlFrameLayout(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
   }

   public PageCurlFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      init();
   }

   @TargetApi(21)
   public PageCurlFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      init();
   }

   private void init() {

      if (Build.VERSION.SDK_INT < 18) {  // clipPath() not supported in hardware accel
         setLayerType(LAYER_TYPE_SOFTWARE, null);
      }

      mCurlStrokePaint = new Paint();
      mCurlStrokePaint.setStyle(Paint.Style.STROKE);
      mCurlStrokePaint.setStrokeWidth(3.0F);
      mCurlStrokePaint.setColor(Color.BLACK);

      mCurlFillPaint = new Paint();
      mCurlFillPaint.setStyle(Paint.Style.FILL);
      mCurlFillPaint.setColor(Color.WHITE);

      mCurlPath = new Path();

      mClipPath = new Path();
   }

   @Override
   public void setCurlFactor(float curl) {

      Log.d(TAG, "setCurlFactor, curl = " + curl + ", page = " + (Integer) getTag(R.id.viewpager));

      int w = getWidth();
      int h = getHeight();

      PointF a = new PointF(w * curl, h);
      PointF b = new PointF(w, h);
      PointF c = new PointF(w, 0);
      PointF d = new PointF(w * curl, 0);

      PointF e = new PointF(w * curl / 2.0F, h);
      PointF f = new PointF(w, h);
      PointF g = new PointF(w, 0);
      PointF j = new PointF(w * curl / 2.0F, 0);

      mClipPath.reset();
      mClipPath.moveTo(e.x, e.y);
      mClipPath.lineTo(f.x, f.y);
      mClipPath.lineTo(g.x, g.y);
      mClipPath.lineTo(j.x, j.y);
      mClipPath.close();

      mCurlPath.reset();
      mCurlPath.moveTo(a.x, a.y);
      mCurlPath.lineTo(b.x, b.y);
      mCurlPath.lineTo(c.x, c.y);
      mCurlPath.lineTo(d.x, d.y);
      mCurlPath.close();

      invalidate();
   }

   @Override
   protected void dispatchDraw(Canvas canvas) {
      canvas.save();
      canvas.clipPath(mClipPath);
      super.dispatchDraw(canvas);
      canvas.restore();
   }

   @Override
   public void onDrawForeground(Canvas canvas) {
      super.onDrawForeground(canvas);
      canvas.drawPath(mCurlPath, mCurlFillPaint);
      canvas.drawPath(mCurlPath, mCurlStrokePaint);
   }
}
