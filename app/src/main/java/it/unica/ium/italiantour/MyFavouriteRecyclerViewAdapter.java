package it.unica.ium.italiantour;

import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.unica.ium.italiantour.FavouriteFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display an {@link InterestMarker} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyFavouriteRecyclerViewAdapter extends RecyclerView.Adapter<MyFavouriteRecyclerViewAdapter.ViewHolder> {

    private final List<InterestMarker> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyFavouriteRecyclerViewAdapter(List<InterestMarker> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getName());
        holder.mDesc.setText(mValues.get(position).getDesc());
        //todo: make thumbnail data from saved photos

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mDesc;
        public final ImageView mThumbnail;
        public InterestMarker mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.marker_title);
            mDesc = (TextView) view.findViewById(R.id.marker_desc);
            mThumbnail = (ImageView) view.findViewById(R.id.marker_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDesc.getText() + "'";
        }
    }
}
