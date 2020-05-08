package com.dr.fit.fitness.Helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dr.fit.fitness.R;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by batuhanozkaya on 20.05.2017.
 */

public class Utility {
    /** REQUEST CODES FOR PERMISSON **/
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 100;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 102;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 103;
    public static final int REQUEST_INTERNET = 104;
    /** ACTIVITY RESULT CODES **/
    public static final int SELECT_FROM_CAMERA = 700;
    public static final int SELECT_FROM_GALLERY = 701;
    /** FOR PHOTO SELECT (ImagePath)**/
    public static Uri pictureUri;
    /** FOR Alert Dialog ICON**/
    public enum AlertDialogIcon{
        ALERT(android.R.drawable.ic_dialog_alert),
        INFO(android.R.drawable.ic_dialog_info),
        EMAIL(android.R.drawable.ic_dialog_email),
        DIALER(android.R.drawable.ic_dialog_dialer),
        MAP(android.R.drawable.ic_dialog_map);

        private final int alertDialogIcon;

        AlertDialogIcon(int alertDialogIcon) {
            this.alertDialogIcon = alertDialogIcon;
        }
    }
    /** FOR DATE-TIME DIFFERENCE **/
    public enum TimeDifference{
        SECOND(0),
        MINUTE(1),
        HOUR(2),
        DAY(3),
        MONTH(4),
        YEAR(5);

        private final int timeDifference;

        TimeDifference(int timeDifference) {
            this.timeDifference = timeDifference;
        }
    }

    /** REQUEST PERMISSON - START **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean requestPermission(final Context context, int REQUEST_WHICH_PERMISSON) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(REQUEST_WHICH_PERMISSON == REQUEST_READ_EXTERNAL_STORAGE){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WHICH_PERMISSON);
                    return false;
                } else {
                    return true;
                }
            }else if(REQUEST_WHICH_PERMISSON == REQUEST_WRITE_EXTERNAL_STORAGE){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WHICH_PERMISSON);
                    return false;
                } else {
                    return true;
                }
            }else if(REQUEST_WHICH_PERMISSON == REQUEST_ACCESS_FINE_LOCATION){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_WHICH_PERMISSON);
                    return false;
                } else {
                    return true;
                }
            }else if(REQUEST_WHICH_PERMISSON == REQUEST_ACCESS_COARSE_LOCATION){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_WHICH_PERMISSON);
                    return false;
                } else {
                    return true;
                }
            }else if(REQUEST_WHICH_PERMISSON == REQUEST_INTERNET){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.INTERNET}, REQUEST_WHICH_PERMISSON);
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
    }
    /** REQUEST PERMISSON - FINISH **/

    /** INTERNET CONNECTION CHECK - START **/
    public static boolean haveInternetConnection(Context context, boolean AlertDialogAppear) throws InterruptedException, IOException {
        if(requestPermission(context, REQUEST_INTERNET)){
            if(Runtime.getRuntime().exec ("ping -c 1 google.com").waitFor() == 0){
                return true;
            }else{
                if(AlertDialogAppear){
                    showAlertDialogOneButton(context, context.getString(R.string.dont_have_internet_connection));
                }
            }
        }else{
            showAlertDialogOneButton(context, "Need \"Internet\" permission for this feature.");
        }
        return false;
    }
    /** INTERNET CONNECTION CHECK - FINISH **/

    /** GPS CHECK - START **/
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    /** GPS CHECK - FINISH **/

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** ALERT DIALOG - START **/
    public static void showAlertDialogOneButton(Context context, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogOneButton(Context context, String Title, String Message, String PositiveButton, boolean needIcon, AlertDialogIcon icon){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Message);
        builder.setCancelable(false);

        if(!Title.equals("")){
            builder.setTitle(Title);
        }

        if(PositiveButton.equals("")){
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }else{
            builder.setPositiveButton(PositiveButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }
        /** Setting a Icon **/
        if(needIcon){
            builder.setIcon(icon.alertDialogIcon);
        }
        AlertDialog alert = builder.create();
        alert.show();
    }
    /** ALERT DIALOG - FINISH **/

    /** DATE-TIME DIFFERENCE - START **/
    public static long DateTimeDifference(long currentTime, long previousTime, TimeDifference timeDifference) {
        if(timeDifference.timeDifference == 0){ //GETTING DIFFERENCE WITH SECONDS
            return (currentTime - previousTime) / 1000;
        }else if(timeDifference.timeDifference == 1){ //GETTING DIFFERENCE WITH MINUTES
            return ((currentTime - previousTime) / 1000) / 60;
        }else if(timeDifference.timeDifference == 2){ //GETTING DIFFERENCE WITH HOURS
            return (((currentTime - previousTime) / 1000) / 60) / 60;
        }else if(timeDifference.timeDifference == 3){ //GETTING DIFFERENCE WITH DAYS
            return ((((currentTime - previousTime) / 1000) / 60) / 60) / 24;
        }else if(timeDifference.timeDifference == 4){ //GETTING DIFFERENCE WITH MONTHS
            return (((((currentTime - previousTime) / 1000) / 60) / 60) / 24) / 30;
        }else{ //GETTING DIFFERENCE WITH YEARS
            return ((((((currentTime - previousTime) / 1000) / 60) / 60) / 24) / 30) / 365;
        }
    }
    /** DATE-TIME DIFFERENCE - FINISH **/

    /** STRING CHECKS - START **/
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /** STRING CHECKS - FINISH **/

    /** SELECT & TAKE PHOTO - GET PHOTO PATH  - START **/
    public static void choosePhoto(final Context context, final Activity activity){
        if(Utility.requestPermission(context, REQUEST_READ_EXTERNAL_STORAGE)){
            final CharSequence[] items = { context.getString(R.string.choose_from_gallery), context.getString(R.string.cancel)};

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(context.getString(R.string.choose));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                     if (items[item].equals(context.getString(R.string.choose_from_gallery))) {
                        fromGallery(activity);
                    } else if (items[item].equals(context.getString(R.string.cancel))) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    private static void fromCamera(Context context, Activity activity){
        String fileName = System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        pictureUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        activity.startActivityForResult(intent, SELECT_FROM_CAMERA);
    }

    private static void fromGallery(Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.choose_from_gallery)), SELECT_FROM_GALLERY);
    }

    @SuppressWarnings("deprecation")
    public static String getPath(Uri selectedImageUri, Activity activity, Intent data, boolean fromCameraOrGallery) {
        String returnPath;
        if(fromCameraOrGallery){
            /** From camera operation **/
            Cursor cursor = activity.managedQuery(selectedImageUri, new String[]{ MediaStore.Images.Media.DATA }, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
            returnPath = selectedImageUri.getPath();
        }else{
            /** From gallery operation **/
            int columnIndex = 0;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            try {
                activity.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            }

            returnPath = cursor.getString(columnIndex);
            cursor.close();
        }

        return returnPath;
    }
    /** Compress Image - START **/
    public static String compressImage(Context context, String imagePath) {
        final float maxHeight = 1024.0f;
        final float maxWidth = 1024.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        assert scaledBitmap != null;
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        if (bmp != null) {
            bmp.recycle();
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename(context);
        try {
            out = new FileOutputStream(filepath);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static String getFilename(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + context.getApplicationContext().getPackageName() + "/Files/Compressed");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        return (mediaStorageDir.getAbsolutePath() + "/" + mImageName);
    }
    /** Compress Image - FINISH **/
    /** SELECT & TAKE PHOTO - GET PHOTO PATH  - FINISH **/

}