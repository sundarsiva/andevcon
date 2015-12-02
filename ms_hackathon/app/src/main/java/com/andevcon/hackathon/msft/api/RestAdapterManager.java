package com.andevcon.hackathon.msft.api;

import android.text.TextUtils;

import com.andevcon.hackathon.msft.BuildConfig;
import com.andevcon.hackathon.msft.helpers.Constants;
import com.microsoft.office365.connectmicrosoftgraph.AuthenticationManager;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestAdapterManager {

    protected static volatile RestAdapterManager instance;

    private RestAdapterManager() {
    }

    public static synchronized RestAdapterManager getInstance() {

        if(null == instance)
            instance = new RestAdapterManager();
        return instance;
    }

    protected RestAdapter createRestAdapter(String baseUrl) {

        //This method catches outgoing REST calls and injects the Authorization and host headers before
        //sending to REST endpoint
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                final String accessToken = AuthenticationManager.getInstance().getAccessToken();
                if (!TextUtils.isEmpty(accessToken)) {
                    request.addHeader("Authorization", "Bearer " + accessToken);
                    request.addHeader("Accept", "application/json");
                }
            }
        };

        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint(baseUrl);
        builder.setRequestInterceptor(requestInterceptor);
        builder.setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
        builder.setClient(getClient());
        return builder.build();
    }

    private OkClient getClient() {

        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(Constants.TIME_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectTimeout(Constants.TIME_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(Constants.TIME_REQUEST_TIMEOUT, TimeUnit.SECONDS);
        return new OkClient(client);
    }
}
