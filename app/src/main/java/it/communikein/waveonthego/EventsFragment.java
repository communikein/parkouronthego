package it.communikein.waveonthego;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import it.communikein.waveonthego.adapter.FirebaseEventListAdapter;
import it.communikein.waveonthego.datatype.Event;
import it.communikein.waveonthego.db.DBHandler;
import it.communikein.waveonthego.views.EventViewHolder;

/**
 *
 * Created by Elia Maracani on 18/02/2017.
 */
public class EventsFragment extends Fragment
        implements FirebaseEventListAdapter.OnItemClick{

    @BindView(R.id.login_button)
    public LoginButton mFBLoginButton;

    private CallbackManager mCallbackManager;

    private FirebaseEventListAdapter mAdapter;
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

    public EventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ref = FirebaseDatabase.getInstance().getReference(DBHandler.DB_EVENTS);
        initUI(view);

        if (isLoggedIn()) {
            mFBLoginButton.setVisibility(View.GONE);
            getEvents();
        }
        else
            mFBLoginButton.setVisibility(View.VISIBLE);
    }

    private void initUI(View view) {
        mCallbackManager = CallbackManager.Factory.create();
        // If using in a fragment
        mFBLoginButton.setFragment(this);
        setFacebookLoginButton();

        // specify an adapter (see also next example)
        mAdapter = new FirebaseEventListAdapter(Event.class, EventViewHolder.class, ref);
        mAdapter.setOnItemClickListener(this);

        RecyclerView mRecyclerView = ButterKnife.findById(view, R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        unbinder.unbind();
    }

    private void getEvents() {
        if (isLoggedIn()) {
            /* make the API call */
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/parkourwave/events",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                        /* handle the result */
                            ArrayList<Event> events = parseFBResponse(response.getJSONObject());

                            for (Event event : events)
                                DBHandler.getInstance().writeToEvents(event);
                        }
                    }
            ).executeAsync();
        }
        else
            mFBLoginButton.setVisibility(View.VISIBLE);
    }

    private ArrayList<Event> parseFBResponse(JSONObject resp) {
        ArrayList<Event> ris = new ArrayList<>();

        try {
            JSONArray array = resp.getJSONArray("data");

            for (int i=0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String id = obj.getString("id");
                String name = obj.getString("name");
                String description = obj.getString("description");
                String startFB = obj.getString("start_time");
                Date end = null;
                if (obj.has("end_time")) {
                    String endFB = obj.getString("end_time");
                    String endTime = endFB.substring(endFB.lastIndexOf("T") + 1,
                            endFB.lastIndexOf("+"));
                    String endDate = endFB.substring(0, endFB.lastIndexOf("T"));
                    end = Utils.dateFBFormat.parse(endDate + " " + endTime);
                }
                String startTime = startFB.substring(startFB.lastIndexOf("T") + 1,
                        startFB.lastIndexOf("+"));
                String startDate = startFB.substring(0, startFB.lastIndexOf("T"));
                Date start = Utils.dateFBFormat.parse(startDate + " " + startTime);
                LatLng coords = null;
                String location = null;
                if (obj.has("place")) {
                    JSONObject placeJSON = obj.getJSONObject("place");
                    location = placeJSON.getString("name");
                    if (placeJSON.has("location")) {
                        double lat = placeJSON.getJSONObject("location").getDouble("latitude");
                        double lng = placeJSON.getJSONObject("location").getDouble("longitude");
                        coords = new LatLng(lat, lng);
                    }
                    else
                        coords = null;
                }

                ris.add(new Event(id, name, description, location, coords, start, end));
            }
        } catch (JSONException | ParseException e) {
            FirebaseCrash.report(e);
            ris = new ArrayList<>();
        }

        return ris;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setFacebookLoginButton(){
        mFBLoginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mFBLoginButton.setReadPermissions(Collections.singletonList("public_profile, email"));
        mFBLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFBLoginButton.setVisibility(View.GONE);
                getEvents();
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

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onItemClick(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
        intent.putExtra(Event.EVENT, event.toJSON().toString());
        startActivity(intent);
    }
}
