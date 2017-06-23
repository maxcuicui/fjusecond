package com.example.sf646_a04.fjushv1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener{
    TextView[] txt;
    ImageView[] imgur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //drawer.setDrawerListener(toggle);---- is deprecated
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView btmnavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        btmnavigationView.setOnNavigationItemSelectedListener(this);
        txt = new TextView[6];
        imgur = new ImageView[6];
        int[] tid = {R.id.comd1,R.id.comd2,R.id.comd3,R.id.comd4,R.id.comd5,R.id.comd6};
        int[] iid = {R.id.t1,R.id.t11,R.id.t2,R.id.t22,R.id.t21,R.id.t12};
        for (int i = 0;i<tid.length;i++) txt[i] = (TextView) findViewById(tid[i]);
        for (int i = 0;i<iid.length;i++) imgur[i] = (ImageView) findViewById(iid[i]);
        try {
            JSONArray jsa;
            jsa = new getDBdata().execute().get();
            JSONObject jsonData = null;
            for (int i = 0;i < 6;i++){
                jsonData = jsa.getJSONObject(i);
                String purl = jsonData.getString("image1");
                txt[i].setText(jsonData.getString("describe"));
                Picasso.with(this)
                        .load(purl)
                        .resize(600,600)
                        .centerInside()
                        .into(imgur[i]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.bom_nav_upload:
                Intent uploadintend = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(uploadintend);
                break;
            case R.id.bom_nav_myinfo:
                Intent regeister = new Intent(MainActivity.this, RegeisterActivity.class);
                startActivity(regeister);
                break;
            }
                return true;
        }
}
 class getDBdata extends AsyncTask<Void, Void, JSONArray> {
    @Override
    protected JSONArray doInBackground(Void... params) {
        try {
            String result = DBconnector.executeQuery("SELECT * FROM 3c");
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                return jsonArray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(JSONArray result) {
    }
}