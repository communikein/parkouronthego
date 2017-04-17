package it.communikein.waveonthego;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import it.communikein.waveonthego.datatype.Spot;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SpotDetailsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private final int RC_LOCATION = 24;
    private final String[] perms_location = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean allowedLocation = false;

    private TextView name_txt;
    private TextView location_txt;
    private TextView description_txt;

    private Spot mSpot;

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            e.getMessage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(handler);
        setContentView(R.layout.activity_spot_details);

        parseData();
        initUI();
        refreshUI();
    }


    private void initUI() {
        name_txt = (TextView) findViewById(R.id.eventName_txt);
        location_txt = (TextView) findViewById(R.id.location_txt);
        description_txt = (TextView) findViewById(R.id.description_txt);
        ViewPager gallery = (ViewPager) findViewById(R.id.containerMedia);

        description_txt.setMovementMethod(new ScrollingMovementMethod());

        name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MediasPagerAdapter mMediasPagerAdapter = new MediasPagerAdapter(getSupportFragmentManager());
        gallery.setAdapter(mMediasPagerAdapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeMeThere();
            }
        });
    }

    private void parseData() {
        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra(Spot.SPOT);

            try {
                JSONObject obj = new JSONObject(str);

                mSpot = new Spot(obj);
            } catch (JSONException e) {
                mSpot = null;
            }
        }
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void requestLocationPermission() {
        if (EasyPermissions.hasPermissions(SpotDetailsActivity.this, perms_location)) {
            allowedLocation = true;
        } else {
            allowedLocation = false;
            EasyPermissions.requestPermissions(SpotDetailsActivity.this,
                    getString(R.string.error_location_permission),
                    RC_LOCATION, perms_location);
        }
    }

    private void refreshUI() {
        name_txt.setText(mSpot.getName());
        description_txt.setText(mSpot.getDescription());
        location_txt.setText(mSpot.getLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(mSpot.getCoords()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mSpot.getCoords(), 15.5f));
    }


    private void takeMeThere() {
        requestLocationPermission();

        if (allowedLocation) {
            String format = "geo:0,0?q=" +
                    mSpot.getCoords().latitude + "," +
                    mSpot.getCoords().longitude +
                    "(" + mSpot.getName() + ")";
            Uri uri = Uri.parse(format);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private class MediasPagerAdapter extends FragmentStatePagerAdapter {

        MediasPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String src = mSpot.getImages().get(position);
            StorageReference imageRef = FirebaseStorage.getInstance().getReference(src);

            return OnlineMediaFragment.newInstance(imageRef);
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() { return mSpot.getImages().size();}

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
