package it.communikein.waveonthego;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import it.communikein.waveonthego.adapter.FirebaseAdminToBeListAdapter;
import it.communikein.waveonthego.datatype.AdminToBe;
import it.communikein.waveonthego.db.DBHandler;
import it.communikein.waveonthego.views.AdminViewHolder;

/**
 * Created by eliam on 18/04/2017.
 */

public class SettingsFragment extends Fragment {

    private FirebaseAdminToBeListAdapter mAdapter;
    private Unbinder unbinder;
    private CallbackManager mCallbackManager;

    @BindView(R.id.login_button)
    public LoginButton mFBLoginButton;
    @BindView(R.id.addAdmin_btt)
    public Button addAdmin_btt;
    @BindView(R.id.my_recycler_view)
    public RecyclerView requests_list;
    @BindView(R.id.infoAdminList_txt)
    public TextView infoAdminList_lbl;

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            FirebaseCrash.report(ex);
            ex.getMessage();
        }
    };

    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        initFB();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initUI(View view) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean admin = dataSnapshot.getValue() != null;

                addAdmin_btt.setVisibility(admin ? View.GONE : View.VISIBLE);
                infoAdminList_lbl.setVisibility(admin ? View.GONE : View.VISIBLE);
                requests_list.setVisibility(admin ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                addAdmin_btt.setVisibility(View.VISIBLE);
                infoAdminList_lbl.setVisibility(View.VISIBLE);
                requests_list.setVisibility(View.GONE);
            }
        };
        db.getReference(DBHandler.DB_ADMINS + "/" + MainActivity.user.getUid())
                .addValueEventListener(postListener);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBHandler.DB_ADMINS_WAITING);
        mAdapter = new FirebaseAdminToBeListAdapter(AdminToBe.class, AdminViewHolder.class, ref);
        mAdapter.setOnItemLongClickListener(new FirebaseAdminToBeListAdapter.OnItemLongClick() {
            @Override
            public void onItemLongClick(final AdminToBe adminToBe) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.label_add_admin))
                        .setMessage(getString(R.string.confirm_add_admin))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHandler.getInstance().confirmAdmin(adminToBe);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initFB() {
        if (isLoggedInFB())
            mFBLoginButton.setVisibility(View.GONE);
        else {
            mFBLoginButton.setVisibility(View.VISIBLE);

            mCallbackManager = CallbackManager.Factory.create();
            // If using in a fragment
            mFBLoginButton.setFragment(this);

            mFBLoginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mFBLoginButton.setReadPermissions(Collections.singletonList("public_profile, email"));
            mFBLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    mFBLoginButton.setVisibility(View.GONE);
                }

                @Override
                public void onCancel() {
                    mFBLoginButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(FacebookException error) {
                    mFBLoginButton.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private boolean isLoggedInFB() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @OnClick(R.id.addAdmin_btt)
    public void addAdmin() {
        DBHandler.getInstance().requestAdminPrivileges(
                FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        unbinder.unbind();
    }
}
