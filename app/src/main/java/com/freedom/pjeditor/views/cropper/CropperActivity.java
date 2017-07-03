package com.freedom.pjeditor.views.cropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.fenchtose.nocropper.CropperView;
import com.freedom.pjeditor.R;
import com.freedom.pjeditor.model.SelectedImages;
import com.freedom.pjeditor.realm.RealmController;
import com.freedom.pjeditor.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.Realm;

/**
 * Created by Mayur on 6/28/2017.
 */

public class CropperActivity extends AppCompatActivity {

    Context context;
    CropperView cropperView;
    ImageView snap;

    private Realm realm;
    Bitmap bitmap = null;
    String path = "";
    private boolean isSnappedToCenter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropper);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Image");
        }

        context = CropperActivity.this;
        realm = RealmController.getInstance().getRealm();

        cropperView = (CropperView) findViewById(R.id.cropper_view);
        snap = (ImageView) findViewById(R.id.snap_button);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snapImage();
            }
        });

        path = getIntent().getStringExtra("path");
        getBitmap(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                storeCoOrdinates();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBitmap(final String path) {
        Observable.just(path).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Uri uri = Uri.fromFile(new File(path));

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    cropperView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void snapImage() {
        if (isSnappedToCenter) {
            cropperView.cropToCenter();
        } else {
            cropperView.fitToCenter();
        }
        isSnappedToCenter = !isSnappedToCenter;
    }

    private void storeCoOrdinates() {

        ImageView img = (ImageView) cropperView.getChildAt(0);
        int[] x = Util.getBitmapPositionInsideImageView(img);
        String coOrds = Arrays.toString(x);

        realm.beginTransaction();

        try {
            SelectedImages selectedImage = RealmController.getInstance().getSelectedImageByPath(path);
            selectedImage.setCroppedImageCoOrdinates(coOrds);
            realm.copyToRealmOrUpdate(selectedImage);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();

        // Rect r = img.getDrawable().getBounds();
    }
}