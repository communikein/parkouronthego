package it.communikein.waveonthego.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.communikein.waveonthego.R;

/**
 * Created by eliam on 15/04/2017.
 */

public class SpotViewHolder extends RecyclerView.ViewHolder {
    public final TextView mNameTextView;
    public final TextView mSummaryTextView;
    public final TextView mLocationText;

    SpotViewHolder(View v) {
        super(v);

        mNameTextView = (TextView) v.findViewById(R.id.primaryText);
        mSummaryTextView = (TextView) v.findViewById(R.id.secondaryText);
        mLocationText = (TextView) v.findViewById(R.id.locationText);
    }
}
