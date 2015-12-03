package com.andevcon.hackathon.msft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.model.UsersDTO;

import java.util.List;

/**
 * Created by krunalshah on 12/2/15.
 */
public class FriendsListAdapter extends ArrayAdapter<UsersDTO> {

    private final Context context;
    private final List<UsersDTO> users;

    public FriendsListAdapter(Context context,  List<UsersDTO> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_item, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_friend_name);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_friend_phone);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tv_friend_email);
        tvName.setText(users.get(position).getDisplayName());
        tvPhone.setText(users.get(position).getMobilePhone());
        tvEmail.setText(users.get(position).getUserPrincipalName());
        return convertView;
    }

}
