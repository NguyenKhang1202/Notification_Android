package vn.edu.hust.sis.khangnv.notifyme;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

// giúp tạo và đăng kí channel với app
// cần thêm: android:name=".MyApplication" vào Manifests
public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push_notification_id";
    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }
    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
}
