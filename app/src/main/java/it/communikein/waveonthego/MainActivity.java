package it.communikein.waveonthego;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.communikein.waveonthego.db.DBHandler;


public class MainActivity extends AppCompatActivity {

    public static final SparseArray<String> imagesNotificationUpload = new SparseArray<>();

    private static final String NAV_ITEM_ID = "navItemId";
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private int mNavItemId = -1;
    private int startNavItemId;
    private final Handler mDrawerActionHandler = new Handler();
    private ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.navigation_view)
    NavigationView drawerNavView;

    public static FirebaseUser user;

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        parseData(savedInstanceState);
        initAppBar();
        initNavigationViews();
    }

    private void initAppBar() {
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (toolbar != null)
            toolbar.setTitle(R.string.app_name);
    }

    static public Intent createIntent(Context context, IdpResponse response){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("my_token", response.getIdpToken());
        return intent;
    }

    public boolean navigate(int menuItemID) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawers();

        if (menuItemID != mNavItemId){
            mNavItemId = menuItemID;

            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (menuItemID) {
                case R.id.navigation_spots:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, new SpotsFragment())
                            .commit();
                    return true;
                case R.id.navigation_news:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, new NewsFragment())
                            .commit();
                    return true;
                case R.id.navigation_events:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, new EventsFragment())
                            .commit();
                    return true;
                /* ************* SETTINGS TAB *************** */
                case R.id.navigation_settings:
                    navigation.setSelectedItemId(-1);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, new SettingsFragment())
                            .commit();
                    return true;
                /* ************* LOGOUT TAB **************** */
                case R.id.navigation_logout:
                    return true;
            }

            return false;
        }

        return false;
    }

    private void initNavigationViews(){
        if (drawerNavView != null) {
            drawerNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                    // update highlighted item in the navigation menu
                    item.setChecked(true);

                    // allow some time after closing the drawer before performing real navigation
                    // so the user can see what is happening
                    drawerLayout.closeDrawer(GravityCompat.START);
                    mDrawerActionHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigate(item.getItemId());
                        }
                    }, DRAWER_CLOSE_DELAY_MS);

                    return true;
                }
            });

            mDrawerToggle = new ActionBarDrawerToggle(
                    this, drawerLayout,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            );
            mDrawerToggle.syncState();
            drawerLayout.addDrawerListener(mDrawerToggle);

            // select the correct nav menu item
            MenuItem item = drawerNavView.getMenu().findItem(startNavItemId);
            if (item != null)
                item.setChecked(true);
        }
        initHeader();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigate(item.getItemId());
            }
        });
        navigation.setSelectedItemId(startNavItemId);

        navigate(startNavItemId);
    }

    private void initHeader() {
        View view = drawerNavView.inflateHeaderView(R.layout.nav_drawer_header);

        ImageView profile_img = ButterKnife.findById(view, R.id.circleView);
        TextView name_txt = ButterKnife.findById(view, R.id.name);
        TextView mail_txt = ButterKnife.findById(view, R.id.email);
        if (user != null) {
            name_txt.setText(user.getDisplayName());
            mail_txt.setText(user.getEmail());
        }

        Glide.with(this)
                .fromUri()
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_image)
                .into(profile_img);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void parseData(Bundle savedInstanceState) {
        // load saved navigation state if present
        if (null == savedInstanceState) {
            startNavItemId = R.id.navigation_spots;
        } else {
            startNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            navigate(R.id.navigation_spots);
        }
    }
}
