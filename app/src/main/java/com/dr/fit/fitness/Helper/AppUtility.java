package com.dr.fit.fitness.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dr.fit.fitness.Activity.HomePageActivity;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class AppUtility {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RecyclerView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setTransparentStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = activity.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static void makeGrayScalePhoto(ImageView IVPhoto){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        IVPhoto.setColorFilter(filter);
    }

    public static void FullScreencall(Activity activity) {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static String checkAndAddZero(int Number){
        if(Number < 10){
            return "0" + Number;
        }else{
            return String.valueOf(Number);
        }
    }

    public static void logoutProcesses(Context context){
        GSharedPreferences sharedPreferences = new GSharedPreferences(context);

        sharedPreferences.SET_USERID(0);
        sharedPreferences.SET_FACEBOOKID("");
        sharedPreferences.SET_PROFILE_IMAGE("");
        sharedPreferences.SET_USER_POINT(0);
        sharedPreferences.SET_IS_PREMUIM(false);
        sharedPreferences.SET_STEP("");
        sharedPreferences.SET_NAME("");
        sharedPreferences.SET_SURNAME("");
        sharedPreferences.SET_EMAIL("");
        sharedPreferences.SET_BIRTHDAY("");
        sharedPreferences.SET_GENDER(0);
        sharedPreferences.SET_PASSWORD("");
    }

    public static void checkIfLogedIn(Context context){
        GSharedPreferences sharedPreferences = new GSharedPreferences(context);

        if(!sharedPreferences.GET_NAME().equals("")){
            context.startActivity(new Intent(context, HomePageActivity.class));
            ((Activity)context).finish();
        }
    }

    public static void checkProfilePictureAndLoad(Context context, RoundedImageView RIVProfilePicture){
        GSharedPreferences sharedPreferences = new GSharedPreferences(context);

        Log.d("Kontrol", "PP: " + sharedPreferences.GET_PROFILE_IMAGE());

        if (!sharedPreferences.GET_PROFILE_IMAGE().equals("http://dededevops.com/media/")){
            Picasso.with(context).load(sharedPreferences.GET_PROFILE_IMAGE()).into(RIVProfilePicture);
        }else{
            RIVProfilePicture.setImageResource(R.drawable.profile_picture);
        }
    }

    public static void isVideosFolderCreatedAndOpen(){
        File folderCheck = new File(Environment.getExternalStorageDirectory() + "/DrFitVideos");

        if(!folderCheck.exists()) folderCheck.mkdirs();
    }

    public static boolean isVideoExistInVideosFolder(String VideoName){
        File videoExist = new File(Environment.getExternalStorageDirectory() + "/DrFitVideos/" + VideoName);

        return videoExist.exists();
    }

    public static void downloadVideos(Context mContext, List<String> VideosName, BroadcastReceiver onComplete){
        for (int i = 0; i < VideosName.size(); i++) {
            if(!isVideoExistInVideosFolder(VideosName.get(i))){
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://dededevops.com/media/" + VideosName.get(i)));

                request.setTitle("Exercises Downloading");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.setDestinationInExternalPublicDir("DrFitVideos", VideosName.get(i));
                DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            }
        }
    }

}