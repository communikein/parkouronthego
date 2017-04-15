package it.communikein.waveonthego.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.communikein.waveonthego.R;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTitleTextView;
    public final TextView mSummaryTextView;
    public final TextView mDateTextView;

    ArticleViewHolder(View v) {
        super(v);

        mTitleTextView = (TextView) v.findViewById(R.id.primaryText);
        mSummaryTextView = (TextView) v.findViewById(R.id.secondaryText);
        mDateTextView = (TextView) v.findViewById(R.id.dateText);
    }
}
