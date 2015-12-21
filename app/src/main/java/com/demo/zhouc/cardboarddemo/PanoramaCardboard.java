package com.demo.zhouc.cardboarddemo;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

import com.google.vrtoolkit.cardboard.CardboardActivity;

public class PanoramaCardboard extends CardboardActivity {
    private final String LOG_TAG = PanoramaCardboard.class.getSimpleName();
    // directory for input images
    private static final String mStoragePath = "CardBoardDemo";
    private static final String mLeftImageName = "left.jpg";
    private static final String mRightImageName = "right.jpg";

    private String mExtLeftImg = "";
    private String mExtRightImg = "";
    private boolean mIsExtImageExist = false;
    private boolean mIsUseOrgImage = true;

    // sensor manager
    private SensorManager mSensorManager;
    // sensor accelerometer & magnetic
    private Sensor mAccelerometer;
    private Sensor mMagnetic;
    // current sensor values
    private float[] mVal_acc = new float[3];
    private float[] mVal_mag = new float[3];
    private int mValue_cnt = 0;
    private int mNum_smooth = 50;
    private float[] mValue0 = new float[50];
    private float[] mValue1 = new float[50];
    private float[] mValue2 = new float[50];

    private static final String TAG = "---MainActivity";

    // original transform matrix for two images
    private static Matrix matrixL_origin, matrixR_origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // remove notification bar and full-screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set content view
        setContentView(R.layout.activity_panorama_cardboard);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mExtLeftImg= extras.getString("PANO_LEFT_PATH");
            mExtRightImg = extras.getString("PANO_RIGHT_PATH");

