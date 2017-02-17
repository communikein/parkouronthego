package it.communikein.waveonthego;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 *
 * Created by Elia on 19/02/2015.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    interface OnLastItemShowed {
        void onLastItemShowed(Article last);
    }

    private final ArrayList<Article> values;
    private OnLastItemShowed lastItemShowedListener;

    public ArticleAdapter(ArrayList<Article> values) {
        this.values = values;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_layout_article, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = values.get(position);
        holder.mTitleTextView.setText(article.getTitle());
        holder.mSummaryTextView.setText(article.getSummary());
        holder.mDateTextView.setText(article.printDate());

        if (lastItemShowedListener != null && position == values.size() - 1)
            lastItemShowedListener.onLastItemShowed(article);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public ArrayList<Article> getData(){
        return values;
    }

    public void clear() { this.values.clear(); }

    public void addAll(ArrayList<Article> values){
        this.values.clear();
        this.values.addAll(values);
    }

    public void setOnLastItemShowedListener(OnLastItemShowed listener) {
        lastItemShowedListener = listener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleTextView;
        final TextView mSummaryTextView;
        final TextView mDateTextView;

        ViewHolder(CardView v) {
            super(v);

            mTitleTextView = (TextView) v.findViewById(R.id.primaryText);
            mSummaryTextView = (TextView) v.findViewById(R.id.secondaryText);
            mDateTextView = (TextView) v.findViewById(R.id.dateText);
        }
    }
}
