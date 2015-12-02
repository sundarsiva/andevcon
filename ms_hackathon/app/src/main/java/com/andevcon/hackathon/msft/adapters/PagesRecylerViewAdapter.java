package com.andevcon.hackathon.msft.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.activities.DetailActivity;
import com.andevcon.hackathon.msft.model.Images;
import com.microsoft.onenotevos.Page;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PagesRecylerViewAdapter extends RecyclerView.Adapter<PagesRecylerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Page> mValues;

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

    public PagesRecylerViewAdapter(Context context, List<Page> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBoundString = mValues.get(position).title;
        holder.mTextView.setText(mValues.get(position).title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_NAME, holder.mBoundString);

                context.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                final View viewLocal = view;
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.page_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.item_share){
                            Toast.makeText(viewLocal.getContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        } else if (menuItem.getItemId() == R.id.item_delete){
                            Toast.makeText(viewLocal.getContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(viewLocal.getContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
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