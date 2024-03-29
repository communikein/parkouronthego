package it.communikein.waveonthego;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.communikein.waveonthego.datatype.Spot;
import it.communikein.waveonthego.db.DBHandler;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AddSpotActivity extends AppCompatActivity implements
        LocalMediaFragment.OnMediaLongClick, LocalMediaFragment.OnMediaClick {

    private final int PLACE_PICKER_REQUEST = 23;

    private final int RC_LOCATION = 24;
    private final String[] perms_location = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean allowedLocation = false;

    private final int RC_READ_STORAGE = 91;
    private final String perms_storage[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int SELECT_PICTURE = 1;
    private boolean allowedStorage = false;

    @BindView(R.id.location_txt)
    EditText location_txt;
    @BindView(R.id.name_txt)
    EditText name_txt;
    @BindView(R.id.description_txt)
    EditText description_txt;
    @BindView(R.id.containerMedia)
    ViewPager gallery;
    private MediasPagerAdapter mMediasPagerAdapter;

    private LatLng coords = null;
    private final ArrayList<File> medias = new ArrayList<>();

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
        setContentView(R.layout.activity_add_spot);
        ButterKnife.bind(this);

        initUI();
        initAppBar();
    }

    private void initUI() {
        location_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && allowedLocation) showMapDialog();
            }
        });

        mMediasPagerAdapter = new MediasPagerAdapter(getSupportFragmentManager());
        gallery.setAdapter(mMediasPagerAdapter);
        gallery.setVisibility(View.GONE);

        requestLocationPermission();
    }

    private void initAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null)
            toolbar.setTitle(R.string.title_activity_add_spot);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void requestLocationPermission() {
        if (EasyPermissions.hasPermissions(AddSpotActivity.this, perms_location)) {
            allowedLocation = true;
        } else {
            allowedLocation = false;
            EasyPermissions.requestPermissions(AddSpotActivity.this,
                    getString(R.string.error_location_permission),
                    RC_LOCATION, perms_location);
        }
    }

    @AfterPermissionGranted(RC_READ_STORAGE)
    private void requestStoragePermission() {
        if (EasyPermissions.hasPermissions(AddSpotActivity.this, perms_storage)) {
            allowedStorage = true;
        } else {
            allowedStorage = false;
            EasyPermissions.requestPermissions(AddSpotActivity.this,
                    getString(R.string.permission_storage_reason),
                    RC_READ_STORAGE, perms_storage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onMediaLongClick(final File media) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_media))
                .setMessage(getString(R.string.confirm_remove_media))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int pos = medias.indexOf(media);

                        medias.remove(pos);
                        if (medias.size() == 0) gallery.setVisibility(View.GONE);
                        mMediasPagerAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onMediaClick(final File media) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_picture_fullscreen);
        dialog.setCancelable(true);

        dialog.findViewById(R.id.imageFull).setVisibility(View.VISIBLE);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageFull);

        Glide.with(this)
                .load(media)
                .placeholder(R.drawable.ic_image)
                .into(imageView);

        //now that the dialog is set up, it's time to show it
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE && data != null) {
                Uri selectedImage = data.getData();

                String selectedMediaPath = Utils.getPath(selectedImage, this);
                if (!selectedMediaPath.equals("")) {
                    File newMedia = new File(selectedMediaPath);
                    medias.add(newMedia);

                    gallery.setVisibility(View.VISIBLE);
                    mMediasPagerAdapter.notifyDataSetChanged();
                }
            }

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

    @OnClick(R.id.fab_add)
    public void tryAddSpot() {
        Spot spot = tryMakeSpot();
        if (spot != null) {
            String key = DBHandler.getInstance().writeToSpots(spot);
            spot.setID(key);
            tryUploadImages(spot);

            finish();
        }
    }

    private Spot tryMakeSpot() {
        Spot ok = null;

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
        else
            ok = new Spot(name, description, location, coords);

        if (errorView != null)
            errorView.requestFocus();

        return ok;
    }

    @OnClick(R.id.fab_images)
    public void tryChooseImage() {
        requestStoragePermission();

        if (allowedStorage)
            showImageDialog();
    }

    @SuppressWarnings("VisibleForTests")
    private void tryUploadImages(final Spot spot) {
        StorageReference spotImagesRef =
                FirebaseStorage.getInstance().getReference().child("images/");

        for (File file : medias) {
            Uri uri = Uri.fromFile(file);
            StorageReference newImageRef = spotImagesRef.child(
                    spot.getID() + "/" + uri.getLastPathSegment());
            UploadTask upload = newImageRef.putFile(uri);

            // Sets an ID for the notification, so it can be updated
            int tmp = 0;
            if (MainActivity.imagesNotificationUpload.size() > 0)
                tmp = MainActivity.imagesNotificationUpload
                        .keyAt(MainActivity.imagesNotificationUpload.size() - 1) + 1;
            final int newID = tmp;

            uploadNotification(newID, R.string.uploading_image, -1);
            MainActivity.imagesNotificationUpload.append(newID, spot.getID());

            // Register observers to listen for when the download is done or if it fails
            upload.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) (
                            100 * ((double) taskSnapshot.getBytesTransferred() /
                            (double) taskSnapshot.getTotalByteCount()));

                    uploadNotification(newID, R.string.uploading_image, progress);
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadNotification(newID, R.string.upload_paused, -1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    FirebaseCrash.report(exception);

                    uploadNotification(newID, R.string.upload_failed, -1);
                    MainActivity.imagesNotificationUpload.remove(newID);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadNotification(newID, R.string.upload_completed, 100);
                    MainActivity.imagesNotificationUpload.remove(newID);

                    String cloudPath = taskSnapshot.getMetadata().getReference().getPath();
                    spot.addImage(cloudPath);
                    DBHandler.getInstance().updateSpot(spot);
                }
            });
        }
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

    private void showImageDialog() {
        Intent pickIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent;
        pickIntent.setType("image/*");
        chooserIntent = Intent.createChooser(pickIntent,
                getString(R.string.select_image));

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    private class MediasPagerAdapter extends FragmentStatePagerAdapter {

        MediasPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            LocalMediaFragment mediaFragment = LocalMediaFragment.newInstance(
                    medias.get(position));
            mediaFragment.setOnMediaLongClickListener(AddSpotActivity.this);
            mediaFragment.setOnMediaClickListener(AddSpotActivity.this);

            return mediaFragment;
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() { return medias.size();}

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private void uploadNotification(int id, int text, int progress) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.uploading))
                .setContentText(getString(text))
                .setSmallIcon(R.drawable.ic_image);

        if (progress >= 0)
                mNotifyBuilder.setProgress(100, progress, false);

        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(id, mNotifyBuilder.build());
    }
}
