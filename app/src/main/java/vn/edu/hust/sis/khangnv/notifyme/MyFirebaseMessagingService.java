package vn.edu.hust.sis.khangnv.notifyme;

import static vn.edu.hust.sis.khangnv.notifyme.MainActivity.NOTIFICATION_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "TAG 1";
    PendingIntent notificationPendingIntent;
    private String title;
    private String body;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        super.onMessageReceived(remoteMessage);

        // Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "field 1: " + data.get("username"));
            Log.d(TAG, "field 2: " + data.get("password"));

        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        sendNotification(title, body);
    }

    private void sendNotification(String title, String body) {

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.ic_android)
                        .setContentIntent(notificationPendingIntent)
                        .setAutoCancel(true);


        /*Notification notification = notificationBuilder.build();*/
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(mNotifyManager != null){
            mNotifyManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("FCM", s);
    }
}
