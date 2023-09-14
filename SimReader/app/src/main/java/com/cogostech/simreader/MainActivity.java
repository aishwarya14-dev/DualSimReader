package com.cogostech.simreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Context;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import static com.cogostech.simreader.R.id.action_settings;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private AppBarConfiguration appBarConfiguration;

    public static Context getMainActContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GsmCellLocation cl = (GsmCellLocation) CellLocation.getEmpty();
//        CellLocation.requestLocationUpdate();
//        System.out.println("GsmCellLocation " + cl.toString());
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_dual_sim_card_main);
        requestPermission();
        clockwise(findViewById(R.id.listView));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readSims() {

        SimReader simReader = SimReader.getSimInfo();//getMActContext());

        final int listView1 = R.id.listView;
        final ListView lv = (ListView) findViewById(listView1);

        DualSimView ad = new DualSimView(this, simReader.simitems);
        lv.setAdapter(ad);
    }

    public void clockwise(View view) {
        ImageView image = (ImageView) findViewById(R.id.dualSIMimageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        image.startAnimation(animation);
        readSims();
    }

    public  void requestPermission(){
        if (Build.VERSION.SDK_INT < 30) {
            if (ActivityCompat.checkSelfPermission(MainActivity.getMainActContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                // if permission is not granted then we are requesting for the permissions on below line.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
            } else {
                // if permission is already granted then we are displaying a toast message as permission granted.
                Toast.makeText(MainActivity.getMainActContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            if (ActivityCompat.checkSelfPermission(MainActivity.getMainActContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                // if permission is not granted then we are requesting for the permissions on below line.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
            }
            if (ActivityCompat.checkSelfPermission(MainActivity.getMainActContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
                // if permission is not granted then we are requesting for the permissions on below line.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 100);
            }
            if (ActivityCompat.checkSelfPermission(MainActivity.getMainActContext(), Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_DENIED) {
                // if permission is not granted then we are requesting for the permissions on below line.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS}, 100);
            }
            Toast.makeText(MainActivity.getMainActContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}
