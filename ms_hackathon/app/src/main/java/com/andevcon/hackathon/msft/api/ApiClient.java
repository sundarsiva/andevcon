package com.andevcon.hackathon.msft.api;

import com.andevcon.hackathon.msft.model.sample.SampleRequestBody;
import com.andevcon.hackathon.msft.model.sample.SampleResponseBody;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by prtrived on 12/1/15.
 */
public class ApiClient {

    private static final String
            GET_SAMPLE_ENDPOINT_URL = "/{sampleParam}/ssampleEndpoint",
            GET_SECTIONS_ENDPOINT_URL = "/{sampleParam}/ssampleEndpoint";

    static RestAdapter getTraveLogRestAdapter() {
        return RestAdapterManager.getInstance().createRestAdapter();
    }

    public interface ApiService {

        @GET(GET_SAMPLE_ENDPOINT_URL)
        void getSampleMethod(@Path("sampleParam") String sampleParam,
                          @Body SampleRequestBody request,
                          Callback<SampleResponseBody> callback);


    }

    public static ApiService apiService = getTraveLogRestAdapter().create(ApiService.class);

}
