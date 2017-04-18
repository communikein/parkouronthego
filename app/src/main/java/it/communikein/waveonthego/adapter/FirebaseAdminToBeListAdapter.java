package it.communikein.waveonthego.adapter;

import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import it.communikein.waveonthego.R;
import it.communikein.waveonthego.datatype.AdminToBe;
import it.communikein.waveonthego.views.AdminViewHolder;

/**
 *
 * Created by Elia Maracani on 18/04/2017.
 */
public class FirebaseAdminToBeListAdapter extends FirebaseRecyclerAdapter<AdminToBe, AdminViewHolder> {

    private OnItemLongClick mCallBack;

    public interface OnItemLongClick {
        void onItemLongClick(AdminToBe adminToBe);
    }

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FirebaseAdminToBeListAdapter(Class<AdminToBe> modelClass,
                                        Class<AdminViewHolder> viewHolderClass, Query ref) {
        super(modelClass, R.layout.entry_layout_admin, viewHolderClass, ref);
    }

    public void setOnItemLongClickListener(OnItemLongClick context) {
        mCallBack = context;
    }

    @Override
    protected void populateViewHolder(AdminViewHolder viewHolder, final AdminToBe model, int position) {
        viewHolder.mTitleTextView.setText(model.getName());
        viewHolder.mSummaryTextView.setText(model.getMail());

        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCallBack != null)
                    mCallBack.onItemLongClick(model);
                return true;
            }
        });
    }
}
