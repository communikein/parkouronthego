package it.communikein.waveonthego;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import it.communikein.waveonthego.datatype.Event;

public class EventDetailsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private TextView name_txt;
    private TextView dateTime_txt;
    private TextView location_txt;
    private TextView description_txt;
    private GoogleMap mMap;

    private String mLocation;
    private String mName;
    private String mDescription;
    private Date mStart;
    private Date mEnd = null;
    private LatLng mCoords;

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(handler);
        setContentView(R.layout.activity_event_details);

        initUI();
        parseData();
        refreshUI();
    }


    private void initUI() {
        name_txt = (TextView) findViewById(R.id.eventName_txt);
        dateTime_txt = (TextView) findViewById(R.id.dateTime_txt);
        location_txt = (TextView) findViewById(R.id.location_txt);
        description_txt = (TextView) findViewById(R.id.description_txt);
        description_txt.setMovementMethod(new ScrollingMovementMethod());

        name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            mLocation = intent.getStringExtra("LOCATION");
            mName = intent.getStringExtra("NAME");
            mDescription = intent.getStringExtra("DESCRIPTION");

            String start = intent.getStringExtra("START");
            String end = intent.getStringExtra("END");
            try {
                mStart = Utils.dateFBFormat.parse(start);
                if (end != null)
                    mEnd = Utils.dateFBFormat.parse(end);
            } catch (ParseException e) {
                mStart = null;
                mEnd = null;
            }
        }
    }

    private void refreshUI() {
        name_txt.setText(mName);
        description_txt.setText(mDescription);
        location_txt.setText(mLocation);

        String startEndTime = "Start: " + Event.printDate(mStart, Utils.dateTimeFormat)
                + "\n"
                + "End: " + Event.printDate(mEnd, Utils.dateTimeFormat);
        dateTime_txt.setText(startEndTime);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateAddress(this);
    }


    private void updateAddress(final Context context){
        (new AsyncTask<Void, Void, LatLng>() {
            @Override
            protected LatLng doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context);
                try{
                    List<Address> add = geocoder.getFromLocationName(mLocation, 1);

                    if (add != null && add.size() > 0 && add.get(0) != null) {
                        mCoords = new LatLng(add.get(0).getLatitude(), add.get(0).getLongitude());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return mCoords;
            }

            @Override
            protected void onPostExecute(LatLng coords) {
                super.onPostExecute(coords);

                if (mMap != null && coords != null) {
                    mMap.addMarker(new MarkerOptions().position(coords));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 15.5f));
                }
            }
        }).execute();
    }

    private void takeMeThere() {
        String format = "geo:0,0?q=" + mCoords.latitude + "," + mCoords.longitude +
                "(" + mName + ")";
        Uri uri = Uri.parse(format);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
