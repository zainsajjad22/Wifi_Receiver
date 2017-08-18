package zainsajjad.wificonnected;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
public class WifiReceiver extends BroadcastReceiver {
    private final static String TAG = WifiReceiver.class.getSimpleName();
    @Override
    public void onReceive(final Context context, final Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
                && WifiManager.WIFI_STATE_ENABLED == wifiState) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Wifi is now enabled");
            }
            context.startService(new Intent(context, WifiActiveService.class));
        }    }
    public static class WifiActiveService extends Service {
        private final static String TAG = WifiActiveService.class.getSimpleName();
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    WifiInfo info = wifiManager.getConnectionInfo();
                    String mac = info.getMacAddress();
                    String ssid = info.getSSID();
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "The SSID & MAC are " + ssid + " " + mac);
                    }
                    createNotification(ssid, mac);
                    stopSelf();
                }
            }, 1000);
            return START_NOT_STICKY;
        }
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }    }
}
