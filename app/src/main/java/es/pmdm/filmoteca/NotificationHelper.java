package es.pmdm.filmoteca;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "filmoteca_channel";
    private static final int NOTIFICATION_ID_DELETE = 1;
    private static final int NOTIFICATION_ID_ADD = 2;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = context.getString(R.string.notification_channel_name);
            String channelDescription = context.getString(R.string.notification_channel_description);

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showFilmDeletedNotification(Context context, String filmTitle) {
        createNotificationChannel(context);

        String title = context.getString(R.string.notification_film_deleted_title);
        String text = context.getString(R.string.notification_film_deleted_text, filmTitle);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_delete)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_DELETE, builder.build());
    }

    public static void showFilmAddedNotification(Context context, Film film, int filmPosition) {
        createNotificationChannel(context);

        String[] genres = context.getResources().getStringArray(R.array.genres);
        String[] formats = context.getResources().getStringArray(R.array.formats);

        String title = context.getString(R.string.notification_film_added_title);
        String text = context.getString(R.string.notification_film_added_text, film.getTitle());

        String bigText = context.getString(R.string.notification_film_details) + "\n\n" +
                context.getString(R.string.notification_film_title_label, film.getTitle()) + "\n" +
                context.getString(R.string.notification_film_director_label, film.getDirector()) + "\n" +
                context.getString(R.string.notification_film_year_label, film.getYear()) + "\n" +
                context.getString(R.string.notification_film_genre_label, genres[film.getGenre()]) + "\n" +
                context.getString(R.string.notification_film_format_label, formats[film.getFormat()]) + "\n\n" +
                context.getString(R.string.notification_film_tap_to_edit);

        Intent intent = new Intent(context, FilmEditActivity.class);
        intent.putExtra("FILM_POSITION", filmPosition);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                filmPosition,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_input_add)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText)
                        .setBigContentTitle(title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID_ADD, builder.build());
    }
}