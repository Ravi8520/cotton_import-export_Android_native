package com.cotton.importexport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {



    private static RequestOptions userAvatarRequestOption;
    public static final String INDIAN_RUPEES = "\u20B9";

    public static final int LoadMoreBeforeNumberOfItems = 10;



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null)
            return RequestBody.create(MultipartBody.FORM, "");
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);

    }




    public interface StandardStatusCodes {
        int SUCCESS = 200;
        int IN_PROGRESS = 201;
        int No_More_Records = 204;
        int BAD_REQUEST = 400;
        int POLICY_NOT_FULL_FILLED = 420;
        int INTERNAL_SERVER_ERROR = 500;
        int NO_DATA_FOUND = 404;
        int CONFLICT = 409;
        int UNAUTHORISE = 401;
        int NOTACCEPTABLE = 406;
        int DUPLICATE_ERROR = 208;
    }

    // Get SHAKey For FB Login
    public static String getSHAKey(Context context) {
        String SHA_KEY = "";
        PackageInfo info;
        try {

            info = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);

            Log.i("TAG", context.getApplicationContext().getPackageName());

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key ", something);
                SHA_KEY = something;
                System.out.println("Hash key " + something);
            }

        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return SHA_KEY;
    }

    public static boolean isInThePast(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long now = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long then = calendar.getTimeInMillis();
        return now > then;
    }

    public static String getyyyyMMddformat(String dateStr) {
        String dateFormate = "";
        try {
          //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            sdf.applyPattern("dd-MM-yyyy HH:mm");
            dateFormate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormate;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void setImage(String url, Context context, final ProgressBar progressBar, ImageView iv) {
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv);
    }
}
