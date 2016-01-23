package mpsc.android.smartactivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {
    private static final String WEARABLE_DATA_PATH = "/wearable_data";
    public String[] XYZVals = new String[3];
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        DataMap dataMap;
        for (DataEvent event : dataEvents) {

            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {}
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                XYZVals[0] = dataMap.getString("wear_x_val");
                XYZVals[1] = dataMap.getString("wear_y_val");
                XYZVals[2] = dataMap.getString("wear_z_val");
                Log.v("myTag", "Wear_x_val " + dataMap.getString("wear_x_val") + "\n");
                Log.v("myTag", "Wear_y_val " + dataMap.getString("wear_y_val") + "\n");
                Log.v("myTag", "Wear_z_val " + dataMap.getString("wear_z_val") + "\n");
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(HandHeldActivity.mBroadcastWearMotionDataAction);
                broadcastIntent.putExtra("wear_x_val", XYZVals[0]);
                broadcastIntent.putExtra("wear_y_val", XYZVals[1]);
                broadcastIntent.putExtra("wear_z_val", XYZVals[2]);
                sendBroadcast(broadcastIntent);
            }
        }
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
}
