package android.example.com.squawker.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

public class SquawkFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String LOG_TAG = SquawkFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, "Refreshed Token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {
    }


}
