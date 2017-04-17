package it.communikein.waveonthego.adapter;


import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import it.communikein.waveonthego.R;
import it.communikein.waveonthego.datatype.Article;
import it.communikein.waveonthego.views.ArticleViewHolder;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */

public class FirebaseArticleListAdapter extends FirebaseRecyclerAdapter<Article, ArticleViewHolder> {

    private OnItemClick mCallBack;

    public interface OnItemClick {
        void onItemClick(Article event);
    }

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FirebaseArticleListAdapter(Class<Article> modelClass,
                                      Class<ArticleViewHolder> viewHolderClass, Query ref) {
        super(modelClass, R.layout.entry_layout_article, viewHolderClass, ref);
    }

    public void setOnItemClickListener(OnItemClick context) {
        mCallBack = context;
    }

    @Override
    protected void populateViewHolder(ArticleViewHolder viewHolder, final Article model, int position) {
        viewHolder.mTitleTextView.setText(model.getTitle());
        viewHolder.mSummaryTextView.setText(model.getSummary());
        viewHolder.mDateTextView.setText(model.printDate());

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null)
                    mCallBack.onItemClick(model);
            }
        });
    }
}
