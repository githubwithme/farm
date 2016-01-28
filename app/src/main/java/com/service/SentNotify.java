package com.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.farm.R;

public class SentNotify extends Service
{
    AlertDialog dialog;
    private static final String TAG = "NotificationService";

    public static final String ACTION_NOTIFICATION_CONTROL = "action_notification_control";
    public static final String KEY_COMMAND_SHOW = "show_notification";
    public static final String KEY_COMMAND_UPDATE = "update_notification";
    public static final String KEY_COMMAND_REMOVE = "remove_notification";

    public static final String TIME_KEY = "time_key";
    public static final int NOTIFICATIN_ID = 101;
    String action;
    private Notification mNotification;
    private NotificationManager manager;
    private PendingIntent pendingIntent;
    private Intent intent;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        action = intent.getAction();
        createNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    /*
     * 创建Notification对象
     */
    public void createNotification()
    {
        if (mNotification == null)
        {
            mNotification = new Notification();
            mNotification.icon = R.drawable.logo;
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;// 表示正处于活动中
            mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
            intent = new Intent(this, SentNotify.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            mNotification.contentIntent = pendingIntent;

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_new);

            contentView.setImageViewResource(R.id.content_view_image, R.drawable.logo);
            mNotification.contentView = contentView;
        }
        startForeground(NOTIFICATIN_ID, mNotification);

    }


}
