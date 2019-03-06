package com.example.prasanthkumar.moviestar.HomeWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.prasanthkumar.moviestar.R;
import com.example.prasanthkumar.moviestar.UIScreens.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MovieStarWidget extends AppWidgetProvider {
    private  static final  String movie_des_key = "movie_des_key";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("movies_description",Context.MODE_PRIVATE);
        String widgetText = sharedPreferences.getString(movie_des_key,null);

        //  CharSequence widgetText = MoviesAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.movie_star_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

