package it.communikein.waveonthego.adapter;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import it.communikein.waveonthego.R;
import it.communikein.waveonthego.Utils;
import it.communikein.waveonthego.datatype.Event;
import it.communikein.waveonthego.views.EventViewHolder;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class FirebaseEventListAdapter extends FirebaseRecyclerAdapter<Event, EventViewHolder> {

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FirebaseEventListAdapter(Class<Event> modelClass,
                                    Class<EventViewHolder> viewHolderClass, Query ref) {
        super(modelClass, R.layout.entry_layout_events, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {
        viewHolder.mNameTextView.setText(model.getName());
        viewHolder.mLocationTextView.setText(model.getLocationString());
        viewHolder.mDateTextView.setText(Event.printDate(model.getDateStart(), Utils.dayMonthFormat));
    }
}

