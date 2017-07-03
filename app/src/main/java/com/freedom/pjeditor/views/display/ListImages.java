package com.freedom.pjeditor.views.display;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;

import com.freedom.pjeditor.R;
import com.freedom.pjeditor.model.SelectedImages;
import com.freedom.pjeditor.realm.RealmController;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by Mayur on 6/27/2017.
 */

public class ListImages extends AppCompatActivity {

    Context context;
    RecyclerView rvImages;

    private Realm realm;
    ArrayList<String> photoPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_images);

        context = ListImages.this;
        realm = RealmController.getInstance().getRealm();
        rvImages = (RecyclerView) findViewById(R.id.rvImages);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit");
        }

        photoPaths = getIntent().getStringArrayListExtra("photoPaths");

        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                storePath(photoPaths);
                return new Object();
            }
        }).subscribe();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvImages.setLayoutManager(layoutManager);

        ImageAdapter imageAdapter = new ImageAdapter(ListImages.this, photoPaths);

        rvImages.setAdapter(imageAdapter);
        rvImages.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void storePath(ArrayList<String> photoPaths) {
        for (String photoPath : photoPaths) {

            realm.beginTransaction();

            // Creating an Increment Id for Model
            Number currentIdNum = realm.where(SelectedImages.class).max(SelectedImages.IMAGE_ID);
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }

            SelectedImages selectedImage = new SelectedImages();
            selectedImage.setImageId(nextId);
            selectedImage.setImagePath(photoPath);

            realm.copyToRealmOrUpdate(selectedImage);
            realm.commitTransaction();
        }
    }
}