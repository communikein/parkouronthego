package it.communikein.waveonthego;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.communikein.waveonthego.datatype.Spot;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SpotDetailsActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnlineMediaFragment.OnMediaClick {

    private final int RC_LOCATION = 24;
    private final String[] perms_location = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean allowedLocation = false;

    @BindView(R.id.name_txt)
    public TextView name_txt;
    @BindView(R.id.location_txt)
    public TextView location_txt;
    @BindView(R.id.description_txt)
    public TextView description_txt;

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
        ButterKnife.bind(this);

        parseData();
        initUI();
        refreshUI();
    }

    private void initUI() {
        description_txt.setMovementMethod(new ScrollingMovementMethod());

        MediasPagerAdapter mMediasPagerAdapter = new MediasPagerAdapter(getSupportFragmentManager());
        ViewPager gallery = ButterKnife.findById(this, R.id.containerMedia);
        gallery.setAdapter(mMediasPagerAdapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void parseData() {
        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra(Spot.SPOT);

            try {
                JSONObject obj = new JSONObject(str);

                mSpot = Spot.fromJSON(obj);
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

    @OnClick(R.id.fab)
    public void takeMeThere() {
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

    @OnClick(R.id.name_txt)
    public void closeActivity() {
        finish();
    }

    @Override
    public void onMediaClick(StorageReference media) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_picture_fullscreen);
        dialog.setCancelable(true);

        dialog.findViewById(R.id.imageFull).setVisibility(View.VISIBLE);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageFull);

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(media)
                .placeholder(R.drawable.ic_image)
                .into(imageView);

        //now that the dialog is set up, it's time to show it
        dialog.show();
    }

    private class MediasPagerAdapter extends FragmentStatePagerAdapter {

        MediasPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String src = mSpot.getImages().get(position);
            StorageReference imageRef = FirebaseStorage.getInstance().getReference(src);
            OnlineMediaFragment mediaFragment = OnlineMediaFragment.newInstance(imageRef);
            mediaFragment.setOnMediaClickListener(SpotDetailsActivity.this);

            return mediaFragment;
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
