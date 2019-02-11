package it.unica.ium.italiantour;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import it.unica.ium.italiantour.FilterFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyFilterRecyclerViewAdapter extends RecyclerView.Adapter<MyFilterRecyclerViewAdapter.ViewHolder> {

    private final List<Filtri.Filtro> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity activity;

    public MyFilterRecyclerViewAdapter(List<Filtri.Filtro> items, OnListFragmentInteractionListener listener, FragmentActivity activity) {
        mValues = items;
        mListener = listener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).title);
        holder.thumb.setImageDrawable(mValues.get(position).icon);

        MainViewModel mvm = ViewModelProviders.of(activity).get(MainViewModel.class);

        //Initialize toggle with current categories selected.
        int categories = mvm.getFilter();
        holder.toggle.setChecked(0 != (categories & (1<<position)));

        //Toggle category every time we click its toggle.
        holder.toggle.setOnClickListener(v -> {
            mvm.newFilter(mvm.getFilter()^(1<<position));
            Log.d("Filter", mvm.getFilter().toString());
        });

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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView thumb;
        public final TextView mContentView;
        public final CheckBox toggle;
        public Filtri.Filtro mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            thumb = (ImageView) view.findViewById(R.id.filter_icon);
            mContentView = (TextView) view.findViewById(R.id.content);
            toggle = (CheckBox) view.findViewById(R.id.check);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
