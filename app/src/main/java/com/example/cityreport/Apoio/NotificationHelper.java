package com.example.cityreport.Apoio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.cityreport.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "my_channel_id";
    private static final CharSequence CHANNEL_NAME = "My Channel";
    public static void showNotification(Context context, String title, String content) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Verifica se a versão do Android é igual ou superior ao Android Oreo (API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification notification = builder.build();
        // Id único para a notificação
        int notificationId = 1;
        // Exibe a notificação
        notificationManager.notify(notificationId, notification);
    }
}