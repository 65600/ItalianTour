package it.unica.ium.italiantour;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getName());
        holder.mDesc.setText(mValues.get(position).getDesc());
        //todo: make thumbnail data from saved photos

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitle;
        final TextView mDesc;
        final ImageView mThumbnail;
        InterestMarker mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.marker_title);
            mDesc = view.findViewById(R.id.marker_desc);
            mThumbnail = view.findViewById(R.id.marker_thumbnail);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mDesc.getText() + "'";
        }
    }
}
