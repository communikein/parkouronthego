package it.communikein.waveonthego;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
class EventViewHolder extends RecyclerView.ViewHolder {
    final TextView mNameTextView;
    final TextView mLocationTextView;
    final TextView mDateTextView;

    EventViewHolder(View v) {
        super(v);

        mNameTextView = (TextView) v.findViewById(R.id.primaryText);
        mLocationTextView = (TextView) v.findViewById(R.id.secondaryText);
        mDateTextView = (TextView) v.findViewById(R.id.dateText);
    }

}
