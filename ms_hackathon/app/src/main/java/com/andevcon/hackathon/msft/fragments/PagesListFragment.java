package com.andevcon.hackathon.msft.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.adapters.PagesRecylerViewAdapter;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.andevcon.hackathon.msft.helpers.Constants;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PagesListFragment extends Fragment {

    @Bind(R.id.rvPagesList)
    RecyclerView rvPagesList;

    @Bind(R.id.pbPagesList)
    ProgressBar pbPagesList;

    private String mSectionId;

    private static final String TAG = PagesListFragment.class.getSimpleName();
    private LayoutInflater inflater;

    public static PagesListFragment newInstance(String secionId) {
        secionId = "1-b87ae439-4fb2-4d98-b7ce-f72226e90777";
        PagesListFragment fragment = new PagesListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_SECTION_ID, secionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSectionId = getArguments().getString(Constants.KEY_SECTION_ID, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_travelog_pages_list, null);
        ButterKnife.bind(this, view);
        fetchPages();
        return view;
    }

    private void fetchPages() {
        togglePbVisibility(true);
        ApiClient.apiService.getPagesFromSections(mSectionId, new Callback<Envelope<Page>>() {
            @Override
            public void success(Envelope<Page> pageEnvelope, Response response) {
                togglePbVisibility(false);
                if (pageEnvelope != null) {
                    List<Page> pagesList = Arrays.asList(pageEnvelope.value);
                    if (pagesList != null && pagesList.size() > 0) {
                        setupRecyclerView(pagesList);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                togglePbVisibility(false);
                Log.e(TAG, "Failed to fetchPages - " + Log.getStackTraceString(error));
            }
        });
    }

    private void togglePbVisibility(boolean show) {
        pbPagesList.setVisibility(show ? View.VISIBLE : View.GONE);
        rvPagesList.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void setupRecyclerView(List<Page> pagesList) {
        if (rvPagesList != null) {
            rvPagesList.setLayoutManager(new LinearLayoutManager(rvPagesList.getContext()));
            rvPagesList.setAdapter(new PagesRecylerViewAdapter(getActivity(), pagesList));
        }
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
