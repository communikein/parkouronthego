package it.communikein.waveonthego;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.util.List;

import it.communikein.waveonthego.datatype.Spot;
import it.communikein.waveonthego.db.DBHandler;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AddSpotActivity extends AppCompatActivity {

    private final int PLACE_PICKER_REQUEST = 23;
    private final int RC_LOCATION = 24;
    String[] perms_location = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private Toolbar toolbar;
    private EditText location_txt,
            name_txt,
            description_txt;

    private boolean allowed = false;
    private LatLng coords = null;

    Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
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
        setContentView(R.layout.activity_add_spot);

        initUI();
        initAppBar();
    }

    private void initUI() {
        location_txt = (EditText) findViewById(R.id.location_txt);
        name_txt = (EditText) findViewById(R.id.name_txt);
        description_txt = (EditText) findViewById(R.id.description_txt);

        location_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && allowed) showMapDialog();
            }
        });

        findViewById(R.id.fab_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAddSpot();
            }
        });

        requestLocationPermission();
    }

    private void initAppBar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null)
            toolbar.setTitle(R.string.title_activity_add_spot);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void requestLocationPermission() {
        if (EasyPermissions.hasPermissions(this, perms_location)) {
            allowed = true;
        } else {
            allowed = false;
            EasyPermissions.requestPermissions(this, getString(R.string.error_location_permission),
                    RC_LOCATION, perms_location);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(this, data);

                String address = "";
                if (place.getAddress().equals("")) {
                    Geocoder geo = new Geocoder(this);

                    try {
                        List<Address> addresses = geo.getFromLocation(
                                place.getLatLng().latitude, place.getLatLng().longitude, 1);

                        if (addresses.size() == 1) {
                            for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                                address += addresses.get(0).getAddressLine(i) + ", ";
                            address = address.substring(0, address.length() - 2);
                        }
                    } catch (IOException e){
                        FirebaseCrash.report(e);
                        address = "";
                    }
                }
                else
                    address = place.getAddress().toString();

                coords = place.getLatLng();
                location_txt.setText(address);
            }
        }
    }

    private void tryAddSpot() {
        String name = null, description = null, location = null;
        if (!TextUtils.isEmpty(name_txt.getText()))
            name = name_txt.getText().toString();
        if (!TextUtils.isEmpty(description_txt.getText()))
            description = description_txt.getText().toString();
        if (!TextUtils.isEmpty(location_txt.getText()))
            location = location_txt.getText().toString();

        View errorView = null;
        if (name == null) {
            name_txt.setError(getString(R.string.error_name_missing));
            errorView = name_txt;
        }
        else if (description == null) {
            description_txt.setError(getString(R.string.error_description_missing));
            errorView = description_txt;
        }
        else if (location == null) {
            location_txt.setError(getString(R.string.error_position_missing));
            errorView = location_txt;
        }
        else {
            Spot spot = new Spot(name, description, location, coords);

            DBHandler.getInstance().writeToSpots(spot);
            finish();
            return;
        }

        if (errorView != null)
            errorView.requestFocus();
    }

    private void showMapDialog(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException
                | GooglePlayServicesRepairableException e ){
            FirebaseCrash.report(e);
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Errore")
                    .setMessage("Google Play Services needed.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }
}
