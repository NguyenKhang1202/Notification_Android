package vn.edu.hust.sis.khangnv.notifyme;

import static vn.edu.hust.sis.khangnv.notifyme.MyApplication.CHANNEL_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button_notify;
    /*private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";*/
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        /*createNotificationChannel();*/
    }

    // gửi notification bằng cách dùng đtg NotificationManager dùng hàm notify()
    public void sendNotification() {
        // get NotificationManager từ hệ thống
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    // tạo đối tượng builder cho từng notification
    // nó như data cho notification
    private NotificationCompat.Builder getNotificationBuilder(){
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;
    }

    // tạo channel cho notification
    // mỗi channel có 1 id riêng, cài đặt các attr cho channel: am thanh, rung,...
    // sử dụng NotificationManager dể tạo
    /*public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }*/
}