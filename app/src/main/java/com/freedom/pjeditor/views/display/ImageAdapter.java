package com.freedom.pjeditor.views.display;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.freedom.pjeditor.R;
import com.freedom.pjeditor.views.cropper.CropperActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mayur on 6/27/2017.
 */

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.FileViewHolder> {

    private final ArrayList<String> paths;
    private final Context context;
    private int imageSize;

    class FileViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        FileViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = paths.get(getAdapterPosition());

                    Intent crop = new Intent(context, CropperActivity.class);
                    crop.putExtra("path", path);
                    context.startActivity(crop);
                }
            });
        }
    }

    ImageAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
        setColumnNumber(context, 3);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNum;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        String path = paths.get(position);
        Glide.with(context).load(new File(path))
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(imageSize, imageSize)
                .placeholder(droidninja.filepicker.R.drawable.image_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }
}