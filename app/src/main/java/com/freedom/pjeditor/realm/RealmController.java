package com.freedom.pjeditor.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.freedom.pjeditor.model.SelectedImages;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Mayur on 2/2/2017.
 */

public class RealmController {

    private static RealmController instance;
    private Realm realm;

    private RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    // =======================================================================================================
    // ========================================= ALL MODELS DEFINED HERE =====================================
    // =======================================================================================================

    // ========================================= Selected Images ==============================================

    // Find all objects in SelectedImages
    public List<SelectedImages> getAllSelectedImages() {

        return realm.where(SelectedImages.class).findAll();
    }

    // Find Single Selected Image by Id
    public SelectedImages getSelectedImageById(String imageId) {
        return realm.where(SelectedImages.class).equalTo(SelectedImages.IMAGE_ID, imageId).findFirst();
    }

    // Find Single Selected Image by Path
    public SelectedImages getSelectedImageByPath(String imagePath) {
        return realm.where(SelectedImages.class).equalTo("imagePath", imagePath).findFirst();
    }

    // Clear all objects from SelectedImages
    public void clearAllImages() {

        realm.beginTransaction();
        realm.delete(SelectedImages.class);
        realm.commitTransaction();
    }
}