package it.communikein.waveonthego;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.communikein.waveonthego.adapter.FirebaseSpotListAdapter;
import it.communikein.waveonthego.datatype.Spot;
import it.communikein.waveonthego.db.DBHandler;
import it.communikein.waveonthego.views.SpotViewHolder;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class SpotsFragment extends android.support.v4.app.Fragment {

    private FirebaseSpotListAdapter mAdapter;
    private DatabaseReference ref;

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            FirebaseCrash.report(ex);
        }
    };

    public SpotsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        return inflater.inflate(R.layout.fragment_spots, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ref = FirebaseDatabase.getInstance().getReference(DBHandler.DB_SPOTS);

        initUI(view);
    }

    private void initUI(View view) {
        mAdapter = new FirebaseSpotListAdapter(Spot.class, SpotViewHolder.class, ref);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddSpot();
            }
        });
    }

    private void doAddSpot(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
