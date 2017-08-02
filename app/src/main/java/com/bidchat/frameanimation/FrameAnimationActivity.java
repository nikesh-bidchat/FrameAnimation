package com.bidchat.frameanimation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class FrameAnimationActivity extends AppCompatActivity {
    final String TAG = FrameAnimationActivity.class.getCanonicalName();

    String animation_folder = "animationimages";
    String file_separator = "/";

    AnimationDrawable Anim;

    final int TOTAL_TIME = 3000;

    ImageView imgAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAnimation = (ImageView) findViewById(R.id.img_animation);

        try {
            if(isStoragePermissionGranted()){
                startFrameAnimation();
            }
        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
    }

    public void startFrameAnimation(){
        String path = Environment.getExternalStorageDirectory().toString() + file_separator + animation_folder;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        Anim = new AnimationDrawable();
        Anim.setOneShot(false);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());

            BitmapDrawable bitmap = new BitmapDrawable(getResources(), path + file_separator + files[i].getName());
            Anim.addFrame(bitmap, TOTAL_TIME / files.length);
        }

        imgAnimation.setImageDrawable(Anim);
        Anim.start();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
