package com.megan.exquisitecorpse.capstone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.activeandroid.query.Select;

public class Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            GalleryPicture galleryPicture = new Select()
                    .from(GalleryPicture.class)
                    .orderBy("ID DESC")
                    .executeSingle();
            byte[] bytePicture = galleryPicture.full_drawing;
            Bitmap bitmapPicture = Utility.getImage(bytePicture);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            remoteViews.setImageViewBitmap(R.id.widget_image, bitmapPicture);

            // Opens the app when you click on the top banner
            Intent bannerIntent = new Intent(context, OpeningScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, bannerIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            Intent intent = new Intent(context, Widget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}