package it.communikein.waveonthego;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import it.communikein.waveonthego.adapter.FirebaseAdminListAdapter;
import it.communikein.waveonthego.adapter.FirebaseArticleListAdapter;
import it.communikein.waveonthego.datatype.Admin;
import it.communikein.waveonthego.datatype.Article;
import it.communikein.waveonthego.db.DBHandler;
import it.communikein.waveonthego.views.AdminViewHolder;
import it.communikein.waveonthego.views.ArticleViewHolder;

/**
 * Created by eliam on 18/04/2017.
 */

public class SettingsFragment extends Fragment {

    private FirebaseAdminListAdapter mAdapter;
    private DatabaseReference ref;
    private Unbinder unbinder;

    @BindView(R.id.addAdmin_btt)
    public Button addAdmin_btt;
    @BindView(R.id.my_recycler_view)
    public RecyclerView requests_list;

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

        ref = FirebaseDatabase.getInstance().getReference(DBHandler.DB_USERS);

        initUI(view);
    }

    private void initUI(View view) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = (MainActivity.user);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean admin = false;
                if (dataSnapshot.getValue() != null)
                    admin = (boolean) dataSnapshot.getValue();

                addAdmin_btt.setVisibility(admin ? View.GONE : View.VISIBLE);
                requests_list.setVisibility(admin ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                addAdmin_btt.setVisibility(View.VISIBLE);
                requests_list.setVisibility(View.GONE);
            }
        };
        db.getReference(DBHandler.DB_ADMINS + "/" + user.getUid()).addValueEventListener(postListener);

        mAdapter = new FirebaseAdminListAdapter(Admin.class, AdminViewHolder.class, ref);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
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