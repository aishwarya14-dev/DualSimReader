package com.cogostech.simreader;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

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
}