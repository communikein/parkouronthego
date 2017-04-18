package it.communikein.waveonthego.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.communikein.waveonthego.R;

/**
 * Created by eliam on 18/04/2017.
 */

public class AdminViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primaryText)
    public TextView mTitleTextView;
    @BindView(R.id.secondaryText)
    public TextView mSummaryTextView;
    public final View mView;

    AdminViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);

        mView = v;
    }
}