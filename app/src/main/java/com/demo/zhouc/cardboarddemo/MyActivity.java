package com.demo.zhouc.cardboarddemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import static com.demo.zhouc.cardboarddemo.R.id.btn_image_cardboard;
import static com.demo.zhouc.cardboarddemo.R.id.btn_video_cardboard;
import static com.demo.zhouc.cardboarddemo.R.id.btn_pano_cardboard;

@SuppressLint("NewApi")


public class MyActivity extends Activity {
    private final String LOG_TAG = ImageCardboard.class.getSimpleName();
    private ImageView mLeftView;
    private ImageView mRightView;
    private int PICK_IMAGE_REQUEST = 1;
    private int GALLERY_INTENT_CALLED = 2;
    private int GALLERY_KITKAT_INTENT_CALLED = 3;
    private int REQUEST_TAKE_GALLERY_VIDEO = 4;
    private int VIDEO_GALLERY_KITKAT_INTENT_CALLED = 5;

    private String imgLeftPath = "";
    private String imgRightPath = "";
    private String videoLeftPath = "";
    private String videoRightPath = "";
    private String panoLeftPath = "";
    private String panoRightPath = "";

    private boolean isVideo = false;

    private static final int IMG_LEFT = 10;
    private static final int IMG_RIGHT = 11;
    private static final int VIDEO_LEFT = 12;
    private static final int VIDEO_RIGHT = 13;
    private static final int PANO_LEFT = 14;
    private static final int PANO_RIGHT = 15;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        Button imgLeftBtn = (Button) findViewById(R.id.btn_img_left);
        imgLeftBtn.setEnabled(false);
        Button imgRightBtn = (Button) findViewById(R.id.btn_img_right);
        imgRightBtn.setEnabled(false);
        Button videoLeftBtn = (Button) findViewById(R.id.btn_video_left);
        videoLeftBtn.setEnabled(false);
        Button videoRightBtn = (Button) findViewById(R.id.btn_video_right);
        videoRightBtn.setEnabled(false);
        Button panoLeftBtn = (Button) findViewById(R.id.btn_pano_left);
        panoLeftBtn.setEnabled(false);
        Button panoRightBtn = (Button) findViewById(R.id.btn_pano_right);
        panoRightBtn.setEnabled(false);

        final Button fav = (Button) findViewById(R.id.btn_sel);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

        imgLeftBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mode = IMG_LEFT;
                isVideo = false;
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

        imgRightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mode = IMG_RIGHT;
                isVideo = false;
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

        videoLeftBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mode = VIDEO_LEFT;
                isVideo = true;
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("video/*");
                    startActivityForResult(intent, VIDEO_GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

        videoRightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mode = VIDEO_RIGHT;
                isVideo = true;
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"),GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("video/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

        panoLeftBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mode = PANO_LEFT;
                isVideo = false;
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

        panoRightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mode = PANO_RIGHT;
                isVideo = false;
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_INTENT_CALLED);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                }
            }
        });

    }

    private void initUI(){
        Button btnImageCardboard = (Button) findViewById(btn_image_cardboard);
        btnImageCardboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ImageCardboard.class);
                i.putExtra("IMG_LEFT_PATH", imgLeftPath);
                i.putExtra("IMG_RIGHT_PATH", imgRightPath);
                startActivity(i);
            }
        });

        Button btnVideoCardboard = (Button) findViewById(btn_video_cardboard);
        btnVideoCardboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VideoCardboard.class);
                i.putExtra("VIDEO_LEFT_PATH", videoLeftPath);
                i.putExtra("VIDEO_RIGHT_PATH", videoRightPath);
                startActivity(i);
            }
        });

        Button btnPanoramaCardboard = (Button) findViewById(btn_pano_cardboard);
        btnPanoramaCardboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PanoramaCardboard.class);
                i.putExtra("PANO_LEFT_PATH", panoLeftPath);
                i.putExtra("PANO_RIGHT_PATH", panoRightPath);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (null == data) return;
        Uri originalUri = null;
        if (requestCode == GALLERY_INTENT_CALLED) {
            originalUri = data.getData();
        } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
            originalUri = data.getData();
            int takeFlags = data.getFlags();
            takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
        }

        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            originalUri = data.getData();
        } else if (requestCode == VIDEO_GALLERY_KITKAT_INTENT_CALLED) {
            originalUri = data.getData();
            int takeFlags = data.getFlags();
            takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
        }

        Log.d(LOG_TAG, "file path is: " + originalUri);

        //loadSomeStreamAsynkTask(originalUri);
        String wholeID = DocumentsContract.getDocumentId(originalUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] columnImg = {MediaStore.Images.Media.DATA};
        String[] columnVideo = {MediaStore.Video.Media.DATA};


        // where id is equal to
        String selImg = MediaStore.Images.Media._ID + "=?";
        String selVideo = MediaStore.Video.Media._ID + "=?";


        Cursor cursorImg = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columnImg, selImg, new String[]{ id }, null);
        Cursor cursorVideo = getContentResolver().
                query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        columnVideo, selVideo, new String[]{ id }, null);

        String filePath = "";

        int columnImgIndex = cursorImg.getColumnIndex(columnImg[0]);
        int columnVideoIndex = cursorVideo.getColumnIndex(columnVideo[0]);


        if (cursorImg.moveToFirst()) {
            filePath = cursorImg.getString(columnImgIndex);
            Log.d(LOG_TAG, filePath);
            TextView text;
            String[] pathdir;
            switch (mode){
                case IMG_LEFT:
                    text = (TextView) findViewById(R.id.img_left_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    imgLeftPath = filePath;
                    break;
                case IMG_RIGHT:
                    text = (TextView) findViewById(R.id.img_right_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    imgRightPath = filePath;
                    break;
                case VIDEO_LEFT:
                    text = (TextView) findViewById(R.id.video_left_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    videoLeftPath = filePath;
                    break;
                case VIDEO_RIGHT:
                    text = (TextView) findViewById(R.id.video_right_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    videoRightPath = filePath;
                    break;
                case PANO_LEFT:
                    text = (TextView) findViewById(R.id.pano_left_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    panoLeftPath = filePath;
                    break;
                case PANO_RIGHT:
                    text = (TextView) findViewById(R.id.pano_right_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    panoRightPath = filePath;
                    break;

            }



        }
        if (cursorVideo.moveToFirst()) {
            Log.d(LOG_TAG, "fuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuckfuck");
            filePath = cursorVideo.getString(columnVideoIndex);
            Log.d(LOG_TAG, filePath);
            TextView text;
            String[] pathdir;
            switch (mode){
                case IMG_LEFT:
                    text = (TextView) findViewById(R.id.img_left_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    imgLeftPath = filePath;
                    break;
                case IMG_RIGHT:
                    text = (TextView) findViewById(R.id.img_right_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    imgRightPath = filePath;
                    break;
                case VIDEO_LEFT:
                    text = (TextView) findViewById(R.id.video_left_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    videoLeftPath = filePath;
                    break;
                case VIDEO_RIGHT:
                    text = (TextView) findViewById(R.id.video_right_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    videoRightPath = filePath;
                    break;
                case PANO_LEFT:
                    text = (TextView) findViewById(R.id.pano_left_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    panoLeftPath = filePath;
                    break;
                case PANO_RIGHT:
                    text = (TextView) findViewById(R.id.pano_right_path);
                    pathdir = filePath.split("/");
                    text.setText(pathdir[pathdir.length-1]);
                    panoRightPath = filePath;
                    break;

            }



        }
        cursorImg.close();
        cursorVideo.close();

    }

    public void onImageButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_image_yes:
                if (checked) {
                    Button imgLeftBtn = (Button) findViewById(R.id.btn_img_left);
                    imgLeftBtn.setEnabled(true);
                    Button imgRightBtn = (Button) findViewById(R.id.btn_img_right);
                    imgRightBtn.setEnabled(true);
                    break;
                }

            case R.id.radio_image_no:
                if (checked) {
                    Button imgLeftBtn = (Button) findViewById(R.id.btn_img_left);
                    imgLeftBtn.setEnabled(false);
                    Button imgRightBtn = (Button) findViewById(R.id.btn_img_right);
                    imgRightBtn.setEnabled(false);

                    imgLeftPath = "";
                    imgRightPath = "";
                    TextView left = (TextView) findViewById(R.id.img_left_path);
                    left.setText("none");
                    TextView right = (TextView) findViewById(R.id.img_right_path);
                    right.setText("none");
                    break;
                }
        }
    }
    public void onVideoButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_video_yes:
                if (checked){
                    Button videoLeftBtn = (Button) findViewById(R.id.btn_video_left);
                    videoLeftBtn.setEnabled(true);
                    Button videoRightBtn = (Button) findViewById(R.id.btn_video_right);
                    videoRightBtn.setEnabled(true);
                    break;
                }

            case R.id.radio_video_no:
                if (checked){
                    Button videoLeftBtn = (Button) findViewById(R.id.btn_video_left);
                    videoLeftBtn.setEnabled(false);
                    Button videoRightBtn = (Button) findViewById(R.id.btn_video_right);
                    videoRightBtn.setEnabled(false);

                    videoLeftPath = "";
                    videoRightPath = "";
                    TextView left = (TextView) findViewById(R.id.video_left_path);
                    left.setText("none");
                    TextView right = (TextView) findViewById(R.id.video_right_path);
                    right.setText("none");
                    break;
                }
        }
    }
    public void onPanoButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pano_yes:
                if (checked) {
                    Button panoLeftBtn = (Button) findViewById(R.id.btn_pano_left);
                    panoLeftBtn.setEnabled(true);
                    Button panoRightBtn = (Button) findViewById(R.id.btn_pano_right);
                    panoRightBtn.setEnabled(true);
                    break;
                }
            case R.id.radio_pano_no:
                if (checked){
                    Button panoLeftBtn = (Button) findViewById(R.id.btn_pano_left);
                    panoLeftBtn.setEnabled(false);
                    Button panoRightBtn = (Button) findViewById(R.id.btn_pano_right);
                    panoRightBtn.setEnabled(false);

                    panoLeftPath = "";
                    panoRightPath = "";
                    TextView left = (TextView) findViewById(R.id.pano_left_path);
                    left.setText("none");
                    TextView right = (TextView) findViewById(R.id.pano_right_path);
                    right.setText("none");
                    break;
                }
        }
    }

}
