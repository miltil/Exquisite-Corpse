package com.megan.exquisitecorpse.capstone;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.ImageView;
import java.util.List;

public class GalleryAdapter extends ArrayAdapter<GalleryPicture> {

    List<GalleryPicture> drawings;

    public GalleryAdapter(Activity context, List<GalleryPicture> drawings){
        super(context, 0, drawings);
        this.drawings = drawings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GalleryPicture galleryPicture = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_drawing, parent, false);
        }

        ImageView drawingView = (ImageView) convertView.findViewById(R.id.display_drawing_view);
        byte[] bytePicture = galleryPicture.full_drawing;
        Bitmap bitmapPicture = Utility.getImage(bytePicture);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapPicture, 200, 400, true);
        drawingView.setImageBitmap(resizedBitmap);

        return convertView;
    }

    @Override
    public void add(GalleryPicture object) {
        super.add(object);
    }

    public int getLength(){
        int listLength = drawings.size();
        return listLength;
    }

}
