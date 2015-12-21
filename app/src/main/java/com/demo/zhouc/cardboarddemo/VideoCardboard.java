package com.demo.zhouc.cardboarddemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.vrtoolkit.cardboard.CardboardActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhouc on 14-11-4.
 */
public class VideoCardboard extends CardboardActivity{

    private static final String mStoragePath = "CardBoardDemo";
    //private static final String mLeftVideoName = "left.avi";
    //private static final String mRightVideoName = "right.avi";
    private static final String mLeftVideoName = "left.mp4";
    private static final String mRightVideoName = "right.mp4";

    private String mExtLeftVideo = "";
    private String mExtRightVideo = "";
    private boolean mIsExtVideoExist;

    // get videoview
    private int mLStopPosition;
    private int mRStopPosition;
    private boolean mIsStopped;
    private final String LOG_TAG = VideoCardboard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_cardboard);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mExtLeftVideo = extras.getString("VIDEO_LEFT_PATH");
            mExtRightVideo = extras.getString("VIDEO_RIGHT_PATH");

            if (mExtLeftVideo.equals("") || mExtRightVideo.equals("")){
                mIsExtVideoExist = false;
            } else {
                mIsExtVideoExist = true;
            }
        }

        // initialize videoview
        mIsStopped = false;
        initVideoView();
    }

    private void initVideoView() {
        VideoView vidViewL      = (VideoView) findViewById(R.id.vv_left);
        VideoView vidViewR    = (VideoView) findViewById(R.id.vv_right);

        // load 2 images
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File vidL = new File(sdPath + File.separator + mStoragePath + File.separator + mLeftVideoName);
        File vidR = new File(sdPath + File.separator + mStoragePath + File.separator + mRightVideoName);

        if (mIsExtVideoExist){
            vidL = new File(mExtLeftVideo);
            vidR = new File(mExtRightVideo);
        }


        vidViewL.setVideoPath( vidL.getAbsolutePath() );
        vidViewR.setVideoPath( vidR.getAbsolutePath() );

        // set the scale and translation of the video
        float scaleL = 1.0f;
        float scaleR = 1.0f;
        float transL_x = 10, transL_y = 10;
        float transR_x = 10, transR_y = 10;

        vidViewL.setScaleX(scaleL);     vidViewL.setScaleY(scaleL);
        vidViewR.setScaleX(scaleR);     vidViewR.setScaleY(scaleR);
        vidViewL.setTranslationX(transL_x);
        vidViewL.setTranslationY(transL_y);
        vidViewR.setTranslationX(transR_x);
        vidViewR.setTranslationY(transR_y);

        // start to play the video
        vidViewL.start();
        vidViewR.start();

    }

//    @Override
//    public void onPause() {
//        Log.d(LOG_TAG, "onPause called");
//        super.onPause();
//        mStopPosition = vidViewL.getCurrentPosition(); //stopPosition is an int
//        vidViewL.pause();
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(LOG_TAG, "onResume called");
//        vidViewL.seekTo(mStopPosition);
//        vidViewL.start(); //Or use resume() if it doesn't work. I'm not sure
//    }

    @Override
    public void onCardboardTrigger (){
        Log.d(LOG_TAG, "trigger");

        if (mIsStopped){
            mIsStopped = false;
            VideoView vidViewL    = (VideoView) findViewById(R.id.vv_left);
            VideoView vidViewR    = (VideoView) findViewById(R.id.vv_right);
            vidViewL.seekTo(mLStopPosition);
            vidViewR.seekTo(mRStopPosition);
            Log.d(LOG_TAG, "Video is resumed. StopPosition is" + mLStopPosition);
            //vidViewL.start(); //Or use resume() if it doesn't work. I'm not sure
            vidViewL.start();
            vidViewR.start();
        } else {
            mIsStopped = true;
            VideoView vidViewL    = (VideoView) findViewById(R.id.vv_left);
            VideoView vidViewR    = (VideoView) findViewById(R.id.vv_right);
            mLStopPosition = vidViewL.getCurrentPosition(); //stopPosition is an int
            mRStopPosition = vidViewR.getCurrentPosition(); //stopPosition is an int
            Log.d(LOG_TAG, "Video is paused. StopPosition is" + mLStopPosition);
            vidViewL.pause();
            vidViewR.pause();
        }
    }


}
