package com.freedom.pjeditor.views.chooser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.freedom.pjeditor.R;
import com.freedom.pjeditor.views.display.ListImages;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChooserActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> photoPaths = new ArrayList<>();

    private static final int REQUEST_READ_PERMISSION = 21;

    Observer<ArrayList<String>> observerPhotoPaths = null;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser_activity);

        context = ChooserActivity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
            }
        } else {
            showGallery();
        }

        observerPhotoPaths = new Observer<ArrayList<String>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull ArrayList<String> strings) {

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Intent display = new Intent(context, ListImages.class);
                display.putStringArrayListExtra("photoPaths", photoPaths);
                startActivity(display);
                finish();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            photoPaths = new ArrayList<>();
            photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

            Observable.just(photoPaths)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observerPhotoPaths);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showGallery();
                } else {
                    Toast.makeText(context, "Gallery Permission is required to access App.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void showGallery() {
        FilePickerBuilder.getInstance()
                .setSelectedFiles(photoPaths)
                .setActivityTheme(R.style.AppTheme)
                .enableCameraSupport(false)
                .setMaxCount(50)
                .pickPhoto(this);
    }
}