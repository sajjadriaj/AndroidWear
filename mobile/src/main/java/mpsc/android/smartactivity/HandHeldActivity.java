package mpsc.android.smartactivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
//import android.support.v4.app.NotificationManagerCompat;
//import android.support.v4.app.NotificationCompat;
//import android.app.Notification;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;


public class HandHeldActivity extends AppCompatActivity {

    PhoneAccService accservice;
    boolean isBound = false;
    public static final String  mBroadcastMotionDataAction = "mpsc.android.smartactivity.SensorData";
    public static final String  mBroadcastWearMotionDataAction = "mpsc.android.smartactivity.WearSensorData";
    private IntentFilter mIntentFilter;
    public String[] XYZVals = new String[3];
    private String LOG_TAG = null;
    private TextView sensorDataText;
    private TextView wearSensorDataText;
    private GoogleApiClient apiClient;
    private MessageApi.MessageListener messageListener;
    private String remoteNodeId;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_held);
        mIntentFilter = new IntentFilter();

        mIntentFilter.addAction(mBroadcastMotionDataAction);
        mIntentFilter.addAction(mBroadcastWearMotionDataAction);
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
        sensorDataText = (TextView) findViewById(R.id.sensorData);
        wearSensorDataText = (TextView) findViewById(R.id.wearData);
//        Intent intent = new Intent(this, PhoneAccService.class);
//        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    @Override
    public void onResume()
    {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
        registerReceiver(mWearReceiver, mIntentFilter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        registerReceiver(mReceiver, mIntentFilter);
        registerReceiver(mWearReceiver, mIntentFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(mBroadcastMotionDataAction))
            {
                XYZVals[0] = intent.getStringExtra("x_val");
                Log.i(LOG_TAG,"x:" + XYZVals[0] + "\n");
                XYZVals[1] = intent.getStringExtra("y_val");
                Log.i(LOG_TAG,"y:" + XYZVals[1] + "\n");
                XYZVals[2] = intent.getStringExtra("z_val");
                Log.i(LOG_TAG, "z:" + XYZVals[2] + "\n");
                sensorDataText.setText(String.format("X Axis = %s \n Y Axis: %s \n Z Axis: %s", XYZVals[0], XYZVals[1], XYZVals[2]));
            }
        }
    };

    private BroadcastReceiver mWearReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(mBroadcastMotionDataAction))
            {
                sensorDataText.setText(String.format("Wear X Axis = %s \n Wear Y Axis: %s \n Wear Z Axis: %s", intent.getStringExtra("wear_x_val"),
                        intent.getStringExtra("wear_y_val"), intent.getStringExtra("wear_z_val")));
            }
        }
    };

    public void StartPhoneService(View view)
    {
        Intent intent = new Intent(this,PhoneAccService.class);
        startService(intent);

//        AccServiceOn serOn = new AccServiceOn();
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragment_place, serOn);
//        ft.commit();
    }

    public void StopPhoneService(View view)
    {
        Intent intent = new Intent(this,PhoneAccService.class);
        stopService(intent);
//        AccServiceOff serOff = new AccServiceOff();
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragment_place, serOff);
//        ft.commit();
    }
    public void StartWearAcc(View view)
    {
        Intent intent = new Intent(this,ListenerService.class);
        startService(intent);
    }

    public void StopWearAcc(View view)
    {
        Intent intent = new Intent(this,ListenerService.class);
        stopService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hand_held, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            PhoneAccService.LocalBinder binder = (PhoneAccService.LocalBinder) service;
//            accservice = binder.getService();
//            isBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            isBound = true;
//        }
//    };
}
