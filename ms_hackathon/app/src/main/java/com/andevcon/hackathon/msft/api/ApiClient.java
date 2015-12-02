package com.andevcon.hackathon.msft.api;

import com.andevcon.hackathon.msft.model.sample.SampleRequestBody;
import com.andevcon.hackathon.msft.model.sample.SampleResponseBody;
import com.microsoft.onenoteapi.service.OneNotePartsMap;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;
import com.microsoft.onenotevos.Section;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.mime.TypedString;

/**
 * Created by prtrived on 12/1/15.
 */
public class ApiClient {

    private static final String
            GET_SAMPLE_ENDPOINT_URL = "/{sampleParam}/ssampleEndpoint",
            GET_PAGES_FROM_SECTION_URL = "/me/notes/sections/{sectionId}/pages",
            GET_SECTIONS_URL = "/me/notes/sections",
            GET_USERS_URL = "/me/photo/$value",
            GET_PAGE_CONTENT_BY_ID = "/me/notes/pages/{id}/content",
            POST_PAGE_INTO_SECTION = "/me/notes/sections/{sectionId}/pages",
            DELETE_PAGE_URL = "/me/notes/pages/{pageId}";

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
        void postPageWithImages(@Path("sectionId") String sectionId,
                                 @PartMap OneNotePartsMap partMap,
                                 Callback<Envelope<Page>> callback);

        @Headers("Content-Type:text/html")
        @POST(POST_PAGE_INTO_SECTION)
        void postSimplePage(@Path("sectionId") String sectionId,
                            @Body TypedString content,
                            Callback<Page> callback);

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
        @DELETE(DELETE_PAGE_URL)
        void deletePage(
                @Path("pageId") String pageId,
                Callback<Response> callback
        );
    }

    public static ApiService apiService = getTraveLogRestAdapter().create(ApiService.class);

}
