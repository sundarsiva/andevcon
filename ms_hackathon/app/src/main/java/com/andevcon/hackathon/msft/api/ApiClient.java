package com.andevcon.hackathon.msft.api;

import com.andevcon.hackathon.msft.model.sample.SampleRequestBody;
import com.andevcon.hackathon.msft.model.sample.SampleResponseBody;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;
import com.microsoft.onenotevos.Section;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by prtrived on 12/1/15.
 */
public class ApiClient {

    private static final String
            GET_SAMPLE_ENDPOINT_URL = "/{sampleParam}/ssampleEndpoint",
            GET_PAGES_FROM_SECTION_URL = "/me/notes/sections/{sectionId}/pages",
            GET_SECTIONS_URL = "/me/notes/sections",
            GET_USERS_URL = "/me/photo/$value",
            GET_PAGE_CONTENT_BY_ID = "/me/notes/pages/{id}/content";

    static RestAdapter getTraveLogRestAdapter() {
        return RestAdapterManager.getInstance().createRestAdapter();
    }

    public interface ApiService {

        @GET(GET_SAMPLE_ENDPOINT_URL)
        void getSampleMethod(@Path("sampleParam") String sampleParam,
                          @Body SampleRequestBody request,
                          Callback<SampleResponseBody> callback);

        @GET(GET_PAGES_FROM_SECTION_URL)
        void getPagesFromSections( @Path("sectionId") String sectionId,
                              Callback<Envelope<Page>> callback);

        @GET(GET_SECTIONS_URL)
        void getSections(Callback<Envelope<Section>> callback);

        @GET(GET_PAGE_CONTENT_BY_ID)
        void getPageContentById(
                @Path("id") String id,
                Callback<Response> callback);

        @GET(GET_USERS_URL)
        void getUserPhoto(Callback<Response> responseCallback);

        /**
         * Deletes the specified page
         *
         * @param pageId
         * @param callback
         */
        @DELETE("/me/notes/pages/{pageId}")
        void deletePage(
                @Path("pageId") String pageId,
                Callback<Envelope<Page>> callback
        );


    }

    public static ApiService apiService = getTraveLogRestAdapter().create(ApiService.class);

}
