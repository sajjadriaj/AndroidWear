package mpsc.android.smartactivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;


/**
 * Created by gambit on 12/24/2015.
 */
public class PhoneAccService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    public float[] vals = new float[3];
    private String LOG_TAG = null;

    @Override
    public void onCreate() {
        super.onCreate();
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG,"In onStartCommand");
        Toast.makeText(PhoneAccService.this,"Service Started",Toast.LENGTH_LONG).show();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //Get the System Sensor Service
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG,"In onDestroy");
        Toast.makeText(PhoneAccService.this,"Service Stopped",Toast.LENGTH_LONG).show();
        senSensorManager.unregisterListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG,"In onBind");
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.8f;
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Calibration Needed
            vals[0] = sensorEvent.values[0];
            vals[1] = sensorEvent.values[1];
            vals[2] = sensorEvent.values[2];

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(HandHeldActivity.mBroadcastMotionDataAction);
            broadcastIntent.putExtra("x_val", Float.toString(vals[0]));
            broadcastIntent.putExtra("y_val", Float.toString(vals[1]));
            broadcastIntent.putExtra("z_val", Float.toString(vals[2]));
            sendBroadcast(broadcastIntent);
        }
    }

    public float[] getVals(){
        return vals;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
