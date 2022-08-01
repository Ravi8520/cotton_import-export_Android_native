package com.ecotton.impex.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.MyContractActivity;
import com.ecotton.impex.activities.NewsActivity;
import com.ecotton.impex.activities.PostDetailActivity;
import com.ecotton.impex.activities.SplashActivity;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.ValidationUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = PushNotificationService.class.getSimpleName();
    String click_action = "", orderID = "", type = "",
            post_id = "", seller_id = "", buyer_id = "", posted_company_id = "", negotiation_by_company_id = "", post_type = "",
            company_id = "", user_id = "", user_type = "";

    public PushNotificationService() {
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }

    private String getNotificationChannelId() {
        return getString(R.string.notification_channel_id);
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String channelId = getNotificationChannelId();
                String channelName = getNotificationChannelId();

                NotificationChannel mNotificationChannel = new NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(mNotificationChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createNotificationId() {
        int notificationId = 0;

        try {
            //notificationId = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(new Date()));
            String mill = new SimpleDateFormat("ddHHmmssSSS", Locale.US).format(new Date());
            mill = mill.substring(mill.length() - 5);
            PrintLog.e(TAG, "Notification ID " + mill);
            notificationId = Integer.parseInt(mill);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notificationId;
    }

    public static void removeNotification(Context mContext, int notificationId) {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeAllNotifications(Context mContext) {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Handle received push notification message to display push notification in notification bar.
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            if (remoteMessage != null) {
                int notificationId = createNotificationId();

                PendingIntent pendingIntent = null;
                    /*Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher);*/

                String title = "";
                String message = "";
                String orderId = "";

                if (remoteMessage.getNotification() != null) {
                    if (ValidationUtil.validateString(remoteMessage.getNotification().getTitle())) {
                        title = remoteMessage.getNotification().getTitle();
                    }

                    if (ValidationUtil.validateString(remoteMessage.getNotification().getBody())) {
                        message = remoteMessage.getNotification().getBody();
                    }
                }
                Log.e("Data : ", remoteMessage.getData().toString());
                Log.e("Notification : ", remoteMessage.getNotification().toString());
                try {
                    click_action = remoteMessage.getNotification().getClickAction();
                    if (click_action.equals("Negotiation")) {
                        company_id = remoteMessage.getData().get("company_id");
                        user_id = remoteMessage.getData().get("user_id");
                        user_type = remoteMessage.getData().get("user_type");
                        post_id = remoteMessage.getData().get("post_id");
                        seller_id = remoteMessage.getData().get("seller_id");
                        buyer_id = remoteMessage.getData().get("buyer_id");
                        posted_company_id = remoteMessage.getData().get("posted_company_id");
                        negotiation_by_company_id = remoteMessage.getData().get("negotiation_by_company_id");
                        post_type = remoteMessage.getData().get("post_type");
                    }else if (click_action.equals("MakeDeal")) {
                        company_id = remoteMessage.getData().get("company_id");
                        user_id = remoteMessage.getData().get("user_id");
                        user_type = remoteMessage.getData().get("user_type");
                    }else if (click_action.equals("News")) {

                    }
                } catch (Exception e) {
                    Log.e("Error : ", e.getMessage());
                }

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, getNotificationChannelId())
                                .setSmallIcon(R.drawable.ic_app_notification)
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setContentTitle(title)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setGroup(getNotificationChannelId())
                                .setPriority(NotificationCompat.PRIORITY_MAX);


                pendingIntent = getNotificationPendingIntent(this,notificationId);
                if (pendingIntent != null) {
                    notificationBuilder.setContentIntent(pendingIntent);
                }

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                createNotificationChannel(notificationManager);
                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getNotificationPendingIntent(Context mContext,int notificationId) {
        PendingIntent pendingIntent = null;

        try {
            Intent mIntent;
            if (click_action.equals("Negotiation")) {
                mIntent = new Intent(this, PostDetailActivity.class);
                mIntent.putExtra("screen", "negotiation");
                mIntent.putExtra("post_id", post_id);
                mIntent.putExtra("seller_id", seller_id);
                mIntent.putExtra("buyer_id", buyer_id);
                mIntent.putExtra("posted_company_id", posted_company_id);
                mIntent.putExtra("company_id", company_id);
                mIntent.putExtra("user_id", user_id);
                mIntent.putExtra("user_type", user_type);
                mIntent.putExtra("negotiation_by_company_id", negotiation_by_company_id);
                mIntent.putExtra("post_type", post_type);
            }else if (click_action.equals("MakeDeal")) {
                mIntent = new Intent(this, MyContractActivity.class);
                mIntent.putExtra("company_id", company_id);
                mIntent.putExtra("user_id", user_id);
                mIntent.putExtra("user_type", user_type);
            }else if (click_action.equals("News")) {
                mIntent = new Intent(this, NewsActivity.class);
            } else {
                mIntent = new Intent(this, SplashActivity.class);
            }

            mIntent.putExtra("pushNotification", true);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                pendingIntent = PendingIntent.getActivity(this,
                        notificationId, mIntent, PendingIntent.FLAG_MUTABLE);

            else
                pendingIntent = PendingIntent.getActivity(this,
                        notificationId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            e.printStackTrace();
            Intent mIntent;
            mIntent = new Intent(this, SplashActivity.class);
            mIntent.putExtra("pushNotification", true);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                pendingIntent = PendingIntent.getActivity(this,
                        notificationId, mIntent, PendingIntent.FLAG_MUTABLE);
            else
                pendingIntent = PendingIntent.getActivity(this,
                        notificationId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        return pendingIntent;
    }

}
