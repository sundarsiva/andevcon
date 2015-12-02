package com.andevcon.hackathon.msft.api;

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

        if(instance == null)
            instance = new RestAdapterManager();

        return instance;
    }

    protected RestAdapter createRestAdapter() {

        RestAdapter.Builder builder = new RestAdapter.Builder();

        //This method catches outgoing REST calls and injects the Authorization and host headers before
        //sending to REST endpoint
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                final String token = AuthenticationManager.getInstance().getAccessToken();
                if (null != token) {
                    request.addHeader("Authorization", "Bearer " + token);
                }
            }
        };

        builder.setEndpoint(Constants.MICROSOFT_GRAPH_API_ENDPOINT);
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
