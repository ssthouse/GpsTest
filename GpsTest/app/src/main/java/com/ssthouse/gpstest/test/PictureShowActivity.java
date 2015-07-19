package com.ssthouse.gpstest.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.ssthouse.gpstest.R;

import java.io.InputStream;

/**
 * 在水里还图片ACtivity
 * Created by ssthouse on 2015/7/18.
 */
public class PictureShowActivity extends Activity {

    private static final String TAG = "PrjEditActivity";
    private ImageView image_spacePage;
    private Bitmap bitmap;

    public static void start(Context context){
        context.startActivity(new Intent(context, PictureShowActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picture_show);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        image_spacePage = (ImageView) findViewById(R.id.id_iv);
        image_spacePage.setOnTouchListener(new MultiPointTouchListener(image_spacePage));
        image_spacePage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        Boolean fromSdCard = false;
        if (!fromSdCard) {

            InputStream is = this.getResources().openRawResource(R.raw.english);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;   //width，hight设为原来的十分一
            bitmap =BitmapFactory.decodeStream(is,null,options);
//            Resources res = getResources();
//            bitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.english));
        } else {
            String picPath = Environment.getExternalStorageDirectory().toString() + "/Pictures/" + "english_description.jpg";
            bitmap = BitmapFactory.decodeFile(picPath);
        }

        if (null != bitmap) {
            image_spacePage.setImageBitmap(bitmap);
        }else{
            Log.e(TAG, "somethiing is wrong");
        }
    }

    public class MultiPointTouchListener implements View.OnTouchListener {
        Matrix matrix = new Matrix();
        Matrix savedMatrix = new Matrix();

        public ImageView image;
        static final int NONE = 0;
        static final int DRAG = 1;
        static final int ZOOM = 2;
        int mode = NONE;

        PointF start = new PointF();
        PointF mid = new PointF();
        float oldDist = 1f;

        public MultiPointTouchListener(ImageView image) {
            super();
            this.image = image;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            this.image.setScaleType(ImageView.ScaleType.MATRIX);

            ImageView view = (ImageView) v;

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:

                    Log.i(TAG, "ACTION_DOWN");
                    matrix.set(view.getImageMatrix());
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.i(TAG, "ACTION_POINTER_DOWN");
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP");
                case MotionEvent.ACTION_POINTER_UP:
                    Log.i(TAG, "ACTION_POINTER_UP");
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "ACTION_MOVE");
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(matrix);
            return true;
        }

        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return FloatMath.sqrt(x * x + y * y);
        }

        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();   //回收图片所占的内存
                bitmap = null;
                System.gc();  //提醒系统及时回收
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();   //回收图片所占的内存
                bitmap = null;
                System.gc();  //提醒系统及时回收
            }
        }
    }
}
