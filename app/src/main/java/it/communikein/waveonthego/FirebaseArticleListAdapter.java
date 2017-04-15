package it.communikein.waveonthego;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import it.communikein.waveonthego.datatype.Article;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */

class FirebaseArticleListAdapter extends FirebaseRecyclerAdapter<Article, ArticleViewHolder> {

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    FirebaseArticleListAdapter(Class<Article> modelClass,
                                      Class<ArticleViewHolder> viewHolderClass, Query ref) {
        super(modelClass, R.layout.entry_layout_article, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(ArticleViewHolder viewHolder, Article model, int position) {
        viewHolder.mTitleTextView.setText(model.getTitle());
        viewHolder.mSummaryTextView.setText(model.getSummary());
        viewHolder.mDateTextView.setText(model.printDate());
    }
}
