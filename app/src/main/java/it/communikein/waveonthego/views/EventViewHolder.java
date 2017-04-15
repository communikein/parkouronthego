package it.communikein.waveonthego.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.communikein.waveonthego.R;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {
    public final TextView mNameTextView;
    public final TextView mLocationTextView;
    public final TextView mDateTextView;

    EventViewHolder(View v) {
        super(v);

        mNameTextView = (TextView) v.findViewById(R.id.primaryText);
        mLocationTextView = (TextView) v.findViewById(R.id.secondaryText);
        mDateTextView = (TextView) v.findViewById(R.id.dateText);
    }

}
