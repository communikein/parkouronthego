package it.communikein.waveonthego;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseUser user;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAppBar();
        parseData(getIntent());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_news);
    }

    private void parseData(Intent intent) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (intent != null)
            token = intent.getStringExtra("my_token");
    }

    private void initAppBar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null)
            toolbar.setTitle(R.string.app_name);
    }

    static public Intent createIntent(Context context, IdpResponse response){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("my_token", response.getIdpToken());
        return intent;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (item.getItemId()) {
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
                case R.id.navigation_dashboard:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, new SpotsFragment())
                            .commit();
                    return true;
            }
            return false;
        }

    };
}
