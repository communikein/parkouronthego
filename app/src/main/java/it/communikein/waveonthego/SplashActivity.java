package it.communikein.waveonthego;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.communikein.waveonthego.db.DBHandler;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_FIREBASE_SIGN_IN = 123;

    @BindView(R.id.app_icon)
    public View imageView;

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
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DBHandler.getInstance().setupDB();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // For a list of all scopes, see:
            // https://developers.google.com/identity/protocols/googlescopes
            AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                    .setPermissions(Arrays.asList(Scopes.PROFILE, Scopes.EMAIL))
                    .build();

            // For a list of permissions see:
            // https://developers.facebook.com/docs/facebook-login/android
            // https://developers.facebook.com/docs/facebook-login/permissions
            AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                    .setPermissions(Arrays.asList("public_profile", "email"))
                    .build();

            // not signed in
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.GreenTheme)
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    googleIdp,
                                    facebookIdp))
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    REQUEST_FIREBASE_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FIREBASE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            int messageID = R.string.unknown_sign_in_response;

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                startActivity(MainActivity.createIntent(this, response));
                finish();
                return;
            }
            // Sign in failed
            else {
                if (response == null)
                    // User pressed back button
                    messageID = R.string.sign_in_cancelled;
                else {
                    if (response.getErrorCode() == ErrorCodes.NO_NETWORK)
                        messageID = R.string.no_internet_connection;

                    if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR)
                        messageID = R.string.unknown_error;
                }
            }

            showSnackbar(messageID);
        }
    }

    private void showSnackbar(int title) {
        Snackbar.make(imageView, title, Snackbar.LENGTH_LONG).show();
    }
}
