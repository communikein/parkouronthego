package it.communikein.waveonthego;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
class ArticleViewHolder extends RecyclerView.ViewHolder {
    final TextView mTitleTextView;
    final TextView mSummaryTextView;
    final TextView mDateTextView;

    ArticleViewHolder(View v) {
        super(v);

        mTitleTextView = (TextView) v.findViewById(R.id.primaryText);
        mSummaryTextView = (TextView) v.findViewById(R.id.secondaryText);
        mDateTextView = (TextView) v.findViewById(R.id.dateText);
    }
}
