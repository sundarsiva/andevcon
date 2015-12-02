package com.andevcon.hackathon.msft.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.adapters.SimpleStringRecyclerViewAdapter;
import com.andevcon.hackathon.msft.helpers.Constants;
import com.andevcon.hackathon.msft.model.Images;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PagesListFragment extends Fragment {

    public static PagesListFragment newInstance(String secionId) {
        PagesListFragment fragment = new PagesListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_SECTION_ID, secionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_travelog_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getRandomSublist(Images.sCheeseStrings, 30)));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

}
