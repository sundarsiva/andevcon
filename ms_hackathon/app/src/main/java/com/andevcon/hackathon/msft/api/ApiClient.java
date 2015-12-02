package com.andevcon.hackathon.msft.api;

import com.andevcon.hackathon.msft.model.sample.SampleRequestBody;
import com.andevcon.hackathon.msft.model.sample.SampleResponseBody;
import com.microsoft.onenoteapi.service.OneNotePartsMap;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;
import com.microsoft.onenotevos.Section;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Path;

/**
 * Created by prtrived on 12/1/15.
 */
public class ApiClient {

    private static final String
            GET_SAMPLE_ENDPOINT_URL = "/{sampleParam}/ssampleEndpoint",
            GET_PAGES_FROM_SECTION_URL = "/me/notes/sections/{sectionId}/pages",
            POST_PAGE_INTO_SECTION = "/me/notes/sections/{sectionId}/pages",
            GET_SECTIONS_URL = "/me/notes/sections";

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

        @Multipart
        @POST(POST_PAGE_INTO_SECTION)
        void postPageIntoSection(@Path("sectionId") String sectionId,
                                @PartMap OneNotePartsMap partMap,
                                Callback<Envelope<Page>> callback);

        @GET(GET_SECTIONS_URL)
        void getSections(Callback<Envelope<Section>> callback);
    }

    public static ApiService apiService = getTraveLogRestAdapter().create(ApiService.class);

}
