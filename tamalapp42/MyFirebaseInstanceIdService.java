package mejia.oscar.tamalapp42;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    public static final String TAG = "Cena";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken( s );


        String token = FirebaseInstanceId.getInstance().getToken(  );
        Log.d(TAG, "Token: " + token);
    }
}
