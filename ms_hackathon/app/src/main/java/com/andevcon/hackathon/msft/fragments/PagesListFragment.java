package com.andevcon.hackathon.msft.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.activities.DetailActivity;
import com.andevcon.hackathon.msft.adapters.PagesRecylerViewAdapter;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.andevcon.hackathon.msft.helpers.Constants;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;

import java.util.Arrays;
import java.util.List;

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

    @Bind(R.id.srlPagesList)
    SwipeRefreshLayout srlPagesList;

    private String mSectionId;

    private static final String TAG = PagesListFragment.class.getSimpleName();

    public static PagesListFragment newInstance(String secionId) {
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
            Log.d(TAG, "onCreate() called with: " + "mSectionId = [" + mSectionId + "]");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_travelog_pages_list, null);
        ButterKnife.bind(this, view);
        initUI();
        fetchPages();
        return view;
    }

    private void initUI() {
        srlPagesList.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        srlPagesList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPages();
            }
        });
    }

    private void fetchPages() {
        togglePbVisibility(true);
        ApiClient.apiService.getPagesFromSections(mSectionId, new Callback<Envelope<Page>>() {
            @Override
            public void success(Envelope<Page> pageEnvelope, Response response) {
                togglePbVisibility(false);
                srlPagesList.setRefreshing(false);
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
                srlPagesList.setRefreshing(false);
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
            rvPagesList.setAdapter(new PagesRecylerViewAdapter(getActivity(), pagesList, new PagesRecylerViewAdapter.ClickListener() {
                @Override
                public void onClickListener(Page page, int position) {
                    launchDetailActivity(page);
                }

                @Override
                public void onLongClickListener(Page page, int position, View view) {
                    showPopUpMenu(page, view);
                }
            }));
        }
    }

    private void showPopUpMenu(final Page page, final View viewLocal) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), viewLocal);
        popupMenu.getMenuInflater().inflate(R.menu.page_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_share) {
                    launchShareIntent(page);
                } else if (menuItem.getItemId() == R.id.item_delete) {
                    ApiClient.apiService.deletePage(page.id, new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            Toast.makeText(getActivity(), page.title + " is deleted", Toast.LENGTH_SHORT).show();
                            fetchPages();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), page.title + " is not deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Link is copied to clipboard - \n\n" + page.contentUrl, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void launchShareIntent(Page page) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, page.title);
            i.putExtra(Intent.EXTRA_TEXT, page.contentUrl);
            startActivity(Intent.createChooser(i, "Share with...."));
        } catch(Exception e) {
            Log.e(TAG, "exception while sharing - " + e.getLocalizedMessage());
        }
    }

    private void launchDetailActivity(Page page) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PAGE_ID, page.id);
        intent.putExtra(DetailActivity.EXTRA_PAGE_NAME, page.title);
        startActivity(intent);
    }
}
