package it.communikein.waveonthego;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * Created by Elia on 19/02/2015.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    private final ArrayList<Event> values;

    private OnItemClickListener mListener;

    public EventAdapter(ArrayList<Event> values) {
        this.values = values;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_layout_events, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = values.get(position);
        holder.mNameTextView.setText(event.getName());
        if (event.getLocationString() != null)
            holder.mLocationTextView.setText(event.getLocationString());
        else
            holder.mLocationTextView.setText("Posizione non disponibile.");
        holder.mDateTextView.setText(Event.printDate(event.getDateStart(), Event.dateAdapterFormat));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public ArrayList<Event> getData(){
        return values;
    }

    public void clear() { this.values.clear(); }

    public void addAll(ArrayList<Event> values){
        this.values.clear();
        this.values.addAll(values);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameTextView;
        final TextView mLocationTextView;
        final TextView mDateTextView;

        ViewHolder(CardView v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            mListener.onItemClick(getData().get(position));
                    }
                }
            });

            mNameTextView = (TextView) v.findViewById(R.id.primaryText);
            mLocationTextView = (TextView) v.findViewById(R.id.secondaryText);
            mDateTextView = (TextView) v.findViewById(R.id.dateText);
        }
    }
}