            if (mExtLeftImg.equals("") || mExtRightImg.equals("")) {
                mIsExtImageExist = false;
            } else {
                mIsExtImageExist = true;
            }
        }

        // TODO: initialize imageview
        initImageView();

        // initialize SensorManager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // initialize sensors
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // register listener function for sensors
        mSensorManager.registerListener(myListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(myListener, mMagnetic, SensorManager.SENSOR_DELAY_GAME);
        // calculate device orientation and update ImageView
        calculateOrientation();
    }

    private void initImageView() {

        // load 2 images
        String sdPath   = Environment.getExternalStorageDirectory().getAbsolutePath(); // SD card location
        File imgL   = new File( sdPath + File.separator + mStoragePath + File.separator + mLeftImageName );
        File imgR   = new File( sdPath + File.separator + mStoragePath + File.separator + mRightImageName );

        if (mIsUseOrgImage) {
            imgL    = new File( sdPath + File.separator + mStoragePath + File.separator + mLeftImageName );
            imgR   = new File( sdPath + File.separator + mStoragePath + File.separator + mRightImageName );

        } else {
            imgL    = new File( mExtLeftImg );
            imgR   = new File( mExtRightImg );

        }

        // save 2 images into bitmap
        Bitmap bmL  = BitmapFactory.decodeFile( imgL.getAbsolutePath() );
        Bitmap bmR  = BitmapFactory.decodeFile( imgR.getAbsolutePath() );

        // get 2 ImageView
        final ImageView imgViewL  = (ImageView) findViewById(R.id.iv_left);
        final ImageView imgViewR  = (ImageView) findViewById(R.id.iv_right);

        // get height and width of images
        float imgL_h = bmL.getHeight();     float imgL_w = bmL.getWidth();
        float imgR_h = bmR.getHeight();     float imgR_w = bmR.getWidth();

        // get height and width of ImageView
        Display d = getWindowManager().getDefaultDisplay();
        Point szScreen = new Point();       d.getSize(szScreen);
        float imgViewL_h = szScreen.y;
        float imgViewL_w = szScreen.x / 2;
        float imgViewR_h = szScreen.y;
        float imgViewR_w = szScreen.x / 2;

        // set current scale and translation of 2 images
        Matrix matrixL =  imgViewL.getImageMatrix();
        Matrix matrixR =  imgViewL.getImageMatrix();

        // TODO: set current scale
        float scaleL = 3.5f*Math.min( 1.0f*imgViewL_h/imgL_h, 1.0f*imgViewL_w/imgL_w );// TODO: change this value
        float scaleR = 3.5f*Math.min( 1.0f*imgViewR_h/imgR_h, 1.0f*imgViewR_w/imgR_w );// TODO: change this value
        matrixL.postScale(scaleL, scaleL);
        matrixR.postScale(scaleR, scaleR);
        // TODO: set current translate to make image in the center
        matrixL.postTranslate( 0, 300 ); // TODO: change this value
        matrixR.postTranslate( 0, 300 ); // TODO: change this value

        // save initial scale and translation matrix for future use
        matrixL_origin = new Matrix(matrixL);
        matrixR_origin = new Matrix(matrixR);

        // display images inside the ImageView
        imgViewL.setImageBitmap(bmL);
        imgViewR.setImageBitmap(bmR);

        // using matrix to control scale and translation of images
        imgViewL.setImageMatrix(matrixL);
        imgViewR.setImageMatrix(matrixR);
        imgViewL.setScaleType(ImageView.ScaleType.MATRIX);
        imgViewR.setScaleType(ImageView.ScaleType.MATRIX);

        // set imageview background color
        imgViewL.setBackgroundColor(Color.rgb(255,0,0));
        imgViewR.setBackgroundColor(Color.rgb(0,255,0));
    }

    public void onPause(){
        mSensorManager.unregisterListener(myListener);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(myListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(myListener, mMagnetic, SensorManager.SENSOR_DELAY_GAME);
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mVal_mag = sensorEvent.values;
                //mVal_mag[0] = mVal_mag[0] - 980f;
                //mVal_mag[1] = mVal_mag[1] - 1200f;
                //mVal_mag[2] = mVal_mag[2] - 1500f;
//0 -13 10 980 1234 1613
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mVal_acc = sensorEvent.values;
            calculateOrientation();
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
    private  void calculateOrientation() {
        // this function is to calculate orientation and update ImageView

        // calculate orientation from mVal_acc and mVal_mag
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, mVal_acc, mVal_mag);
        SensorManager.getOrientation(R, values);

        // transform angles from rad to degree
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        // TODO: smooth neighboring sensor values to avoid vibration
        mValue_cnt = (mValue_cnt+1) % mNum_smooth;
        mValue0[mValue_cnt] = values[0];
        mValue1[mValue_cnt] = values[1];
        mValue2[mValue_cnt] = values[2];
        values[0] = 0;
        values[1] = 0;
        values[2] = 0;
        for (int i = 0; i < mNum_smooth; i++) {
            values[0] = values[0] + mValue0[i];
            values[1] = values[1] + mValue1[i];
            values[2] = values[2] + mValue2[i];
        }
        values[0] = values[0]/mNum_smooth;
        values[1] = values[1]/mNum_smooth;
        values[2] = values[2]/mNum_smooth;

        // update image in ImageView
        // get ImageView
        final ImageView imgViewL  = (ImageView) findViewById(com.demo.zhouc.cardboarddemo.R.id.iv_left);
        final ImageView imgViewR  = (ImageView) findViewById(com.demo.zhouc.cardboarddemo.R.id.iv_right);

        // set scale and translation of 2 images
        Matrix matrixL = new Matrix(matrixL_origin);
        Matrix matrixR = new Matrix(matrixR_origin);

        // TODO: set up correspondence between orientation and translation
        float factor_x = 10; // TODO: change this value
        float factor_y = 10; // TODO: change this value
        //matrixL.postTranslate(-values[0] * factor_x, -(values[2] + 90) * factor_y);
        //matrixR.postTranslate(-values[0]*factor_x, -(values[2]+90)*factor_y );
        matrixL.postTranslate(-values[0] * factor_x, -(values[2]+ 90) * factor_y);
        matrixR.postTranslate(-values[0] * factor_x, -(values[2] + 90) * factor_y);
        //Log.d(LOG_TAG, "magnetic: " + values[0]);
        Log.d(LOG_TAG, "m_magnetic: " + mVal_mag[0] + ' ' + mVal_mag[1] + ' ' + mVal_mag[2]);

        // rotate image with respect to center of each ImageView
        matrixL.postRotate(values[1],imgViewL.getMeasuredWidth()/2, imgViewL.getMeasuredHeight()/2);
        matrixR.postRotate(values[1],imgViewL.getMeasuredWidth()/2, imgViewL.getMeasuredHeight()/2);

        // using matrix to control scale and translation of images
        imgViewL.setImageMatrix(matrixL);
        imgViewR.setImageMatrix(matrixR);
        imgViewL.setScaleType(ImageView.ScaleType.MATRIX);
        imgViewR.setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override
    public void onCardboardTrigger (){
        Log.d(LOG_TAG, "trigger");

        if (mIsExtImageExist){
            setContentView(R.layout.activity_image_cardboard);
            if (mIsUseOrgImage) {
                mIsUseOrgImage = false;
            } else {
                mIsUseOrgImage = true;
            }
            initImageView();
        }
//        if (mIsZoom == false){
//            mScaleLFactor = mScaleLAftZoom;
//            mScaleRFactor = mScaleRAftZoom;
//            mIsZoom = true;
//            setContentView(R.layout.activity_image_cardboard);
//            initImageView();
//        } else{
//            mScaleLFactor = mScaleLB4Zoom;
//            mScaleRFactor = mScaleRB4Zoom;
//            mIsZoom = false;
//            setContentView(R.layout.activity_image_cardboard);
//            initImageView();
//        }
    }

}


