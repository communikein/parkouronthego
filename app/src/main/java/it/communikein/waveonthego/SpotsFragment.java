package it.communikein.waveonthego;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import it.communikein.waveonthego.adapter.FirebaseSpotListAdapter;
import it.communikein.waveonthego.datatype.Spot;
import it.communikein.waveonthego.db.DBHandler;
import it.communikein.waveonthego.views.SpotViewHolder;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class SpotsFragment extends Fragment
        implements FirebaseSpotListAdapter.OnItemClick {

    private FirebaseSpotListAdapter mAdapter;
    private DatabaseReference ref;
    private Unbinder unbinder;

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            FirebaseCrash.report(ex);
            ex.getMessage();
        }
    };

    public SpotsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        View view = inflater.inflate(R.layout.fragment_spots, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ref = FirebaseDatabase.getInstance().getReference(DBHandler.DB_SPOTS);

        initUI(view);
    }

    private void initUI(View view) {
        mAdapter = new FirebaseSpotListAdapter(Spot.class, SpotViewHolder.class, ref);
        mAdapter.setOnItemClickListener(SpotsFragment.this);

        RecyclerView mRecyclerView = ButterKnife.findById(view, R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.fab_add)
    public void doAddSpot(){
        startActivity(new Intent(getActivity(), AddSpotActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Spot spot) {
        Intent intent = new Intent(getActivity(), SpotDetailsActivity.class);
        intent.putExtra(Spot.SPOT, spot.toJSON().toString());
        startActivity(intent);
    }
}
