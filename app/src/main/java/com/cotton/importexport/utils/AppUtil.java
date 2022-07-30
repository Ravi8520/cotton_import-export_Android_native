package com.cotton.importexport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cotton.importexport.R;
import com.cotton.importexport.activities.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AppUtil {
    private static RequestOptions mDefaultRequestOptions;
    private static RequestOptions mUserAvtarRequestOptions;
    private static RequestOptions mPharmacyRequestOptions;
    private static RequestOptions mPrescriptionRequestOptions;
    private static RequestOptions mInvoiceRequestOptions;

    Activity mActivity;
    public static boolean isEncryptionDecryptionCode = true;

    //language code
    public static final String ENG = "en";
    public static final String GUJ = "gu";

    public AppUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void hideSoftKeyboard(int timeout) {
        try {
            if (this.mActivity != null) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager != null) {
                            inputMethodManager.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getRootView().getWindowToken(), 2);
                        }
                    }
                }, (long) timeout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            } else {
                return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
            }
        }
        return false;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    } public static void showToast1(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void shareText(Context context, String msg) {
        if (ValidationUtil.validateString(msg)) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, msg);
            context.startActivity(Intent.createChooser(i, "Share"));
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public static RequestOptions getDefaultRequestOptions()
    {
        if (mDefaultRequestOptions == null) {
            mDefaultRequestOptions = new RequestOptions();
            mDefaultRequestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            mDefaultRequestOptions.placeholder(R.drawable.placeholder);
            mDefaultRequestOptions.centerCrop();
        }
        return mDefaultRequestOptions;
    }
    public static RequestOptions getDefaultRequestOptions1()
    {
        if (mDefaultRequestOptions == null) {
            mDefaultRequestOptions = new RequestOptions();
            mDefaultRequestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            mDefaultRequestOptions.placeholder(R.drawable.pdf_placeholder);
            mDefaultRequestOptions.centerCrop();
        }
        return mDefaultRequestOptions;
    }


    public static RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null)
            return RequestBody.create(MultipartBody.FORM, "");
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);

    }


    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;

        public static final int FAILURE_RESULT = 1;

        public static final String PACKAGE_NAME = "com.example.pharma";

        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";
        public static final String LOCATION_POSTAL_CODE = PACKAGE_NAME + ".LOCATION_DATA_POSTAL_CODE";
        public static final String LOCATION_ADMIN_AREA = PACKAGE_NAME + ".LOCATION_DATA_ADMIN_AREA";
        public static final String LOCATION_COUNTRY_NAME = PACKAGE_NAME + ".LOCATION_DATA_COUNTRY_NAME";


    }


    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String getVersionName(Context mContext) {
        String mVersionName = "";
        try {
            mVersionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return mVersionName;
    }

    public static void openUrl(Context context, String url) {
        if (ValidationUtil.validateString(url)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
    }

    public static void autoLogout(Activity mActivity) {
        SessionUtil mSessionUtil = new SessionUtil(mActivity);
        String fcmToken = mSessionUtil.getFcmtoken();
        mSessionUtil.logOut();
        mSessionUtil.setFCMToken(fcmToken);
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));

                /*if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }*/
            }
            return null;
        }
    };

    public static void changeLocale(Context context, String launguage) {
        Locale myLocale = new Locale(launguage);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static String getFileNameFromUri(Context mContext, Uri fileUri) {
        String result = "";

        if (mContext != null && fileUri != null) {
            Cursor mCursor = mContext.getContentResolver().query(fileUri, null, null, null, null);

            try {
                if (mCursor != null && mCursor.moveToFirst()) {
                    int fileNameIndex = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    if (!mCursor.isNull(fileNameIndex)) {
                        result = mCursor.getString(fileNameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mCursor.close();
            }
        }

        return result;
    }

    public static String getFileMimeTypeFromUri(Context mContext, Uri fileUri) {
        String result = "";

        if (mContext != null && fileUri != null) {
            try {
                result = mContext.getContentResolver().getType(fileUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static byte[] getBytesDataFromUri(Context mContext, Uri fileUri) {
        byte[] result = null;

        if (mContext != null && fileUri != null) {
            try {
                InputStream inputStream = mContext.getContentResolver().openInputStream(fileUri);
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }

                result = byteBuffer.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static String getFileSize(Context context, Uri uri) {
        String fileSize = "";
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {

                // get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileSize;
    }

    public static void showProgressDialog(){
         CustomDialog customDialog;
        customDialog = new CustomDialog();
    }


    public static String encodeToBase64(Context mContext, Uri imageUri) {
        String base64Str = "";
        if(imageUri!=null) {
            try {
                InputStream imageStream = mContext.getContentResolver().openInputStream(imageUri);
                Bitmap image = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                base64Str = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return base64Str;
    }

    public static Bitmap decodeToBase64(String encodedImage) {
        Bitmap image = null;
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return image;
    }


    public static void openDialPad(Context mContext, String mobNo) {
        if (ValidationUtil.validateString(mobNo)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobNo));
            mContext.startActivity(intent);
        }
    }

    //Disabled All, Cut, Copy and Paste actions
    public static void setCustomSelectionActionModeCallback(EditText editText){
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    public static RequestOptions getPlaceholderRequestOption() {
        if (mPrescriptionRequestOptions == null) {
            mPrescriptionRequestOptions = new RequestOptions();
            mPrescriptionRequestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            mPrescriptionRequestOptions.placeholder(R.drawable.placeholder);
            mPrescriptionRequestOptions.centerCrop();
        }
        return mPrescriptionRequestOptions;
    }
}
