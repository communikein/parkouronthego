package it.communikein.waveonthego.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.communikein.waveonthego.R;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primaryText)
    public TextView mNameTextView;
    @BindView(R.id.secondaryText)
    public TextView mLocationTextView;
    @BindView(R.id.dateText)
    public TextView mDateTextView;
    public final View mView;

    EventViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);

        mView = v;
    }
}
