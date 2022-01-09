package mejia.oscar.tamalapp42;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "Cena";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived( remoteMessage );

        String from = remoteMessage.getFrom();
        Log.d( TAG, "Mensaje recibido de " + from );

       if( remoteMessage.getNotification() !=null){
           Log.d( TAG, "Notificación: " + remoteMessage.getNotification().getBody());
mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
       }

       if(remoteMessage.getData().size()>0){
           Log.d(TAG, "Data"  +  remoteMessage.getData());
       }



    }

    private void mostrarNotificacion(String title, String body) {
Intent intent = new Intent (this, MainActivity.class);
intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, intent, PendingIntent.FLAG_ONE_SHOT );

        Uri sounduuri = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this )
        .setSmallIcon(R.drawable.icono_transp).setContentTitle(title)
                .setContentText(body)
                .setAutoCancel( true )
                .setSound( sounduuri )
                .setContentIntent( pendingIntent );

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( 0, notificationBuilder.build() );

    }
}
