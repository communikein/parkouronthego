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
import android.widget.Button;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * Created by eliam on 18/02/2017.
 */

public class EventsFragment extends Fragment {

    private LoginButton mFBLoginButton;
    private Button mEventsButton;

    CallbackManager mCallbackManager;
    private EventAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            FirebaseCrash.report(ex);
        }
    };

    public EventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(handler);
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
    }

    private void initUI(View view) {
        mCallbackManager = CallbackManager.Factory.create();
        mFBLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        // If using in a fragment
        mFBLoginButton.setFragment(this);
        setFacebookLoginButton();

        mEventsButton = (Button) view.findViewById(R.id.events_btt);
        mEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews();
            }
        });

        ArrayList<Event> temp = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new EventAdapter(temp);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getNews() {
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

                        updateView(events);
                    }
                }
        ).executeAsync();
    }

    private ArrayList<Event> parseFBResponse(JSONObject resp) {
        ArrayList<Event> ris = new ArrayList<>();

        try {
            JSONArray array = resp.getJSONArray("data");

            for (int i=0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String name = obj.getString("name");
                String description = obj.getString("description");
                String startFB = obj.getString("start_time");
                String startTime = startFB.substring(startFB.lastIndexOf("T") + 1,
                        startFB.lastIndexOf("+"));
                String startDate = startFB.substring(0, startFB.lastIndexOf("T"));
                Date start = Event.dateFBFormat.parse(startDate + " " + startTime);
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

                ris.add(new Event(name, description, location, coords, start));
            }
        } catch (JSONException | ParseException e) {
            FirebaseCrash.report(e);
            ris = new ArrayList<>();
        }

        return ris;
    }

    private void updateView(ArrayList<Event> events) {
        mAdapter.clear();
        mAdapter.addAll(events);
        mAdapter.notifyDataSetChanged();
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
                mEventsButton.setEnabled(true);
            }

            @Override
            public void onCancel() {
                mEventsButton.setEnabled(false);
            }

            @Override
            public void onError(FacebookException error) {
                mEventsButton.setEnabled(false);
            }
        });
    }
}
