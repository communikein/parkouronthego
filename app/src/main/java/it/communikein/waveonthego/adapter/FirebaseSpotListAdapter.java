package it.communikein.waveonthego.adapter;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import it.communikein.waveonthego.R;
import it.communikein.waveonthego.datatype.Spot;
import it.communikein.waveonthego.views.SpotViewHolder;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class FirebaseSpotListAdapter extends FirebaseRecyclerAdapter<Spot, SpotViewHolder> {

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FirebaseSpotListAdapter(Class<Spot> modelClass,
                             Class<SpotViewHolder> viewHolderClass, Query ref) {
        super(modelClass, R.layout.entry_layout_spot, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(SpotViewHolder viewHolder, Spot model, int position) {
        viewHolder.mNameTextView.setText(model.getName());
        viewHolder.mLocationText.setText(model.getLocationString());
        viewHolder.mSummaryTextView.setText(model.getDescription());
    }
}

