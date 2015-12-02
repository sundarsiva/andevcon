package com.andevcon.hackathon.msft.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.model.Images;
import com.microsoft.onenotevos.Page;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PagesRecylerViewAdapter extends RecyclerView.Adapter<PagesRecylerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Page> mValues;
    private ClickListener listener;

    public interface ClickListener {
        void onClickListener(Page page, int position);
        void onLongClickListener(Page page, int position, View view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public Page getValueAt(int position) {
        return mValues.get(position);
    }

    public PagesRecylerViewAdapter(Context context, List<Page> items, ClickListener listener) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Page page = mValues.get(position);
        holder.mBoundString = page.title;
        holder.mTextView.setText(page.title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(page, position);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                listener.onLongClickListener(page, position, view);
                return true;
            }
        });

        Picasso.with(holder.mImageView.getContext())
                .load(Images.getRandomCheeseDrawable())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}