package android.example.com.squawker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.security.PrivateKey;
import java.util.Map;

public class SquawkFirrebaseMessagingService extends FirebaseMessagingService {

    private static final int SQUAWKER_NOTIFICATION_INTENT = 321;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO/COMPLETED (2) As part of the new Service - Override onMessageReceived. This method will
        // be triggered whenever a squawk is received. You can get the data from the squawk
        // message using getData(). When you send a test message, this data will include the
        // following key/value pairs:
        // test: true
        // author: Ex. "TestAccount"
        // authorKey: Ex. "key_test"
        // message: Ex. "Hello world"
        // date: Ex. 1484358455343
        Map<String, String> data = remoteMessage.getData();
        if (data.containsKey(SquawkContract.COLUMN_AUTHOR) && data.containsKey(SquawkContract.COLUMN_AUTHOR_KEY)
                && data.containsKey(SquawkContract.COLUMN_DATE) && data.containsKey(SquawkContract.COLUMN_DATE)) {
            // TODO/COMPLETED (3) As part of the new Service - If there is message data, get the data using
            // the keys and do two things with it :
            // 1. Display a notification with the first 30 character of the message
            // 2. Use the content provider to insert a new message into the local database
            // Hint: You shouldn't be doing content provider operations on the main thread.
            // If you don't know how to make notifications or interact with a content provider
            // look at the notes in the classroom for help.
            sendNotification(getApplicationContext(),
                    data.get(SquawkContract.COLUMN_MESSAGE).substring(0, 29));

            insertSquawk(data);
        }
    }

    private void insertSquawk(final Map<String, String> data) {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SquawkContract.COLUMN_AUTHOR, data.get(SquawkContract.COLUMN_AUTHOR));
                contentValues.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(SquawkContract.COLUMN_AUTHOR_KEY));
                contentValues.put(SquawkContract.COLUMN_MESSAGE, data.get(SquawkContract.COLUMN_MESSAGE));
                contentValues.put(SquawkContract.COLUMN_DATE, data.get(SquawkContract.COLUMN_DATE));
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, contentValues);
                return null;
            }
        };

        asyncTask.execute();
    }

    private static void sendNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle("Squawker Notification")
                        .setContentText(message)
                        .setContentIntent(pendingIntent(context))
                        .setSmallIcon(R.drawable.ic_duck)
                        .setLargeIcon(largeIcon(context))
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(SQUAWKER_NOTIFICATION_INTENT, notificationBuilder.build());
    }


    private static PendingIntent pendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, SQUAWKER_NOTIFICATION_INTENT,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_duck);
        return bitmap;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
