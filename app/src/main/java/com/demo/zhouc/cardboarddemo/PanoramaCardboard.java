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

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;


    private static final float mScaleLB4Zoom = 13.5f;
    private static final float mScaleRB4Zoom = 13.5f;
    private static final float mScaleLAftZoom = 18.5f;
    private static final float mScaleRAftZoom = 18.5f;
    private static float mScaleLFactor;
    private static float mScaleRFactor;
    private boolean mIsZoom;


    private final String LOG_TAG = PanoramaCardboard.class.getSimpleName();
    // directory for input images
    private static final String mStoragePath = "CardBoardDemo";
    private static final String mLeftImageName = "panLeft.jpg";
    private static final String mRightImageName = "panRight.jpg";

    private String mExtLeftImg = "";
    private String mExtRightImg = "";
    private boolean mIsExtImageExist = false;
    private boolean mIsUseOrgImage = true;

    // sensor manager
    private SensorManager mSensorManager;
    // sensor accelerometer & magnetic
    private Sensor mAccelerometer;
    private Sensor mMagnetic;
    private Sensor mGyroscope;
    private Sensor mSensorVector;

    // current sensor values
    private float[] mVal_acc = new float[3];
    private float[] mVal_mag = new float[3];
    private int mValue_cnt = 0;
    private int mNum_smooth = 50;
    private float[] mValue0 = new float[50];
    private float[] mValue1 = new float[50];
    private float[] mValue2 = new float[50];



    private int mValue2_cnt = 0;
    private int mNum_smooth2 = 50;

    private float[] m2Value0 = new float[50];
    private float[] m2Value1 = new float[50];
    private float[] m2Value2 = new float[50];

    private boolean mIsCalibrated = false;
    private float[] mCalibrateMatrix = new float[9];


    private float[] mNewRotationMatrix = new float[9];
    private float[] orientationVals = new float[9];

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
                mIsUseOrgImage = true;
            } else {
                mIsExtImageExist = true;
                mIsUseOrgImage = false;
            }
        }

        mScaleLFactor = mScaleLB4Zoom;
        mScaleRFactor = mScaleRB4Zoom;
        mIsZoom = false;

        // TODO: initialize imageview
        initImageView();

        // initialize SensorManager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // initialize sensors
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        // register listener function for sensors
        mSensorManager.registerListener(myListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(myListener, mMagnetic, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(myListener, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(myListener, mSensorVector, SensorManager.SENSOR_DELAY_GAME);
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
        float scaleL = mScaleLFactor*Math.min( 1.0f*imgViewL_h/imgL_h, 1.0f*imgViewL_w/imgL_w );// TODO: change this value
        float scaleR = mScaleRFactor*Math.min( 1.0f*imgViewR_h/imgR_h, 1.0f*imgViewR_w/imgR_w );// TODO: change this value
        matrixL.postScale(scaleL, scaleL);
        matrixR.postScale(scaleR, scaleR);
        // TODO: set current translate to make image in the center
        matrixL.postTranslate( -200, -200 ); // TODO: change this value
        matrixR.postTranslate( -200, -200 ); // TODO: change this value

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
        mSensorManager.registerListener(myListener, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(myListener, mSensorVector, SensorManager.SENSOR_DELAY_GAME);


    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE)
             {

                if (timestamp != 0) {
                    final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                    // Axis of the rotation sample, not normalized yet.
                    float axisX = sensorEvent.values[0];
                    float axisY = sensorEvent.values[1];
                    float axisZ = sensorEvent.values[2];

                    // Calculate the angular speed of the sample
                    float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                    // Normalize the rotation vector if it's big enough to get the axis

                    if (omegaMagnitude > 0.000001f) {
                        axisX /= omegaMagnitude;
                        axisY /= omegaMagnitude;
                        axisZ /= omegaMagnitude;
                    }


                    // Integrate around this axis with the angular speed by the timestep
                    // in order to get a delta rotation from this sample over the timestep
                    // We will convert this axis-angle representation of the delta rotation
                    // into a quaternion before turning it into the rotation matrix.
                    float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                    float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                    float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
                    deltaRotationVector[0] = sinThetaOverTwo * axisX;
                    deltaRotationVector[1] = sinThetaOverTwo * axisY;
                    deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                    deltaRotationVector[3] = cosThetaOverTwo;
                }
                timestamp = sensorEvent.timestamp;

                float[] deltaRotationMatrix = new float[9];
                 SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            }


            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mVal_mag = sensorEvent.values;
                //mVal_mag[0] = mVal_mag[0] - 980f;
                //mVal_mag[1] = mVal_mag[1] - 1200f;
                //mVal_mag[2] = mVal_mag[2] - 1600f;
//0 -13 10
// 980 1234 1613
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mVal_acc = sensorEvent.values;

            // It is good practice to check that we received the proper sensor event
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
            {
                // Convert the rotation-vector to a 4x4 matrix.
                SensorManager.getRotationMatrixFromVector(mNewRotationMatrix,
                        sensorEvent.values);
                SensorManager
                        .remapCoordinateSystem(mNewRotationMatrix,
                                SensorManager.AXIS_X, SensorManager.AXIS_Z,
                                mNewRotationMatrix);
                SensorManager.getOrientation(mNewRotationMatrix, orientationVals);

                // Optionally convert the result from radians to degrees
                orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
                orientationVals[1] = (float) Math.toDegrees(orientationVals[1]);
                orientationVals[2] = (float) Math.toDegrees(orientationVals[2]);



                //tv.setText(" Yaw: " + orientationVals[0] + "\n Pitch: "
                //        + orientationVals[1] + "\n Roll (not used): "
                //        + orientationVals[2]);

            }




            calculateOrientation();
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
    private  void calculateOrientation() {
        // this function is to calculate orientation and update ImageView

        // calculate orientation from mVal_acc and mVal_mag
        float[] values = new float[3];
        float[] values2 = new float[3];
        float[] R = new float[9];
        //mVal_mag[0] = 1f;
        //mVal_mag[1] = 1f;
        //mVal_mag[2] = 1f;



        SensorManager.getRotationMatrix(R, null, mVal_acc, mVal_mag);

        SensorManager.getOrientation(R, values);


        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);

        //float[] Realsie = new float[9];
        //SensorManager.getOrientation(deltaRotationMatrix, values2);

//        if (mIsCalibrated==false){
//            mCalibrateMatrix = R;
//            mIsCalibrated = true;
//        }
//
//
//
//        Realsie = matrixMultiplication(mCalibrateMatrix, deltaRotationMatrix);
//        SensorManager.getOrientation(Realsie, values2);

        SensorManager.getOrientation(deltaRotationMatrix, values2);

        // transform angles from rad to degree
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        values2[0] = (float) Math.toDegrees(values2[0]);
        values2[1] = (float) Math.toDegrees(values2[1]);
        values2[2] = (float) Math.toDegrees(values2[2]);

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



        mValue2_cnt = (mValue2_cnt+1) % mNum_smooth2;
        m2Value0[mValue2_cnt] = values2[0];
        m2Value1[mValue2_cnt] = values2[1];
        m2Value2[mValue2_cnt] = values2[2];
        values2[0] = 0;
        values2[1] = 0;
        values2[2] = 0;
        for (int i = 0; i < mNum_smooth2; i++) {
            values2[0] = values2[0] + m2Value0[i];
            values2[1] = values2[1] + m2Value1[i];
            values2[2] = values2[2] + m2Value2[i];
        }
        values2[0] = values2[0]/mNum_smooth2;
        values2[1] = values2[1]/mNum_smooth2;
        values2[2] = values2[2]/mNum_smooth2;

        // update image in ImageView
        // get ImageView
        final ImageView imgViewL  = (ImageView) findViewById(com.demo.zhouc.cardboarddemo.R.id.iv_left);
        final ImageView imgViewR  = (ImageView) findViewById(com.demo.zhouc.cardboarddemo.R.id.iv_right);

        // set scale and translation of 2 images
        Matrix matrixL = new Matrix(matrixL_origin);
        Matrix matrixR = new Matrix(matrixR_origin);

        // TODO: set up correspondence between orientation and translation
        float factor_x = 20; // TODO: change this value
        float factor_y = 10; // TODO: change this value
        //matrixL.postTranslate(-values[0] * factor_x, -(values[2] + 90) * factor_y);
        //matrixR.postTranslate(-values[0]*factor_x, -(values[2]+90)*factor_y );
        matrixL.postTranslate(-orientationVals[0]  * factor_x, -(values[2]+ 90) * factor_y);
        matrixR.postTranslate(-orientationVals[0] * factor_x, -(values[2] + 90) * factor_y);
        //Log.d(LOG_TAG, "magnetic: " + values[0]);
        //Log.d(LOG_TAG, "m_magnetic: " + mVal_mag[0] + ' ' + mVal_mag[1] + ' ' + mVal_mag[2]);
        Log.d(LOG_TAG, "values0: " + values[0] + ' ' + values[1] + ' ' + values[2]);
        Log.d(LOG_TAG, "values2: " + values2[0] + ' ' + values2[1] + ' ' + values2[2]);
        Log.d(LOG_TAG, "fuck fuck" + orientationVals[0] + ' ' + orientationVals[1] + ' ' + orientationVals[2]);


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

//        if (mIsExtImageExist){
//            setContentView(R.layout.activity_image_cardboard);
//            if (mIsUseOrgImage) {
//                mIsUseOrgImage = false;
//            } else {
//                mIsUseOrgImage = true;
//            }
//            initImageView();
//        }
        if (mIsZoom == false){
            mScaleLFactor = mScaleLAftZoom;
            mScaleRFactor = mScaleRAftZoom;
            mIsZoom = true;
            setContentView(R.layout.activity_panorama_cardboard);
            initImageView();
        } else{
            mScaleLFactor = mScaleLB4Zoom;
            mScaleRFactor = mScaleRB4Zoom;
            mIsZoom = false;
            setContentView(R.layout.activity_panorama_cardboard);
            initImageView();
        }
    }

    private float[] matrixMultiplication(float[] a, float[] b)
    {
        float[] result = new float[9];

        result[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
        result[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
        result[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];

        result[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
        result[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
        result[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];

        result[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
        result[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
        result[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];

        return result;
    }

}


