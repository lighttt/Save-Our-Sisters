package bootcamp.android.demoapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.security.Key;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity {

    int count = 0;
    int key,keyEvent;

    private static final String TAG = "MainActivity";
    private GifImageView mCurrent;
    private ImageView mPast;
    private TextView mCurrentEmergency,mPastEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started MainActivity");
        //storage permission
        requestStoragePermission();

        mCurrent = (GifImageView)findViewById(R.id.current);
        mCurrentEmergency = (TextView)findViewById(R.id.currentEmergency);
        mPast = (ImageView)findViewById(R.id.pastevents);
        mPastEvent = (TextView)findViewById(R.id.pastevents1);

        mCurrentEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent current = new Intent(MainActivity.this,LocationActivity.class);
                startActivity(current);
            }
        });

        mCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent current1 = new Intent(MainActivity.this,LocationActivity.class);
                startActivity(current1);
            }
        });

        mPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent past = new Intent(MainActivity.this,PastActivity.class);
                startActivity(past);
            }
        });
        mPastEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent past1 = new Intent(MainActivity.this,PastActivity.class);
                startActivity(past1);
            }
        });
    }

     /*
    --------------------------------------------Permissions----------------------------------
    */

    /**
     * checks to see the permission is given
     * @param
     */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    /**
     *  ========================= Used for volume key button ===================================
     */

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //get the time from the calendar and show notification
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 5);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            return true;
        }
        else {
            return super.onKeyDown(keyCode,event);
        }
    }
}


