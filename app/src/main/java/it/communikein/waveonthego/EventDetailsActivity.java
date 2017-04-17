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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.communikein.waveonthego.datatype.Event;
import it.communikein.waveonthego.db.DBHandler;

public class EventDetailsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    @BindView(R.id.name_txt)
    TextView name_txt;
    @BindView(R.id.dateTime_txt)
    TextView dateTime_txt;
    @BindView(R.id.location_txt)
    TextView location_txt;
    @BindView(R.id.description_txt)
    TextView description_txt;

    private GoogleMap mMap;
    private Event event;

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
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        initUI();
        parseData();
        refreshUI();
    }

    private void initUI() {
        description_txt.setMovementMethod(new ScrollingMovementMethod());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void parseData() {
        Intent intent = getIntent();

        if (intent != null) {
            String tmp = intent.getStringExtra(Event.EVENT);
            try {
                JSONObject obj = new JSONObject(tmp);
                event = Event.fromJSON(obj);
            } catch (JSONException e) {
                event = null;
            }
        }
    }

    private void refreshUI() {
        name_txt.setText(event.getName());
        description_txt.setText(event.getDescription());
        location_txt.setText(event.getLocation());

        String startEndTime =
                "Start: " + Event.printDate(event.getDateStart(), Utils.dateTimeFormat)
                + "\n"
                + "End: " + Event.printDate(event.getDateEnd(), Utils.dateTimeFormat);
        dateTime_txt.setText(startEndTime);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (event.getCoords() == null ||
                event.getCoords().latitude < 0 || event.getCoords().longitude < 0)
            updateAddress(this);
    }


    private void updateAddress(final Context context){
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context);
                try{
                    List<Address> add = geocoder.getFromLocationName(event.getLocation(), 1);

                    if (add != null && add.size() > 0 && add.get(0) != null) {
                        event.setCoords(new LatLng(
                                add.get(0).getLatitude(),
                                add.get(0).getLongitude()));

                        DBHandler.getInstance().updateEvent(event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void coords) {
                super.onPostExecute(coords);

                if (mMap != null && event.getCoords() != null &&
                        event.getCoords().latitude >= 0 && event.getCoords().longitude >= 0) {
                    mMap.addMarker(new MarkerOptions().position(event.getCoords()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(event.getCoords(), 15.5f));
                }
            }
        }).execute();
    }

    @OnClick(R.id.fab)
    public void takeMeThere() {
        String format = "geo:0,0?q=" + event.getLatitude() + "," + event.getLongitude() +
                "(" + event.getName() + ")";
        Uri uri = Uri.parse(format);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.name_txt)
    public void closeActivity() {
        finish();
    }
}
