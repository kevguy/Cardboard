package com.demo.zhouc.cardboarddemo;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;

import com.google.vrtoolkit.cardboard.CardboardActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;



/**
 * Created by zhouc & xtao on 14-11-4.
 */
public class ImageCardboard extends CardboardActivity {

    private static final String mStoragePath = "CardBoardDemo";
    private static final String mLeftImageName = "left.bmp";
    private static final String mRightImageName = "right.bmp";

    private String mExtLeftImg = "";
    private String mExtRightImg = "";
    private boolean mIsExtImageExist = false;
    private boolean mIsUseOrgImage = true;

    private static final float mScaleLB4Zoom = 0.8f;
    private static final float mScaleRB4Zoom = 0.8f;
    private static final float mScaleLAftZoom = 1.6f;
    private static final float mScaleRAftZoom = 1.6f;
    private static float mScaleLFactor;
    private static float mScaleRFactor;
    private boolean mIsZoom;

    private final String LOG_TAG = ImageCardboard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set content view
        setContentView(R.layout.activity_image_cardboard);
        // TODO: initialize imageview
        mScaleLFactor = mScaleLB4Zoom;
        mScaleRFactor = mScaleRB4Zoom;
        mIsZoom = false;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mExtLeftImg= extras.getString("IMG_LEFT_PATH");
            mExtRightImg = extras.getString("IMG_RIGHT_PATH");

            if (mExtLeftImg.equals("") || mExtRightImg.equals("")) {
                mIsExtImageExist = false;
            } else {
                mIsExtImageExist = true;
            }

        }

        initImageView();

    }

    private void initImageView() {

        // load 2 images
        String sdPath   = Environment.getExternalStorageDirectory().getAbsolutePath(); // SD card location
        sdPath = "/storage/emulated/0";
        Log.d(LOG_TAG, "path fuck: " + sdPath);
        Log.d(LOG_TAG, sdPath + File.separator + mStoragePath + File.separator + mLeftImageName);

        File imgL    = new File( sdPath + File.separator + mStoragePath + File.separator + mLeftImageName );
        File imgR   = new File( sdPath + File.separator + mStoragePath + File.separator + mRightImageName );

        if (mIsUseOrgImage) {
            imgL    = new File( sdPath + File.separator + mStoragePath + File.separator + mLeftImageName );
            imgR   = new File( sdPath + File.separator + mStoragePath + File.separator + mRightImageName );

        } else {
            imgL    = new File( mExtLeftImg );
            imgR   = new File( mExtRightImg );

        }


        //File imgL    = new File( sdPath + File.separator + mLeftImageName );
        //File imgR   = new File( sdPath + File.separator + mRightImageName );

        String FILE_NAME = "fuck.txt";
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write("fuck fuck".getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        String FILE_NAME = "file.txt";
        if (isExternalStorageAvailable() && isExternalStorageReadOnly()) {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(baseDir, FILE_NAME);

            String line = "";
            StringBuilder text = new StringBuilder();

            try {
                FileReader fReader = new FileReader(file);
                BufferedReader bReader = new BufferedReader(fReader);

                while( (line = bReader.readLine()) != null  ){
                    text.append(line+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */


        //String FILE_NAME = "file.txt";
        if (isExternalStorageAvailable() && isExternalStorageReadOnly()) {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(baseDir, "hello.txt");
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write("fuck fuck");
                writer.close();
                Log.d(LOG_TAG, "out of the way bitch");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            if (!isExternalStorageAvailable()){
                Log.d(LOG_TAG, "ext storage is not vailabe");
            }
            if (!isExternalStorageReadOnly()){
                Log.d(LOG_TAG, "EXT is read only");
            }
        }




        // save 2 images into bitmap
        Bitmap bmL  = BitmapFactory.decodeFile( imgL.getAbsolutePath() );
        Bitmap bmR  = BitmapFactory.decodeFile( imgR.getAbsolutePath() );

        //LinearLayout ll = (LinearLayout) findViewById(R.layout.activity_image_cardboard);


        // get imageview
        final ImageView imgViewL   = (ImageView) findViewById(R.id.iv_left);
        final ImageView imgViewR  = (ImageView) findViewById(R.id.iv_right);



        // get height and width of images
        float imgL_h = bmL.getHeight();
        float imgL_w = bmL.getWidth();
        float imgR_h = bmR.getHeight();
        float imgR_w = bmR.getWidth();

        // get height and width of ImageView
        Display d = getWindowManager().getDefaultDisplay();
        Point szScreen = new Point();       d.getSize(szScreen);
        float imgViewL_h = szScreen.y;
        float imgViewL_w = szScreen.x / 2;

        float imgViewR_h = szScreen.y;
        float imgViewR_w = szScreen.x / 2;

        /*
        // Sample Image
        // TODO: set scale and translation of 2 images
        Matrix matrixL =  imgViewL.getImageMatrix();
        Matrix matrixR =  imgViewL.getImageMatrix();
        // TODO: set scale of curent image
        float scaleL = 0.8f*Math.min( 1.0f*imgViewL_h/imgL_h, 1.0f*imgViewL_w/imgL_w ); // TODO: change this value
        float scaleR = 0.8f*Math.min( 1.0f*imgViewR_h/imgR_h, 1.0f*imgViewR_w/imgR_w ); // TODO: change this value
        matrixL.postScale( scaleL, scaleL );
        matrixR.postScale( scaleR, scaleR );
        // TODO: set translation of curent image
        matrixL.postTranslate( 90, 150 );// TODO: change this value
        matrixR.postTranslate( 90, 150 );// TODO: change this value
        */

        // Test 2 Image
        // TODO: set scale and translation of 2 images
        Matrix matrixL =  imgViewL.getImageMatrix();
        Matrix matrixR =  imgViewL.getImageMatrix();
        // TODO: set scale of curent image
        float scaleL = mScaleLFactor*Math.min( 1.0f*imgViewL_h/imgL_h, 1.0f*imgViewL_w/imgL_w ); // TODO: change this value
        float scaleR = mScaleRFactor*Math.min( 1.0f*imgViewR_h/imgR_h, 1.0f*imgViewR_w/imgR_w ); // TODO: change this value
        matrixL.postScale(scaleL, scaleL);
        matrixR.postScale(scaleR, scaleR);
        // TODO: set translation of curent image
        matrixL.postTranslate(90, 250);// TODO: change this value
        matrixR.postTranslate(90, 250);// TODO: change this value

        // display images inside the ImageView
        imgViewL.setImageBitmap(bmL);
        imgViewR.setImageBitmap(bmR);

        // using matrix to control scale and translation of images
        imgViewL.setImageMatrix(matrixL);
        imgViewR.setImageMatrix(matrixR);
        imgViewL.setScaleType(ImageView.ScaleType.MATRIX);
        imgViewR.setScaleType(ImageView.ScaleType.MATRIX);

        // set imageview background color
        imgViewL.setBackgroundColor(Color.rgb(255, 0, 0));
        imgViewR.setBackgroundColor(Color.rgb(0, 255, 0));


        //ll.invalidate();
        //ll.requestLayout();
        imgViewL.invalidate();
        imgViewR.invalidate();


    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
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
