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
public class SpotViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primaryText)
    public TextView mNameTextView;
    @BindView(R.id.secondaryText)
    public TextView mSummaryTextView;
    @BindView(R.id.locationText)
    public TextView mLocationText;
    public final View mView;

    public SpotViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);

        mView = v;
    }
}
